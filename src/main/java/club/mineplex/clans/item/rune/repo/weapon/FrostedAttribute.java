package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.util.UtilText;


public class FrostedAttribute extends RuneImpl
{
	private static ValueRange amountGen = new ValueRange(0, 3);	// Value generator for slow amount range
	private static ValueRange durationGen = new ValueRange(20, 60);	// Value generator for slow duration range
	
	private int _slowAmount;		// The slowness level/amplifier
	private int _slowDuration;		// The duration (in ticks) of slow effect
	
	/**
	 * Class constructor
	 */
	public FrostedAttribute() {
		super(RuneType.SUPER_PREFIX);
		
		_slowAmount = amountGen.rounded();
		_slowDuration = durationGen.rounded();
	}
	
	@Override
	public String getDisplayName()
	{
		return "Frosted";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Apply slowness %s for %d ticks to enemies";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<Integer>(_slowAmount, amountGen) {
					@Override
					protected String displayOverride() {
						return UtilText.toRoman(_slowAmount + 1);
					}
				},
				new ValueRangeDisplay<>(_slowDuration, durationGen)
		};
	}
}
