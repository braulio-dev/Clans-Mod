package club.mineplex.clans.item.rune.repo.armor;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.PartialReductionRuneImpl;


public class LavaAttribute extends PartialReductionRuneImpl
{	
	private static ValueRange reductionGen = new ValueRange(0.2d, 1.0d);
	
	public LavaAttribute()
	{
		super(RuneType.SUPER_PREFIX, reductionGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Lava Forged";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Reduces damage from fire and lava by %.1f%%";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<Double>(getReductionPercent(), reductionGen) {
					@Override
					public Double getDisplayValue() {
						return getReductionPercent() * 100d;
					}
				}
		};
	}

}