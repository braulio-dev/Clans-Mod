package club.mineplex.clans.item.custom;

import club.mineplex.clans.item.CustomItem;
import net.minecraft.item.Item;


public class RareItem extends CustomItem {

	protected long _lastBlock;

	public RareItem(String name, String[] description, Item material) {
		super(material, name, description);
	}

}
