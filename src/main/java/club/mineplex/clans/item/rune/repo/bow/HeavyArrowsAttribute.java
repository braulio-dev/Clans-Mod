package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;


public class HeavyArrowsAttribute extends RuneImpl
{
	private static ValueRange knockbackGen = new ValueRange(25, 75);
	
	private double _knockbackPercent;
	
	public HeavyArrowsAttribute()
	{
		super(RuneType.PREFIX);
		
		_knockbackPercent = knockbackGen.decimal();
	}

	@Override
	public String getDisplayName() 
	{
		return "Heavy";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Increase knockback by %.2f%%";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(_knockbackPercent, knockbackGen)
		};
	}
}
