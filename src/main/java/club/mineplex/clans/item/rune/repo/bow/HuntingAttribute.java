package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.util.UtilText;


public class HuntingAttribute extends RuneImpl
{
	private static ValueRange amountGen = new ValueRange(0, 2);	// Value generator for slow amount range
	private static ValueRange durationGen = new ValueRange(1, 4);	// Value generator for slow duration range
	
	private int _slowAmount;		// The slowness level/amplifier
	private double _slowDuration;	// The duration (in ticks) of slow effect
	
	public HuntingAttribute()
	{
		super(RuneType.PREFIX);
		
		_slowAmount = amountGen.rounded();
		_slowDuration = durationGen.decimal();
	}

	@Override
	public String getDisplayName() 
	{
		return "Hunting";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Damaged enemies receive slowness %s for %.2f seconds";
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
