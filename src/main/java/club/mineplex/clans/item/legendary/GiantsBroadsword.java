package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;

import static net.minecraft.util.EnumChatFormatting.*;


public class GiantsBroadsword extends LegendaryItem
{

	public GiantsBroadsword()
	{
		super("Giants Broadsword", new String[]
		{
			WHITE + "Forged in the godly mines of Plagieus,",
			WHITE + "this sword has endured thousands of",
			WHITE + "wars. It is sure to grant glorious",
			WHITE + "victory in battle.",
			WHITE + " ",
			WHITE + "Deals " + YELLOW + "10 Damage" + WHITE + " with attack",
			YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Shield",
		}, Type.GIANTS_BROADSWORD);
	}
	
}