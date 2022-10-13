package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.events.MineplexServerSwitchEvent;
import club.mineplex.clans.events.client.PacketEvent;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.gamestate.mineplex.MineplexServerType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleResourcepack extends AbstractMod {

    private static final String RESOURCEPACK = "http://198.20.72.74/ResClans.zip";
    private static final String RESOURCEPACK_19 = "http://198.20.72.74/ResClans19.zip";

    private boolean isManuallyLoading = false;

    public ModuleResourcepack() {
        super("Resourcepack Loader");
    }

    @SubscribeEvent
    public void denyPack(final PacketEvent.Send.Pre event) {
        if (!(event.getPacket() instanceof C19PacketResourcePackStatus)) {
            return;
        }

        if (!this.gameState.isMineplex()) {
            return;
        }

        if (!this.gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS)) {
            return;
        }

        final IterableState state = ClansSettings.LOAD_CLANS_RESOURCEPACK;
        if (!(state.equals(IterableState.OFF) || state.equals(IterableState.IN_CLANSHUB))) {
            return;
        }

        final C19PacketResourcePackStatus packetResourcePackStatus = (C19PacketResourcePackStatus) event.getPacket();
        packetResourcePackStatus.status = C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
    }

    @SubscribeEvent
    public void onSwitch(final MineplexServerSwitchEvent event) {
        if (!event.getTo().getServerType().equals(MineplexServerType.CLANSHUB)
                || !ClansSettings.LOAD_CLANS_RESOURCEPACK.equals(IterableState.IN_CLANSHUB)) {
            return;
        }

        // Not providing hash since Mineplex already doesn't provide it themselves, therefore
        // Minecraft names its server resourcepack the default 'legacy' string
        this.isManuallyLoading = true;
        Minecraft.getMinecraft().getResourcePackRepository().downloadResourcePack(RESOURCEPACK, "");
        this.isManuallyLoading = false;
    }

    public boolean shouldCancelResourcepack(final String resourceUrl) {
        return !this.isManuallyLoading
                && (resourceUrl.endsWith(RESOURCEPACK.substring(RESOURCEPACK.lastIndexOf("/"))))
                && !ClansSettings.LOAD_CLANS_RESOURCEPACK.equals(IterableState.ON);
    }
}
