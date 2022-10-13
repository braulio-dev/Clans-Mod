package club.mineplex.clans.client.settings;

import club.mineplex.clans.client.gui.setting.ColorPicker;
import club.mineplex.clans.client.gui.setting.Cycler;
import club.mineplex.clans.client.gui.setting.SettingEntry;
import club.mineplex.clans.client.gui.setting.TickBox;
import lombok.Value;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.lang.reflect.Field;

@Value
public class SettingReflect<T> {

    String name;
    T value;

    Setting setting;
    Field field;

    SettingEntry<T> guiEntry;

    public SettingReflect(final String name, final T value, final Setting setting, final Field field) {
        this.name = name;
        this.value = value;
        this.setting = setting;
        this.field = field;
        guiEntry = asButton();
    }

    public T getValue() {
        this.field.setAccessible(true);
        try {
            return (T) this.field.get(null);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(final T value) {
        this.field.setAccessible(true);
        try {
            this.field.set(null, value);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getDisplayName() {
        return I18n.format("clansmod.setting." + this.getSetting().category().getAbsoluteKey() + "." + this.getSetting().key());
    }

    private SettingEntry<T> asButton() {
        final Class<?> clazz = this.getValue().getClass();
        SettingEntry<?> buttonTmp = null ;
        if (clazz.equals(Color.class)) {
            buttonTmp = new ColorPicker(this.getDisplayName(), (SettingReflect<Color>) this);
        } else if (clazz.equals(Boolean.class)) {
            buttonTmp = new TickBox(this.getDisplayName(), (SettingReflect<Boolean>) this);
        } else if (clazz.equals(IterableState.class)) {
            buttonTmp = new Cycler(this.getDisplayName(), (SettingReflect<IterableState>) this);
        }

        return buttonTmp == null ? null : ((SettingEntry<T>) buttonTmp);
    }

}
