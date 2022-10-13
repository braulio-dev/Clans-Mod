package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.AttackRuneImpl;
import club.mineplex.clans.item.rune.RuneType;


public class ScorchingAttribute extends AttackRuneImpl
{
	private static ValueRange fireGen = new ValueRange(2, 6);
	
	private double _fireDuration;
	
	public ScorchingAttribute()
	{
		super(RuneType.SUPER_PREFIX, 1);	// Activates every hit
		_fireDuration = fireGen.decimal();
	}

	@Override
	public String getDisplayName() 
	{
		return "Scorching";
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Struck enemies catch fire for %.2f seconds";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(_fireDuration, fireGen)
		};
	}
}
