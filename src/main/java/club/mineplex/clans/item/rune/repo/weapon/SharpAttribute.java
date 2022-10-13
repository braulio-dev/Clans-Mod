package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.item.rune.DamageRuneImpl;


public class SharpAttribute extends DamageRuneImpl
{
	private static ValueRange damageGen = new ValueRange(0.5d, 1.5d);
	
	public SharpAttribute()
	{
		super(RuneType.PREFIX, damageGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Sharp";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "%.2f bonus damage";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getBonusDamage(), damageGen)
		};
	}
}
