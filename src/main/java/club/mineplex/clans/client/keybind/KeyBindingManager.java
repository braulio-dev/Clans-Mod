package club.mineplex.clans.client.keybind;

import club.mineplex.clans.client.keybind.repository.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.*;

public class KeyBindingManager {

    private static KeyBindingManager instance;
    private final Map<Class<? extends ModKeybind>, ModKeybind> keyBinds = new HashMap<>();

    private KeyBindingManager() {
        this.registerKeyBindings(
                new KeybindSettings(),
                new KeybindSlotLock(),
                new KeybindClanHome(),
                new KeybindClanStuck(),
                new KeybindMount(),
                new KeybindRenderDistance(),
                new KeybindHud()
        );

        MinecraftForge.EVENT_BUS.register(
                new ListenerKeybind(this)
        );
    }

    public static KeyBindingManager getInstance() {
        if (instance == null) {
            instance = new KeyBindingManager();
        }
        return instance;
    }

    private void registerKeyBindings(final ModKeybind... modKeybinds) {
        for (final ModKeybind modKeybind : modKeybinds) {
            this.keyBinds.put(modKeybind.getClass(), modKeybind);
        }
    }

    public void setup() {
        for (final ModKeybind key : this.getKeyBinds()) {
            ClientRegistry.registerKeyBinding(key.getKeyBinding());
        }
    }

    List<ModKeybind> getKeyBinds() {
        return new ArrayList<>(this.keyBinds.values());
    }

    private Optional<ModKeybind> getKeyBind(final Class<? extends ModKeybind> keybindingClass) {
        return Optional.ofNullable(this.keyBinds.get(keybindingClass));
    }

    public ModKeybind getKeyBindThrow(final Class<? extends ModKeybind> keybindingClass) {
        return this.getKeyBind(keybindingClass)
                   .orElseThrow(() -> new RuntimeException("Keybinding not found: " + keybindingClass.getName()));
    }
}
