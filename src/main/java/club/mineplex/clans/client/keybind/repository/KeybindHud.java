package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.gui.repository.GuiHudManager;
import club.mineplex.clans.client.keybind.ModKeybind;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class KeybindHud extends ModKeybind {

    public KeybindHud() {
        super("hud.desc", Keyboard.KEY_RCONTROL);
    }

    @Override
    public void onPress() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiHudManager());
    }

}
