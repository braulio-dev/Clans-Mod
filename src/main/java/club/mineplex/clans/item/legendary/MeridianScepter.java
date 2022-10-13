package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;
import club.mineplex.clans.util.LineFormat;
import club.mineplex.clans.util.UtilText;

import static net.minecraft.util.EnumChatFormatting.*;


public class MeridianScepter extends LegendaryItem
{

	private long _interactWait;
	
	public MeridianScepter()
	{
		super("Meridian Scepter", UtilText.splitLinesToArray(new String[]
		{
			WHITE + "Legend says that this scepter was retrieved from the deepest trench in all of Minecraftia. It is said that he who wields this scepter holds the power of Poseidon himself.",
			" ",
			"#" + YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Meridian Beam"
		}, LineFormat.LORE), Type.MERIDIAN_SCEPTER);
	}

	public long getInteractWait() {
		return _interactWait;
	}
}