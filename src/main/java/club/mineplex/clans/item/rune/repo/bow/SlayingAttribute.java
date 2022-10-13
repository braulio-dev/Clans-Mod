package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.DamageRuneImpl;


public class SlayingAttribute extends DamageRuneImpl
{
	private static ValueRange attackGen = new ValueRange(2, 12);
	
	public SlayingAttribute()
	{
		super(RuneType.SUFFIX, attackGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Slaying";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Increase damage by %.2f half-hearts against mobs & bosses";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getBonusDamage(), attackGen)
		};
	}
}
