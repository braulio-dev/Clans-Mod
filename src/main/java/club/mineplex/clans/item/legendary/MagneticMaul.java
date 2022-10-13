package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;
import club.mineplex.clans.util.LineFormat;
import club.mineplex.clans.util.UtilText;

import static net.minecraft.util.EnumChatFormatting.*;


public class MagneticMaul extends LegendaryItem implements IChargeable
{
	
	private double _power;
	private double _heat;
	
	public MagneticMaul()
	{
		super("Magnetic Maul", UtilText.splitLinesToArray(new String[]
		{
			WHITE + "For centuries, warlords used this hammer to control their subjects. This brutal weapon allows you to pull your enemies towards you with magnetic force!",
			" ",
			"#" + WHITE + "Deals " + YELLOW + "8 Damage" + WHITE + " with attack",
			"#" + YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Magnetism"
		}, LineFormat.LORE), Type.MAGNETIC_MAUL);
	}

	@Override
	public double getPower() {
		return _power;
	}

	@Override
	public double getMaxPower() {
		return 80;
	}

	public double getHeat() {
		return _heat;
	}

}