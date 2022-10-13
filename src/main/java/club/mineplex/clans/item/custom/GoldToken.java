package club.mineplex.clans.item.custom;

import club.mineplex.clans.item.CustomItem;
import net.minecraft.init.Items;


public class GoldToken extends CustomItem
{
	private int _goldValue;
	
	public GoldToken(int goldValue)
	{
		super(Items.rabbit_foot, "Gold Token", new String[] {
				String.format("A gold token worth %s gold coins.", goldValue)
		});
		
		_goldValue = goldValue;
	}
	
	public int getGold()
	{
		return _goldValue;
	}

}