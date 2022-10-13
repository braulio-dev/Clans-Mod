package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.keybind.ModKeybind;
import club.mineplex.clans.util.UtilClient;
import org.lwjgl.input.Keyboard;

public class KeybindClanHome extends ModKeybind {
    public KeybindClanHome() {
        super("chome.desc", Keyboard.KEY_H);
    }

    @Override
    public void onPress() {
        if (!this.getPlayer().isSneaking() && this.inClans()) {
            this.getPlayer().sendChatMessage("/clan home");
        } else {
            UtilClient.playSound("note.bass", 2F, 0.5F);
        }
    }
}
