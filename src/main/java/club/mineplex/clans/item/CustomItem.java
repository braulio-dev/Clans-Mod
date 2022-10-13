package club.mineplex.clans.item;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.item.rune.RuneHolder;
import club.mineplex.clans.util.UtilItem;
import ibxm.Player;
import lombok.NonNull;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

/**
 * Implementation of a custom item in Mineplex's server.
 * Currently, only applies to cannons, gold tokens, runes, legendary items, and any other item in clans obtained via unnatural means.
 *
 * Its NBT data stores all information about the item, including its name, original owner, {@link UUID}, description, etc.
 */
public class CustomItem
{

	// All of these fields are parsed from the item in the Mineplex network, so even if they are unused
	// they are still required to identify a custom item in the server
	transient String _displayName;
	transient Player _lastUser = null;
	transient String[] _description;
	transient Item _material;
	String OriginalOwner = null;
	String _uuid;
	boolean _dullEnchantment;
	RuneHolder _attributes;

	private final transient ItemFactory itemFactory = ClansMod.getInstance().getItemManager();

	public CustomItem(@NonNull Item material) {
		this(material, new ItemStack(material).getDisplayName(), null);
	}

	public CustomItem(@NonNull Item material, @NonNull String displayName, String[] description) {
		_displayName = displayName;
		_description = description;
		_material = material;
		_attributes = new RuneHolder();
		_uuid = UUID.randomUUID().toString();
	}

	public String getDisplayName()
	{
		final StringBuilder displayName = new StringBuilder();

		_attributes.getSuperPrefix().ifPresent(superPrefix -> displayName.append(superPrefix.getDisplayName()).append(" "));
		_attributes.getPrefix().ifPresent(prefix -> displayName.append(prefix.getDisplayName()).append(" "));
		displayName.append(_displayName);
		_attributes.getSuffix().ifPresent(suffix -> displayName.append(" of ").append(suffix.getDisplayName()));

		return EnumChatFormatting.RESET + EnumChatFormatting.GOLD.toString() + displayName;
	}

	public String[] getDescription()
	{
		return _description;
	}

	public Item getMaterial() {
		return _material;
	}

	public String getOriginalOwner() {
		return OriginalOwner;
	}

	public String getUniqueId() {
		return _uuid;
	}

	public boolean hasDullEnchantment() {
		return _dullEnchantment;
	}

	public RuneHolder getRuneHolder() {
		return _attributes;
	}

	public void setDisplayName(String displayName) {
		this._displayName = displayName;
	}

	public void setDescription(String[] description) {
		this._description = description;
	}

	public void setOriginalOwner(String originalOwner) {
		OriginalOwner = originalOwner;
	}

	public void setGlow(boolean glow) {
		this._dullEnchantment = glow;
	}

	public Collection<String> getDisplayLore() {
		Collection<String> lore = new ArrayList<>();

		// Showing the item's description
		EnumChatFormatting lineColor = EnumChatFormatting.WHITE;
		Optional.ofNullable(_description).ifPresent(description -> {
			for (String line : description) {
				lore.add(lineColor + line);
			}
		});

		// Including attribute/rune lines
		_attributes.asSet().forEach(attribute -> lore.add(lineColor + "• " + attribute.getDescription()));

		return lore;
	}

	public boolean compare(CustomItem item) {
		return item != null && _uuid.equals(item._uuid);
	}

	public boolean compare(ItemStack item){
		return compare(ClansMod.getInstance().getItemManager().read(item).orElse(null));
	}

	public ItemStack asItemStack() {
		return constructData(new ItemStack(_material, 1));
	}

	public ItemStack constructData(ItemStack item) {
		item.setStackDisplayName(getDisplayName());
		UtilItem.setLore(item, getDisplayLore());
		itemFactory.write(this, item);
		return item;
	}

}