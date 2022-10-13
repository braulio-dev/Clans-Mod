package club.mineplex.clans.client.keybind.repository;

import club.mineplex.clans.client.keybind.ModKeybind;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeybindSlotLock extends ModKeybind {

    @SideOnly(Side.CLIENT)
    public KeybindSlotLock() {
        super("slotlock.desc", Keyboard.KEY_L);
    }

}
