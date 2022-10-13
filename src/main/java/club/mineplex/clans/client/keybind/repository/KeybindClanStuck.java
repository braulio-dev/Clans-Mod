package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.keybind.ModKeybind;
import club.mineplex.clans.util.UtilClient;
import org.lwjgl.input.Keyboard;

public class KeybindClanStuck extends ModKeybind {

    public KeybindClanStuck() {
        super("cstuck.desc", Keyboard.KEY_J);
    }

    @Override
    public void onPress() {
        if (!this.getPlayer().isSneaking() && this.inClans()) {
            this.getPlayer().sendChatMessage("/clan stuck");
        } else {
            UtilClient.playSound("note.bass", 2F, 0.5F);
        }
    }
}
