package club.mineplex.clans.item.rune.repo.armor;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.ReductionRuneImpl;


public class PaddedAttribute extends ReductionRuneImpl
{	
	private static ValueRange reductionGen = new ValueRange(1.0d, 4.0d);
	
	public PaddedAttribute()
	{
		super(RuneType.PREFIX, reductionGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Padded";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "-%.1f damage taken from falls";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getFlatReduction(), reductionGen)
		};
	}
}
