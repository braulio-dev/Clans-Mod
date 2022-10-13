package club.mineplex.clans.item.rune.repo.armor;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;


public class ConqueringArmorAttribute extends RuneImpl
{
	private static ValueRange reductionGen = new ValueRange(2.5d, 6.25d);
	private double _reduction;
	
	public ConqueringArmorAttribute()
	{
		super(RuneType.SUFFIX);
		_reduction = reductionGen.decimal() / 100;
	}

	@Override
	public String getDisplayName() 
	{
		return "Conquering";
	}

	@Override
	public String getDescriptionFormat()
	{
		return "%.1f%% damage taken from mobs & bosses";
//		return String.format("%.1f%% damage taken from mobs & bosses", (1 - _reduction) * 100);
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<Double>(_reduction * 100, reductionGen) {
					@Override
					public Double getDisplayValue() {
						return (1 - _reduction) * 100;
					}
				}
		};
	}
}
