package club.mineplex.clans.client.settings.repo;

import club.mineplex.clans.client.settings.IterableSetting;
import club.mineplex.clans.client.settings.IterableState;
import club.mineplex.clans.client.settings.Setting;

import static club.mineplex.clans.client.settings.SettingCategory.CHAMPIONS;

public class ChampionsSettings {

    @Setting(category = CHAMPIONS, key = "skill-recharge-renderer")
    @IterableSetting(states = {
            IterableState.ON,
            IterableState.ICON_ONLY,
            IterableState.OFF
    })
    public static IterableState SKILL_RECHARGE_RENDERER = IterableState.ON;

    @Setting(category = CHAMPIONS, key = "mana-renderer")
    @IterableSetting(states = {
            IterableState.ON,
            IterableState.ICON_ONLY,
            IterableState.OFF
    })
    public static IterableState MANA_RENDERER = IterableState.ON;

    @Setting(category = CHAMPIONS, key = "prevent-preparing-arrow-twice")
    public static boolean PREVENT_PREPARING_ARROW_TWICE = true;

}
