package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.DamageRuneImpl;


public class RecursiveAttribute extends DamageRuneImpl
{
	private static ValueRange attackGen = new ValueRange(2, 6);
	
	public RecursiveAttribute()
	{
		super(RuneType.PREFIX, attackGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Recursive";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Increase damage by %.2f half-hearts";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getBonusDamage(), attackGen)
		};
	}
}
