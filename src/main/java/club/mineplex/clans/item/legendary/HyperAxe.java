package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.legendary.LegendaryItem;
import club.mineplex.clans.item.value.ValueRange;

import static net.minecraft.util.EnumChatFormatting.*;


public class HyperAxe extends LegendaryItem
{
	private static final ValueRange AMOUNT_GEN = new ValueRange(0, 3);		// [1, 4] speed amount
	private static final ValueRange DURATION_GEN = new ValueRange(80, 320);	// [4, 16] seconds speed duration

	private final int _speedAmount;
	private final int _speedDuration;
	
	private long _lastAttack;
	
	public HyperAxe()
	{
		super("Hyper Axe", new String[]
		{
			WHITE + "Of all the weapons known to man,",
			WHITE + "none matches the savagery of the",
			WHITE + "Hyper Axe. Infused with a rabbit's",
			WHITE + "speed and a pigman's ferocity, this",
			WHITE + "blade can rip through any opponent.",
			WHITE + " ",
			WHITE + "Hit delay is reduced by " + YELLOW + "50%",
			WHITE + "Deals " + YELLOW + "6 Damage" + WHITE + " with attack",
			YELLOW + "Right-Click" + WHITE + " to use " + GREEN + "Dash",
		}, Type.HYPER_AXE);
		
		_speedAmount = AMOUNT_GEN.rounded();
		_speedDuration = DURATION_GEN.rounded();
		_lastAttack = 0;
	}
	
	private long timeSinceLastAttack()
	{
		return System.currentTimeMillis() - _lastAttack;
	}

	public int getSpeedAmount() {
		return _speedAmount;
	}

	public int getSpeedDuration() {
		return _speedDuration;
	}
}