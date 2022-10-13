package club.mineplex.clans.client.settings.repo;

import club.mineplex.clans.client.settings.IterableSetting;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.Setting;

import static club.mineplex.clans.client.settings.SettingCategory.DISCORD;
import static club.mineplex.clans.client.settings.SettingCategory.GENERAL;

public class ClientSettings {

    // Discord
    @Setting(category = DISCORD, key = "rich-status")
    public static boolean DISCORD_RICH_STATUS = true;

    @Setting(category = DISCORD, key = "display-server")
    public static boolean DISCORD_SHOW_SERVER = true;

    @Setting(category = DISCORD, key = "display-mineplex-server")
    @IterableSetting(states = {
            IterableState.ON,
            IterableState.TYPE_ONLY,
            IterableState.OFF
    })
    public static IterableState DISCORD_SHOW_MINEPLEX_SERVER = IterableState.ON;

    // General
    @Setting(category = GENERAL, key = "use-render-distance-key")
    public static boolean CYCLE_RENDER_DISTANCE_KEY = true;


}
