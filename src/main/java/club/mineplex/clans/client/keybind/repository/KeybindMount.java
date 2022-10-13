package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.keybind.ModKeybind;
import club.mineplex.clans.client.modules.clans.ModuleEnhancedMounts;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.util.UtilClient;
import org.lwjgl.input.Keyboard;

public class KeybindMount extends ModKeybind {

    public KeybindMount() {
        super("mount.desc", Keyboard.KEY_M);
    }

    @Override
    public void onPress() {
        if (!ClansSettings.USE_MOUNT_SPAWN_KEY) {
            return;
        }

        if (!this.getPlayer().isSneaking() && this.inClans()) {
            this.getMod().getModuleThrow(ModuleEnhancedMounts.class).spawnMount();
        } else {
            UtilClient.playSound("note.bass", 2F, 0.5F);
        }
    }
}
