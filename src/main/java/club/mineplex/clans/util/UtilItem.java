package club.mineplex.clans.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import javax.annotation.Nonnull;
import java.util.*;

public class UtilItem
{

    private static final EnumMap<ItemType, Collection<Item>> items = new EnumMap<ItemType, Collection<Item>>(ItemType.class);

    enum ItemType {
        ARMOR,
        SWORD,
        AXE;

    }
    static {
        Arrays.stream(ItemType.values()).forEach(itemType -> items.put(itemType, new ArrayList<>()));

        // Swords
        registerItem("diamond_sword", ItemType.SWORD);
        registerItem("golden_sword", ItemType.SWORD);
        registerItem("stone_sword", ItemType.SWORD);
        registerItem("wooden_sword", ItemType.SWORD);
        registerItem("iron_sword", ItemType.SWORD);

        // Axes
        registerItem("diamond_axe", ItemType.AXE);
        registerItem("golden_axe", ItemType.AXE);
        registerItem("stone_axe", ItemType.AXE);
        registerItem("wooden_axe", ItemType.AXE);
        registerItem("iron_axe", ItemType.AXE);

        // Armor
        registerItem("diamond_helmet", ItemType.ARMOR);
        registerItem("diamond_chestplate", ItemType.ARMOR);
        registerItem("diamond_boots", ItemType.ARMOR);
        registerItem("diamond_leggings", ItemType.ARMOR);
        registerItem("golden_helmet", ItemType.ARMOR);
        registerItem("golden_chestplate", ItemType.ARMOR);
        registerItem("golden_leggings", ItemType.ARMOR);
        registerItem("golden_boots", ItemType.ARMOR);
        registerItem("iron_helmet", ItemType.ARMOR);
        registerItem("iron_chestplate", ItemType.ARMOR);
        registerItem("iron_leggings", ItemType.ARMOR);
        registerItem("iron_boots", ItemType.ARMOR);
        registerItem("chainmail_helmet", ItemType.ARMOR);
        registerItem("chainmail_chestplate", ItemType.ARMOR);
        registerItem("chainmail_leggings", ItemType.ARMOR);
        registerItem("chainmail_boots", ItemType.ARMOR);
        registerItem("leather_helmet", ItemType.ARMOR);
        registerItem("leather_chestplate", ItemType.ARMOR);
        registerItem("leather_leggings", ItemType.ARMOR);
        registerItem("leather_boots", ItemType.ARMOR);
    }

    private static void registerItem(String id, ItemType type, ItemType... additionalTypes) {
        final Item item = Item.getByNameOrId(id);
        items.get(type).add(item);
        if (additionalTypes != null && additionalTypes.length > 0) {
            Arrays.stream(additionalTypes).forEach(itemType -> items.get(itemType).add(item));
        }
    }

    public static void writeNBT(ItemStack item, Map<String, NBTBase> data) {
        Optional.ofNullable(item.getTagCompound()).ifPresent(compound -> {
            for (final String name : data.keySet()) {
                compound.setTag(name, data.get(name));
            }
        });
    }

    public static Map<String, NBTBase> readNBT(final ItemStack itemStack) {
        if (itemStack == null) {
            return Collections.emptyMap();
        }

        final NBTTagCompound compound = itemStack.getTagCompound();

        if (compound == null) {
            return Collections.emptyMap();
        }

        final Map<String, NBTBase> unhandled = new HashMap<>();
        for (final String name : compound.getKeySet())
        {
            unhandled.put(name, compound.getTag(name));
        }
        return unhandled;
    }

    public static boolean isSword(@Nonnull Item material)
    {
        return items.get(ItemType.SWORD).contains(material);
    }

    public static boolean isAxe(@Nonnull Item material)
    {
        return items.get(ItemType.AXE).contains(material);
    }

    public static boolean isArmor(@Nonnull Item material)
    {
        return items.get(ItemType.ARMOR).contains(material);
    }

    public static void setLore(ItemStack itemStack, Collection<String> lore) {
        final NBTTagList list = new NBTTagList();
        lore.forEach(line -> list.appendTag(new NBTTagString(line)));

        final NBTTagCompound compound = itemStack.getTagCompound();
        final NBTTagCompound display = compound.hasKey("display") ? compound.getCompoundTag("display") : new NBTTagCompound();
        display.setTag("Lore", list);
        compound.setTag("display", display);
    }

    public static boolean hasLore(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("display")
        && itemStack.getTagCompound().getCompoundTag("display").hasKey("Lore", 9);
    }

    public static Optional<List<String>> getLore(ItemStack itemStack) {
        if (!hasLore(itemStack)) {
            return Optional.empty();
        }

        ArrayList<String> list = new ArrayList<String>();
        NBTTagCompound nbttagcompound = itemStack.getTagCompound().getCompoundTag("display");
        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Lore", 8);

        if (nbttaglist1.tagCount() > 0)
        {
            for (int j1 = 0; j1 < nbttaglist1.tagCount(); ++j1)
            {
                list.add(nbttaglist1.getStringTagAt(j1));
            }
        }
        return Optional.of(list);
    }

    public static List<String> getLoreThrow(ItemStack itemStack) {
        return getLore(itemStack).orElseThrow(() -> new IllegalStateException("ItemStack has no lore"));
    }

}
