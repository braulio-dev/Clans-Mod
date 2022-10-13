package club.mineplex.clans.gamestate.controller;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.GameState;
import club.mineplex.clans.gamestate.MultiplayerData;
import io.netty.channel.local.LocalAddress;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.net.InetSocketAddress;

public class ListenerServerConnection {

    private final GameState gameState = ClansMod.getInstance().getGameState();

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.gameState.setMultiplayerData(new MultiplayerData(null));

        for (final ServerController<?> controller : ClansMod.getInstance().getServerControllers()) {
            controller.setRunning(false);
        }
    }

    @SubscribeEvent
    public void onServer(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (event.manager.getRemoteAddress() instanceof LocalAddress) {
            return;
        }

        InetSocketAddress address = (InetSocketAddress) event.manager.getRemoteAddress();
        if (address.getHostString().endsWith(".")) {
            final String hostname = address.getHostString().substring(0, address.getHostString().length() - 1);
            final int port = address.getPort();
            address = new InetSocketAddress(hostname, port);
        }
        MultiplayerData multiplayerData = new MultiplayerData(address);

        for (final ServerController controller : ClansMod.getInstance().getServerControllers()) {
            if (controller.matchesServer(address)) {
                controller.setRunning(true);
                multiplayerData = controller.getNewData(multiplayerData);
                controller.onServerJoin(multiplayerData);
            }
        }

        this.gameState.setMultiplayerData(multiplayerData);
    }


}
