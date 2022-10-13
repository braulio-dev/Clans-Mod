 package club.mineplex.clans.item.rune.repo.armor;

 import club.mineplex.clans.item.value.ValueRange;
 import club.mineplex.clans.item.value.ValueRangeDisplay;
 import club.mineplex.clans.item.rune.RuneType;
 import club.mineplex.clans.item.rune.ReductionRuneImpl;

 /**
  * @author Mineplex
  */
 public class ReinforcedAttribute extends ReductionRuneImpl
{
	private static ValueRange reductionGen = new ValueRange(0.5d, 1.0d);
	
	public ReinforcedAttribute()
	{
		super(RuneType.PREFIX, reductionGen);
	}

	@Override
	public String getDisplayName() 
	{
		return "Reinforced";
	}

	@Override
	public String getDescriptionFormat() {
		return "-%.1f damage taken from melee";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<>(getFlatReduction(), reductionGen)
		};
	}
}
