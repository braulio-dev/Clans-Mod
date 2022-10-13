package club.mineplex.clans.gamestate.controller;

import club.mineplex.clans.gamestate.MultiplayerData;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.net.InetSocketAddress;

public class MineplexController extends ServerController<MineplexData> {

    @Override
    protected boolean matchesServer(final InetSocketAddress address) {
        return MineplexData.isMineplexNetwork(address);
    }

    @Override
    protected void onServerJoin(final MineplexData serverData) {
        /*
        TODO: implement Mineplex's controller cache
         */
    }

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!isRunning()) {
            return; // We return if the server we are on is not Mineplex
        }

    }

    @Override
    protected MineplexData getNewData(final MultiplayerData root) {
        return new MineplexData(root.getServerAddress().orElse(null));
    }

}
