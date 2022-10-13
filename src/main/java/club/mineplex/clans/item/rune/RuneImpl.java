package club.mineplex.clans.item.rune;

import club.mineplex.clans.item.value.ValueRangeDisplay;

import java.util.Arrays;


public abstract class RuneImpl
{

	private final transient RuneType _type;
	
	protected RuneImpl(RuneType type) {
		_type = type;
	}

	public abstract String getDisplayName();

	protected String getDescriptionFormat() {
		return "Default description";
	}

	public ValueRangeDisplay<?>[] getValues() {
		return new ValueRangeDisplay[0];
	}

	/**
	 * @return a user-friendly description of this attribute, entailing it's effects
	 * and current associated values.
	 */
	public final String getDescription() {
		return String.format(
				getDescriptionFormat(),
				Arrays.stream(getValues()).map(pair -> pair.getDisplay().isPresent() ? pair.getDisplay().get() : pair.getDisplayValue()).toArray()
		);
	}

	public RuneType getType() {
		return _type;
	}

}