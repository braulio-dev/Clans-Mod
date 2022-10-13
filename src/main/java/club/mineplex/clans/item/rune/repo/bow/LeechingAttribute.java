package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;


public class LeechingAttribute extends RuneImpl
{
	private static ValueRange healGen = new ValueRange(5, 15);
	
	private int _healPercent;
	
	public LeechingAttribute()
	{
		super(RuneType.SUPER_PREFIX);
		
		_healPercent = healGen.rounded();
	}

	@Override
	public String getDisplayName() 
	{
		return "Leeching";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Heal for %d percentage of damage dealt";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(_healPercent, healGen)
		};
	}
}
