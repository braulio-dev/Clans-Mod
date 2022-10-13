package club.mineplex.clans.gamestate;

import club.mineplex.clans.gamestate.mineplex.MineplexData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;
import java.util.Optional;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode
public class MultiplayerData {

    private static final String DEFAULT_MULTIPLAYER_SERVER = "Unknown Server";
    private final InetSocketAddress serverAddress;

    public static String getDefaultMultiplayerServer() {
        return DEFAULT_MULTIPLAYER_SERVER;
    }

    public final Optional<InetSocketAddress> getServerAddress() {
        return Optional.ofNullable(this.serverAddress);
    }

    public final InetSocketAddress getServerAddressThrow() {
        return this.getServerAddress().orElseThrow(RuntimeException::new);
    }

    public final <T extends MultiplayerData> T as(final Class<T> clazz) {
        return clazz.cast(this);
    }

    public final boolean is(final Class<? extends MineplexData> clazz) {
        return clazz.isInstance(this);
    }

}
