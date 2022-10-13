package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.repo.DropSettings;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.item.CustomItem;
import club.mineplex.clans.item.legendary.LegendaryItem;
import club.mineplex.clans.item.custom.GoldToken;
import club.mineplex.clans.item.custom.RunedPickaxe;
import club.mineplex.clans.util.UtilClient;
import club.mineplex.clans.util.UtilItem;
import club.mineplex.clans.util.UtilText;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ModuleDropPrevention extends AbstractMod {

    private static Map<Item, Supplier<Boolean>> itemMap = new HashMap<>();

    static {
        itemMap.put(Item.getByNameOrId("beacon"), () -> DropSettings.OUTPOST);
        itemMap.put(Item.getByNameOrId("tnt"), () -> DropSettings.TNT);
        itemMap.put(Item.getByNameOrId("sponge"), () -> DropSettings.CANNON);
    }

    public ModuleDropPrevention() {
        super("Drop Prevention");
    }

    public boolean handleDrop(final ItemStack itemStack) {
        if (itemStack == null) {
            return true;
        }

        if (isPreventionEnabled(itemStack)) {
            final String name = itemStack.hasDisplayName()
                    ? itemStack.getDisplayName()
                    : itemStack.getItem().getItemStackDisplayName(itemStack);

            UtilClient.playSound("note.bass", 2F, 0.5F);
            UtilText.sendPlayerMessageWithPrefix(
                    this.getName(), String.format("You are not allowed to drop &e%s&r.", name)
            );

            return false;
        }

        return true;
    }

    @Override
    public boolean isModuleUsable() {
        return this.gameState.isMineplex()
                && this.gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS);
    }

    private boolean isPreventionEnabled(@Nonnull ItemStack stack) {
        Optional<CustomItem> item = mod.getItemManager().read(stack);
        if (item.isPresent()) {
            final CustomItem customItem = item.get();
            if (customItem instanceof LegendaryItem) {
                switch (((LegendaryItem) customItem).getType()) {
                    case MAGNETIC_MAUL:
                        return DropSettings.MAUL;
                    case ALLIGATORS_TOOTH:
                        return DropSettings.TOOTH;
                    case SCYTHE_OF_THE_FALLEN_LORD:
                        return DropSettings.SCYTHE;
                    case GIANTS_BROADSWORD:
                        return DropSettings.BROADSWORD;
                    case HYPER_AXE:
                        return DropSettings.HYPER;
                    case KNIGHTS_GREATLANCE:
                        return DropSettings.LANCE;
                    case MERIDIAN_SCEPTER:
                        return DropSettings.SCEPTER;
                    case WIND_BLADE:
                        return DropSettings.WINDBLADE;
                }
            } else if (customItem instanceof RunedPickaxe) {
                return DropSettings.RUNED_PICKAXE;
            } else if (customItem instanceof GoldToken) {
                return DropSettings.GOLD_TOKEN;
            }

        }

        // Checking if the item is a rune
        final String RUNE_NAME = EnumChatFormatting.GOLD + "Ancient Rune";
        if (stack.hasDisplayName() && stack.getDisplayName().equals(RUNE_NAME)
                && UtilItem.hasLore(stack) && !UtilItem.getLoreThrow(stack).isEmpty()) {
            return DropSettings.RUNE;
        }

        return itemMap.getOrDefault(stack.getItem(), () -> false).get();
    }

}
