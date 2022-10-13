package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.AttackRuneImpl;
import club.mineplex.clans.item.rune.RuneType;


public class FlamingAttribute extends AttackRuneImpl
{
	private static ValueRange attackGen = new ValueRange(2, 4);
	private static ValueRange fireGen = new ValueRange(60, 120);
	
	private int _fireDuration;
	
	public FlamingAttribute() {
		super(RuneType.SUPER_PREFIX, attackGen.rounded());
		_fireDuration = fireGen.rounded();
	}

	@Override
	public String getDisplayName() 
	{
		return "Flaming";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Every %d attacks gives Fire for %.1f seconds";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getAttackLimit(), attackGen, true),
				new ValueRangeDisplay<Double>((double) _fireDuration, fireGen) {
					@Override
					public Double getDisplayValue() {
						return _fireDuration / 20d;
					}
				}
		};
	}
}
