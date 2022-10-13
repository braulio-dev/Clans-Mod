package club.mineplex.clans.client.settings.repo;

import club.mineplex.clans.client.settings.Setting;

import static club.mineplex.clans.client.settings.SettingCategory.VALUABLE_ITEM_DROP_PREVENTION;

public class DropSettings {

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "gold_token")
    public static boolean GOLD_TOKEN = false;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "runed_pickaxe")
    public static boolean RUNED_PICKAXE = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "windblade")
    public static boolean WINDBLADE = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "broadsword")
    public static boolean BROADSWORD = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "lance")
    public static boolean LANCE = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "scythe")
    public static boolean SCYTHE = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "scepter")
    public static boolean SCEPTER = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "hyper")
    public static boolean HYPER = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "tooth")
    public static boolean TOOTH = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "maul")
    public static boolean MAUL = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "cannon")
    public static boolean CANNON = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "rune")
    public static boolean RUNE = false;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "outpost")
    public static boolean OUTPOST = true;

    @Setting(category = VALUABLE_ITEM_DROP_PREVENTION, key = "tnt")
    public static boolean TNT = true;

}
