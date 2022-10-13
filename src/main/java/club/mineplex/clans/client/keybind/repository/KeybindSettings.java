package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.gui.repository.modmenu.GuiModMenu;
import club.mineplex.clans.client.keybind.ModKeybind;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeybindSettings extends ModKeybind {

    @SideOnly(Side.CLIENT)
    public KeybindSettings() {
        super("settings.desc", Keyboard.KEY_RSHIFT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPress() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiModMenu());
    }

}
