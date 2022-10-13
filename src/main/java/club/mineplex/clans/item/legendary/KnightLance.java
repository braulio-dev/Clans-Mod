package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;

import static net.minecraft.util.EnumChatFormatting.*;


public class KnightLance extends LegendaryItem
{

	private boolean _charging;
	private double _buildup;
	
	public KnightLance()
	{
		super("Knight's Greatlance", new String[]
		{
			WHITE + "Relic of a bygone age.",
			WHITE + "Emblazoned with cryptic runes, this",
			WHITE + "Lance bears the marks of its ancient master.",
			WHITE + "You feel him with you always:",
			WHITE + "Heed his warnings and stave off the darkness.",
			" ",
			WHITE + "Deals " + YELLOW + "8 Damage" + WHITE + " with attack",
			YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Charge",
		}, Type.KNIGHTS_GREATLANCE);
	}
	
}