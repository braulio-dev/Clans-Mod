package club.mineplex.clans.gamestate;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import lombok.*;

@Data
public class GameState {

    private static GameState gameStateInstance = null;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final ClansMod mod;
    @NonNull
    private MultiplayerData multiplayerData = new MultiplayerData(null);

    private GameState(final ClansMod mod) {
        this.mod = mod;
    }

    public static GameState getInstance(final ClansMod client) {
        if (gameStateInstance != null) {
            throw new UnsupportedOperationException("A GameState instance has already been initialized!");
        }

        gameStateInstance = new GameState(client);
        return gameStateInstance;
    }

    public boolean isMineplex() {
        return this.multiplayerData.is(MineplexData.class);
    }

    public boolean isMultiplayer() {
        return this.multiplayerData.getServerAddress().isPresent();
    }

}
