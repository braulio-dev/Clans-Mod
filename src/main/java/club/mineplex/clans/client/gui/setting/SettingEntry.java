package club.mineplex.clans.client.gui.setting;

import club.mineplex.clans.client.CustomFontRenderer;
import club.mineplex.clans.client.gui.repository.modmenu.ModBoundingBox;
import club.mineplex.clans.client.settings.SettingReflect;
import lombok.Data;
import net.minecraft.client.Minecraft;

@Data
public abstract class SettingEntry<T> implements ModBoundingBox.BoundingBoxElement {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final String displayName;
    private final SettingReflect<T> setting;

    protected SettingEntry(final String displayName, final SettingReflect<T> setting) {
        this.displayName = displayName;
        this.setting = setting;
    }

    @Override
    public float getHeight(CustomFontRenderer renderer) {
        return renderer.getHeight(this.getSetting().getDisplayName());
    }

}
