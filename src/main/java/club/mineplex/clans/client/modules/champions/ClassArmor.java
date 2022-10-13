package club.mineplex.clans.client.modules.champions;

import club.mineplex.core.mineplex.champions.ChampionsKit;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import static club.mineplex.core.mineplex.champions.ChampionsKit.*;

public enum ClassArmor {

    BRUTE_ARMOR(BRUTE, Items.diamond_helmet, Items.diamond_leggings, Items.diamond_leggings, Items.diamond_boots),
    KNIGHT_ARMOR(KNIGHT, Items.iron_helmet, Items.iron_leggings, Items.iron_leggings, Items.iron_boots),
    ASSASSIN_ARMOR(ASSASSIN, Items.leather_helmet, Items.leather_leggings, Items.leather_leggings, Items.leather_boots),
    RANGER_ARMOR(RANGER, Items.chainmail_helmet, Items.chainmail_leggings, Items.chainmail_leggings, Items.chainmail_boots),
    MAGE_ARMOR(MAGE, Items.golden_helmet, Items.golden_leggings, Items.golden_leggings, Items.golden_boots);

    private final ChampionsKit kit;
    private final ItemArmor helmet;
    private final ItemArmor chestplate;
    private final ItemArmor leggings;
    private final ItemArmor boots;

    ClassArmor(final ChampionsKit kit,
               final ItemArmor helmet,
               final ItemArmor chestplate,
               final ItemArmor leggings,
               final ItemArmor boots) {

        this.kit = kit;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public boolean matches(final ItemStack helmet,
                           final ItemStack chestplate,
                           final ItemStack leggings,
                           final ItemStack boots) {

        try {
            final ItemArmor helmetItem = helmet == null ? null : (ItemArmor) helmet.getItem();
            final ItemArmor chestplateItem = chestplate == null ? null : (ItemArmor) chestplate.getItem();
            final ItemArmor leggingsItem = leggings == null ? null : (ItemArmor) leggings.getItem();
            final ItemArmor bootsItem = boots == null ? null : (ItemArmor) boots.getItem();

            return this.equalsOrBothNull(this.helmet, helmetItem)
                    && this.equalsOrBothNull(this.chestplate, chestplateItem)
                    && this.equalsOrBothNull(this.leggings, leggingsItem)
                    && this.equalsOrBothNull(this.boots, bootsItem);
        } catch (final ClassCastException e) {
            return false;
        }
    }

    private boolean equalsOrBothNull(final ItemArmor one, final ItemArmor two) {
        return (one == null && two == null)
                || (two != null && one != null && one.getArmorMaterial().equals(two.getArmorMaterial()));
    }

    public ChampionsKit getKit() {
        return this.kit;
    }
}