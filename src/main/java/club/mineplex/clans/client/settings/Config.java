package club.mineplex.clans.client.settings;

import club.mineplex.clans.ClansMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Config {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final File file;
    private final List<Class<?>> repository = new ArrayList<>();
    private final JsonObject root;

    public Config(@NonNull final File file) {
        this.file = file;

        JsonObject rootTmp;
        try (final Reader reader = Files.newBufferedReader(Paths.get(file.toURI()))) {
            rootTmp = this.gson.fromJson(reader, JsonObject.class);
        } catch (final IOException e) {
            rootTmp = new JsonObject();
        }
        this.root = rootTmp;
    }

    private void createIfNotExists(@NonNull final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                ClansMod.logger.error("Could not create \"" + file.getName() + "\n file!");
            }
        }
    }

    public final void registerClass(final Class<?> clazz) {
        this.repository.add(clazz);
    }

    @SneakyThrows
    public final void save() {
        this.loopRepository((clazz, setting) -> {
            final JsonObject section = this.root.get(clazz.getName()).getAsJsonObject();
            section.add(setting.getName(), this.gson.toJsonTree(setting.getValue(), setting.getField().getType()));
        });

        final FileWriter fileWriter = new FileWriter(this.file);
        this.gson.toJson(this.root, fileWriter);
        fileWriter.close();
    }

    public final void load() {
        this.loopRepository((clazz, setting) -> {
            final JsonObject section = this.root.get(clazz.getName()).getAsJsonObject();
            setting.getSetting().category().registerSetting(setting);

            if (!section.has(setting.getName())) {
                return;
            }

            final Object value = this.gson.fromJson(section.get(setting.getName()), setting.getField().getType());
            setting.setValue(value);
        });
    }

    @SneakyThrows
    private void loopRepository(final BiConsumer<Class<?>, SettingReflect> configMemberConsumer) {
        for (final Class<?> memberClass : this.repository) {
            final Field[] declaredFields = memberClass.getDeclaredFields();

            if (!this.root.has(memberClass.getName())) {
                this.root.add(memberClass.getName(), new JsonObject());
            }

            for (final Field field : declaredFields) {
                if (!field.isAnnotationPresent(Setting.class)) {
                    continue;
                }

                if (!this.root.has(memberClass.getName())) {
                    continue;
                }

                final boolean accessible = field.isAccessible();
                field.setAccessible(true);

                final Setting setting = field.getAnnotation(Setting.class);
                final SettingReflect fieldSetting = new SettingReflect(
                        field.getName(),
                        field.get(null),
                        setting,
                        field
                );

                configMemberConsumer.accept(memberClass, fieldSetting);
                field.setAccessible(accessible);
            }

        }
    }


}
