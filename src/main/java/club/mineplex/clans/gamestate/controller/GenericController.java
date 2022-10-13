package club.mineplex.clans.gamestate.controller;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.MultiplayerData;

import java.net.InetSocketAddress;

public class GenericController extends ServerController<MultiplayerData> {

    @Override
    protected boolean matchesServer(final InetSocketAddress address) {
        return ClansMod.getInstance().getServerControllers()
                       .stream()
                       .filter(serverController -> !(serverController instanceof GenericController))
                       .noneMatch(controller -> controller.matchesServer(address));
    }

    @Override
    protected void onServerJoin(final MultiplayerData serverData) {
        /*
        Generic servers are currently not supported
         */
    }

    @Override
    protected MultiplayerData getNewData(final MultiplayerData root) {
        return root;
    }

}
