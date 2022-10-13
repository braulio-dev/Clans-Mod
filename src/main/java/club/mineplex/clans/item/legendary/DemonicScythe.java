package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;

import static net.minecraft.util.EnumChatFormatting.*;


public class DemonicScythe extends LegendaryItem
{
	private long _interactWait;
	
	public DemonicScythe()
	{
		super("Scythe of the Fallen Lord", new String[]
		{
			WHITE + "An old blade fashioned of nothing more",
			WHITE + "stray bones, brave adventurers have",
			WHITE + "imbued it with the remnant powers of a",
			WHITE + "dark and powerful foe.",
			" ",
			WHITE + "Deals " + YELLOW + "8 Damage" + WHITE + " with attack",
			YELLOW + "Attack" + WHITE + " to use " + GREEN + "Leach Health",
			YELLOW + "Right Click" + WHITE + " to use " + GREEN + "Skull Launcher",
			YELLOW + "Shift-Right Click" + WHITE + " to use " + GREEN + "Gravity Skull Launcher",
		}, Type.SCYTHE_OF_THE_FALLEN_LORD);
	}

}