package club.mineplex.clans.gamestate.mineplex;

import club.mineplex.clans.events.MineplexGameStatusEvent;
import club.mineplex.clans.events.client.CustomItemHoverEvent;
import club.mineplex.clans.gamestate.MultiplayerData;
import lombok.*;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class MineplexData extends MultiplayerData {

    private static final String DEFAULT_MINEPLEX_SERVER = "Unknown Server";
    private static final List<String> mineplexAddresses = Arrays.asList(
            "173.236.67.11",
            "173.236.67.12",
            "173.236.67.13",
            "173.236.67.14",
            "173.236.67.15",
            "173.236.67.16",
            "173.236.67.17",
            "173.236.67.18",
            "173.236.67.19",
            "173.236.67.20",
            "173.236.67.21",
            "173.236.67.22",
            "173.236.67.23",
            "173.236.67.24",
            "173.236.67.25",
            "173.236.67.26",
            "173.236.67.27",
            "173.236.67.28",
            "173.236.67.29",
            "173.236.67.30",
            "173.236.67.31",
            "173.236.67.32",
            "173.236.67.33",
            "173.236.67.34",
            "173.236.67.35",
            "173.236.67.36",
            "173.236.67.37",
            "173.236.67.38",
            "96.45.82.193",
            "96.45.82.3",
            "96.45.83.216",
            "96.45.83.38",
            "173.236.67.101",
            "173.236.67.102",
            "173.236.67.103",
            "107.6.151.174",
            "107.6.151.190",
            "107.6.151.206",
            "107.6.151.210",
            "107.6.151.22",
            "107.6.176.114",
            "107.6.176.122",
            "107.6.176.138",
            "107.6.176.14",
            "107.6.176.166",
            "107.6.176.194"
    );
    @Setter(AccessLevel.NONE)
    private MineplexServerType serverType = MineplexServerType.UNKNOWN;
    @NonNull
    private MineplexGame game = MineplexGame.NONE;
    @NonNull
    private MineplexGame.Status gameStatus = MineplexGame.Status.IN_LOBBY;
    @Setter(AccessLevel.NONE)
    private String server;

    public MineplexData(final InetSocketAddress serverAddress) {
        super(serverAddress);
    }

    public static String getDefaultMineplexServer() {
        return DEFAULT_MINEPLEX_SERVER;
    }

    public static boolean isMineplexNetwork(final InetSocketAddress address) {
        final String domain = address.getHostString().toLowerCase();
        for (final String mineplexAddress : mineplexAddresses) {
            if (mineplexAddress.equals(domain)) {
                return true;
            }
        }

        return domain.endsWith("mineplex.com")
                || domain.endsWith("us.mineplex.com")
                || domain.endsWith("clans.mineplex.com")
                || domain.endsWith("eu.mineplex.com")
                || domain.endsWith("cs.mineplex.com")
                || domain.endsWith("hub.mineplex.com")
                || domain.endsWith("mineplex.club"); // TODO: remove test server
    }

    public void setGame(MineplexGame game) {
        if (game != this.game) {
            setGameStatus(MineplexGame.Status.IN_LOBBY);
        }
        this.game = game;
    }

    public void setGameStatus(MineplexGame.Status gameStatus) {
        if (gameStatus != this.gameStatus) {
            MineplexGameStatusEvent event = new MineplexGameStatusEvent(game, gameStatus);
            MinecraftForge.EVENT_BUS.post(event);
        }
        this.gameStatus = gameStatus;
    }

    public void setServer(@Nullable final String server) {
        MineplexServerType newType = MineplexServerType.UNKNOWN;
        for (final MineplexServerType value : MineplexServerType.values()) {
            if (value.doesServerMatch(server) && !value.equals(MineplexServerType.UNKNOWN)) {
                newType = value;
                break;
            }
        }

        this.serverType = newType;
        this.game = MineplexGame.fromServer(server);
        this.server = Optional.ofNullable(server).orElse(MineplexData.getDefaultMineplexServer());
    }

}
