package club.mineplex.clans.item.custom;

import club.mineplex.clans.util.LineFormat;
import club.mineplex.clans.util.UtilText;
import net.minecraft.init.Items;

import static net.minecraft.util.EnumChatFormatting.WHITE;
import static net.minecraft.util.EnumChatFormatting.YELLOW;


public class RunedPickaxe extends RareItem
{
	private long _instamineEnabled;
	private boolean _enabled;
	
	public RunedPickaxe() {
		super("Runed Pickaxe", UtilText.splitLinesToArray(new String[]
				{
					"What an interesting design this pickaxe seems to have!",
					YELLOW + "Right-Click" + WHITE + " to use " + YELLOW + "Instant mine" + WHITE + "."
				}, LineFormat.LORE), Items.record_mellohi);
	}

}