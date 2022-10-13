package club.mineplex.clans.client.gui;

import club.mineplex.clans.client.settings.IterableSetting;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.Setting;
import club.mineplex.clans.client.settings.SettingReflect;
import club.mineplex.clans.util.object.Pair;
import lombok.SneakyThrows;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class GuiButtonDefault extends GuiButton {

    public GuiButtonDefault(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButtonDefault(final int buttonId, final int x, final int y, final int widthIn, final int heightIn,
                            final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }


    public static GuiButtonDefault asGuiButton(final SettingReflect setting, final int id, final int x, final int y,
                                               final int width,
                                               final int height) {

        return new GuiButtonDefault(id, x, y, width, height, updateValue(setting, true).getValue()) {
            @SneakyThrows
            @Override
            public void press() {
                final Pair<Object, String> result = GuiButtonDefault.updateValue(setting, false);
                final Object value = result.getKey();
                this.displayString = result.getValue();
                setting.setValue(value);
            }
        };
    }

    private static Pair<Object, String> updateValue(final SettingReflect setting, final boolean dummy) {
        final Field field = setting.getField();
        final Class<?> clazz = field.getType();
        final Setting annotation = setting.getSetting();
        final String displayFormat = annotation.displayFormat();
        final Object value = setting.getValue();

        Object valueToSet = value;
        String valueDisplay = value.toString();
        if (clazz.equals(IterableState.class)) {
            final IterableSetting iterable = field.getAnnotation(IterableSetting.class);
            final List<IterableState> states = Arrays.asList(iterable.states());
            final int indexOf = states.indexOf(value);

            if (!dummy) {
                valueToSet = states.get(indexOf + 1 > states.size() - 1 ? 0 : indexOf + 1);
            }
            valueDisplay = String.format(displayFormat, ((IterableState) valueToSet).getDisplayString());
        } else if (clazz.equals(boolean.class)) {
            if (!dummy) {
                valueToSet = !((Boolean) value);
            }
            valueDisplay = String.format(displayFormat, ((Boolean) valueToSet)
                    ? EnumChatFormatting.GREEN + "On"
                    : EnumChatFormatting.RED + "Off"
            );
        }

        final String name =
                I18n.format("clansmod.setting." + annotation.category().getAbsoluteKey() + "." + annotation.key());
        valueDisplay = name + ": " + valueDisplay;
        return new Pair<>(valueToSet, valueDisplay);
    }

    public void press() {

    }


}
