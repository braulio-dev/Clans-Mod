package club.mineplex.clans.item.rune.repo.bow;

import club.mineplex.clans.item.rune.RuneImpl;
import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.RuneType;


public class InverseAttribute extends RuneImpl
{
	private static ValueRange knockbackGen = new ValueRange(0.d, 1.d);
	
	private double _knockbackModifier;
	
	public InverseAttribute()
	{
		super(RuneType.PREFIX);
		
		_knockbackModifier = knockbackGen.decimal();
	}

	@Override
	public String getDisplayName() 
	{
		return "Inverse";	
	}
	
	@Override
	public String getDescriptionFormat() {
		return "Reverse knockback and modify amount to %.2f percent";
	}

	@Override
	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[] {
				new ValueRangeDisplay<Double>(_knockbackModifier, knockbackGen) {
					@Override
					public Double getDisplayValue() {
						return (.5d + _knockbackModifier) * 100d;
					}
				}
		};
	}
}
