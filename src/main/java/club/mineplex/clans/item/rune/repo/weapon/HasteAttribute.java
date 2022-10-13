package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.AttackRuneImpl;
import club.mineplex.clans.item.rune.RuneType;
import club.mineplex.clans.util.UtilText;


public class HasteAttribute extends AttackRuneImpl
{
	private static ValueRange attackGen = new ValueRange(2, 4);
	private static ValueRange speedGen = new ValueRange(0, 2);
	private static ValueRange durationGen = new ValueRange(60, 120);
	
	private int _speedAmount;
	private int _speedDuration;
	
	public HasteAttribute() {
		super(RuneType.SUFFIX, attackGen.rounded());
		
		_speedAmount = speedGen.rounded();
		_speedDuration = durationGen.rounded();
	}

	@Override
	public String getDisplayName() 
	{
		return "Haste";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Every %d attacks gives you Speed %s for %.1f seconds";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getAttackLimit(), attackGen, true),
				new ValueRangeDisplay<Integer>(_speedAmount, speedGen) {
					@Override
					protected String displayOverride() {
						return UtilText.toRoman(_speedAmount + 1);
					}
				},
				new ValueRangeDisplay<Float>((float) _speedDuration, durationGen) {
					@Override
					public Float getDisplayValue() {
						return _speedDuration / 20f;
					}
				}
		};
	}
}
