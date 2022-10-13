package club.mineplex.clans.client.keybind;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

public abstract class ModKeybind {
    private static final String CLANS_KEY_START = "clansmod.key.";
    private static final String CLANS_MOD_CATEGORY = "clansmod.key.category";

    private final KeyBinding keyBinding;

    protected ModKeybind(final String identifier, final int keyCode) {
        this(identifier, keyCode, CLANS_MOD_CATEGORY);
    }

    private ModKeybind(final String identifier, final int keyCode, final String category) {
        this(new KeyBinding(CLANS_KEY_START + identifier, keyCode, category));
    }

    private ModKeybind(final KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return this.keyBinding;
    }

    public void onPress() {
    }

    protected boolean inClans() {
        final GameState gameState = this.getMod().getGameState();
        return gameState.isMineplex()
                && gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS);
    }

    protected EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected ClansMod getMod() {
        return ClansMod.getInstance();
    }
}
