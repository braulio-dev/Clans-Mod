package club.mineplex.clans.gamestate.controller;

import club.mineplex.clans.gamestate.MultiplayerData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.net.InetSocketAddress;

@Data
public abstract class ServerController<K extends MultiplayerData> {

    @Setter(AccessLevel.PROTECTED)
    private boolean isRunning = false;

    protected abstract boolean matchesServer(final InetSocketAddress address);

    protected abstract void onServerJoin(K serverData);

    protected abstract K getNewData(MultiplayerData root);

}
