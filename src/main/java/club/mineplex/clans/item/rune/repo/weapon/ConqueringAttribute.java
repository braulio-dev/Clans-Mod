package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.DamageRuneImpl;


public class ConqueringAttribute extends DamageRuneImpl
{
	private static ValueRange damageGen = new ValueRange(1.0d, 4.0d);
	
	public ConqueringAttribute()
	{
		super(RuneType.SUFFIX, damageGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Conquering";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "%.2f bonus damage against mobs & bosses";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getBonusDamage(), damageGen)
		};
	}
}
