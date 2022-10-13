package club.mineplex.clans.gamestate.mineplex;

import java.util.Arrays;

/**
 * @author Rey
 * @created 10/09/2022
 * @project Clans Mod
 */
public enum MineplexGame {

    MASTER_BUILDERS("Master Builders", "BLD"),
    DRAW_MY_THING("Draw My Thing", "DMT"),
    SUPER_PAINTBALL("Super Paintball", "PB"),
    NANO_GAMES("Nano Games", "NANO"),
    MIXED_ARCADE("Mixed Arcade", "MIN"),
    MICRO_BATTLES("Micro Battle", "MB"),
    TURF_WARS("Turf Wars", "TF"),
    DRAGON_ESCAPE("Dragon Escape", "DE"),
    SPEED_BUILDERS("Speed Builders", "SB"),
    BLOCK_HUNT("Block Hunt", "BH"),
    CAKEWARS_STANDARD("Cake Wars Standard", "CW4"),
    CAKEWARS_DUOS("Cake Wars Duos", "CW2"),
    CAKEWARS_SOLO("Cake Wars Solo", "CW1"),
    SURVIVAL_GAMES_TEAMS("Survival Games Teams", "SG2"),
    SURVIVAL_GAMES_SOLO("Survival Games Solo", "SG"),
    SKYWARS_TEAMS("Skywars Games Solo", "SKY2"),
    SKYWARS_SOLO("Skywars Games Solo", "SKY"),
    UHC("UHC", "UHC"),
    BRIDGES("The Bridges", "BR"),
    MINESTRIKE("Mine-Strike", "MS"),
    SUPER_SMASH_MOBS_TEAMS("SSM Teams", "SSM2"),
    SUPER_SMASH_MOBS_SOLO("SSM", "SSM"),
    CHAMPIONS_DOMINATE("Champions Domination", "DOM"),
    CHAMPIONS_CTF("Champions CTF", "CTF"),
    CLANS("Clans", "Clans"),
    DEATH_RUN("Death Run", "DRN"),
    NONE("Unknown Game", null);

    private final String shortName;
    private final String name;

    MineplexGame(final String name, final String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public static MineplexGame fromServer(final CharSequence server) {
        return Arrays.stream(MineplexGame.values())
                     .filter(game -> game.getShortName() != null)
                     .filter(game -> server.toString().matches(game.getShortName() + "-\\d+"))
                     .findFirst()
                     .orElse(MineplexGame.NONE);
    }

    public String getName() {
        return this.name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public enum Status {
        IN_LOBBY,
        STARTING,
        IN_GAME
    }

}
