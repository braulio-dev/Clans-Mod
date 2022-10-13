package club.mineplex.clans.item;

import club.mineplex.clans.item.legendary.*;
import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.rune.repo.armor.*;
import club.mineplex.clans.item.rune.repo.bow.*;
import club.mineplex.clans.item.rune.repo.weapon.*;
import club.mineplex.clans.item.custom.GoldToken;
import club.mineplex.clans.item.custom.RareItem;
import club.mineplex.clans.item.custom.RunedPickaxe;
import club.mineplex.clans.serialization.RuntimeTypeAdapterFactory;
import club.mineplex.clans.util.UtilItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.*;


public class ItemFactory {

    private final Gson gson;

    private final Collection<Class<? extends LegendaryItem>> legendaryItems;
    private final Set<Class<? extends CustomItem>> customItems;
    private final Set<Class<? extends RuneImpl>> weaponRunes;
    private final Set<Class<? extends RuneImpl>> armorRunes;
    private final Set<Class<? extends RuneImpl>> bowRunes;

    public ItemFactory() {
        legendaryItems = new HashSet<>(Arrays.asList(
                MeridianScepter.class,
                AlligatorsTooth.class,
                WindBlade.class,
                GiantsBroadsword.class,
                HyperAxe.class,
                MagneticMaul.class,
                KnightLance.class,
                DemonicScythe.class
        ));

        customItems = new HashSet<>(Arrays.asList(
                RunedPickaxe.class,
                GoldToken.class
        ));

        weaponRunes = new HashSet<>(Arrays.asList(
                FrostedAttribute.class,
                SharpAttribute.class,
                JaggedAttribute.class,
                HasteAttribute.class,
                FlamingAttribute.class,
                ConqueringAttribute.class
        ));

        armorRunes = new HashSet<>(Arrays.asList(
                SlantedAttribute.class,
                ReinforcedAttribute.class,
                ConqueringArmorAttribute.class,
                PaddedAttribute.class,
                LavaAttribute.class
        ));

        bowRunes = new HashSet<>(Arrays.asList(
                HeavyArrowsAttribute.class,
                HuntingAttribute.class,
                InverseAttribute.class,
                LeechingAttribute.class,
                RecursiveAttribute.class,
                ScorchingAttribute.class,
                SlayingAttribute.class
        ));

        gson = createGsonFactory();
    }

    private Gson createGsonFactory() {
        final RuntimeTypeAdapterFactory<RuneImpl> runeFactory = RuntimeTypeAdapterFactory.of(RuneImpl.class);
        final RuntimeTypeAdapterFactory<CustomItem> customItemFactory = RuntimeTypeAdapterFactory.of(CustomItem.class);

        customItemFactory.registerSubtype(CustomItem.class);
        customItemFactory.registerSubtype(LegendaryItem.class);
        customItemFactory.registerSubtype(RareItem.class);
        getCustomItems().forEach(customItemFactory::registerSubtype);
        getRunes().forEach(runeFactory::registerSubtype);

        return new GsonBuilder().registerTypeAdapterFactory(runeFactory)
                                .registerTypeAdapterFactory(customItemFactory)
                                .create();
    }

    public Optional<CustomItem> read(final ItemStack item) {
        if (item == null) {
            return Optional.empty();
        }

        try {
            final Map<String, NBTBase> data = UtilItem.readNBT(item);
            final String json = ((NBTTagString) data.get("gearmanager.json")).getString();
            CustomItem customItem = gson.fromJson(json, CustomItem.class);

            return Optional.ofNullable(customItem);
        } catch (final Exception exception) {
            return Optional.empty();
        }
    }

    void write(CustomItem customItem, ItemStack item) {
        // Reading the searlized CustomItem
        final Map<String, NBTBase> data = UtilItem.readNBT(item);

        // Interpolating the new data
        data.put("gearmanager.json", new NBTTagString(gson.toJson(customItem, CustomItem.class)));
        data.put("gearmanager.uuid", new NBTTagString(customItem._uuid));

        // Saving the serialized CustomItem into the item's NBT data
        UtilItem.writeNBT(item, data);
    }

    public Set<Class<? extends CustomItem>> getCustomItems() {
        final Set<Class<? extends CustomItem>> items = new HashSet<>();
        items.addAll(legendaryItems);
        items.addAll(customItems);
        return items;
    }

    public Set<Class<? extends RuneImpl>> getRunes() {
        final HashSet<Class<? extends RuneImpl>> set = new HashSet<>();
        set.addAll(weaponRunes);
        set.addAll(armorRunes);
        set.addAll(bowRunes);
        return Collections.unmodifiableSet(set);
    }

}
