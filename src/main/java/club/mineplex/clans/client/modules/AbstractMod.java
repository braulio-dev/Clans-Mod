package club.mineplex.clans.client.modules;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.util.UtilClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;

public abstract class AbstractMod {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final ClansMod mod = ClansMod.getInstance();
    protected final GameState gameState = mod.getGameState();
    private final boolean isBlocked;
    private final String name;

    protected AbstractMod(final String name) {
        this.name = name;
        this.isBlocked = !UtilClient.isModFeatureAllowed(this.name.replace(" ", "-").trim().toLowerCase());
    }

    public void init() {

    }

    public final String getName() {
        return this.name;
    }

    protected boolean isModuleUsable() {
        return true;
    }

    public final boolean isEnabled() {
        return this.isModuleUsable() && !this.isModuleBlocked();
    }

    public final boolean isModuleBlocked() {
        return this.isBlocked;
    }

    protected final Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    protected final EntityPlayerSP getPlayer() {
        return this.getMinecraft().thePlayer;
    }

    protected final PlayerControllerMP getPlayerController() {
        return this.getMinecraft().playerController;
    }
}
