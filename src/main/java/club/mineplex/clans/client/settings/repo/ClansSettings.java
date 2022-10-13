package club.mineplex.clans.client.settings.repo;

import club.mineplex.clans.client.settings.IterableSetting;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.Setting;

import static club.mineplex.clans.client.settings.SettingCategory.CLANS;
import static club.mineplex.clans.client.settings.SettingCategory.CLANS_QOL;

public class ClansSettings {

    @Setting(category = CLANS, key = "lock-inventory-slots")
    public static boolean LOCK_INVENTORY_SLOTS = true;

    @Setting(category = CLANS, key = "use-mount-spawn-key")
    public static boolean USE_MOUNT_SPAWN_KEY = true;

    @Setting(category = CLANS_QOL, key = "map-fix", locked = true)
    public static boolean FIX_CLANS_MAP = true;

    @Setting(category = CLANS_QOL, key = "fake-item-warning")
    public static boolean FAKE_ITEM_WARNING = true;

    @Setting(category = CLANS_QOL, key = "enhanced-death-messages")
    public static boolean ENHANCED_DEATH_MESSAGES = true;

    @Setting(category = CLANS_QOL, key = "show-reinforced-total")
    public static boolean SHOW_REINFORCED_TOTAL = true;

    @Setting(category = CLANS_QOL, key = "enhanced-runed-items-display")
    public static boolean ENHANCED_RUNED_ITEMS_DISPLAY = true;

    @Setting(category = CLANS, key = "load-pack")
    @IterableSetting(states = {
            IterableState.ON,
            IterableState.IN_CLANSHUB,
            IterableState.OFF
    })
    public static IterableState LOAD_CLANS_RESOURCEPACK = IterableState.IN_CLANSHUB;

}
