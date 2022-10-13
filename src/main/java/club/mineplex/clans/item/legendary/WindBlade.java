package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;

import static net.minecraft.util.EnumChatFormatting.*;


public class WindBlade extends LegendaryItem implements IChargeable
{

	private double _power;
	private double _burnoutThreshold;

	private int _messageTimer;

	public WindBlade()
	{
		super("Wind Blade", new String[]
		{
			WHITE + "Long ago, a race of cloud dwellers",
			WHITE + "terrorized the skies. A remnant of",
			WHITE + "their tyranny, this airy blade is",
			WHITE + "the last surviving memorium from",
			WHITE + "their final battle against the Titans.",
			" ",
			WHITE + "Deals " + YELLOW + "7 Damage" + WHITE + " with attack",
			YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Flight",
		}, Type.WIND_BLADE);
	}

	@Override
	public double getPower() {
		return _power;
	}

	@Override
	public double getMaxPower() {
		return 80;
	}

	public double getBurnoutThreshold() {
		return _burnoutThreshold;
	}
}