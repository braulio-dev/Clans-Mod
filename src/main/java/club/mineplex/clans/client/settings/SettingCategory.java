package club.mineplex.clans.client.settings;

import lombok.NonNull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum SettingCategory {

    // Main categories
    GENERAL("general", null),
    DISCORD("discord", null),
    MINEPLEX("mineplex", null),
    CLANS("clans", null),

    // Mineplex
    CHAMPIONS("champions", MINEPLEX),

    // Clans
    CLANS_QOL("qol", CLANS),
    VALUABLE_ITEM_DROP_PREVENTION("valuable-item-drop-prevention", CLANS);

    private final SettingCategory parent;
    private final String key;
    private final String keyAbsolute;
    private final String name;
    private final boolean root;

    private final LinkedList<SettingReflect<?>> settings = new LinkedList<>();
    private final ResourceLocation icon;

    private static final ResourceLocation DEFAULT_MODULE_ICON =
            new ResourceLocation("clansmod", "textures/mod_menu/modules/default_icon.png");

    SettingCategory(final String key, final SettingCategory parent) {
        this.parent = parent;
        this.key = key;
        this.root = parent == null;

        final StringBuilder stringBuilder = new StringBuilder();
        for (SettingCategory parent_ = parent; parent_ != null; parent_ = parent_.getParent()) {
            stringBuilder.insert(0, '.').insert(0, parent_.getKey());
        }

        stringBuilder.append(key);
        this.keyAbsolute = stringBuilder.toString();
        this.name = I18n.format("clansmod.setting." + this.keyAbsolute);
        icon = new ResourceLocation("clansmod", "textures/mod_menu/modules/" + key + "_icon.png");
    }

    public boolean isRoot() {
        return root;
    }

    public SettingCategory getParent() {
        return this.parent;
    }

    public String getKey() {
        return this.key;
    }

    public String getAbsoluteKey() {
        return this.keyAbsolute;
    }

    public String getName() {
        return this.name;
    }

    public void registerSetting(@NonNull final SettingReflect<?> setting) {
        if (this.settings.contains(setting)) {
            return;
        }

        this.settings.add(setting);
    }

    public List<SettingReflect<?>> getSettings() {
        return this.settings;
    }

    public SettingCategory[] getChildren() {
        final ArrayList<SettingCategory> settings = new ArrayList<>();
        for (final SettingCategory value : SettingCategory.values()) {
            if (value.getParent() == this) {
                settings.add(value);
            }
        }

        return settings.toArray(new SettingCategory[0]);
    }

    public void bindIconTexture() {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(icon);
        } catch (Exception ex) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(DEFAULT_MODULE_ICON);
        }
    }

}
