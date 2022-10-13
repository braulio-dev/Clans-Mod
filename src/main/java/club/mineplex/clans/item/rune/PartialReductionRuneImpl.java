package club.mineplex.clans.item.rune;

import club.mineplex.clans.item.value.ValueRange;


public abstract class PartialReductionRuneImpl extends RuneImpl {

	private double _reductionPercent;

	protected PartialReductionRuneImpl(RuneType type, ValueRange reductionGen) {
		super(type);
		_reductionPercent = reductionGen.decimal();
	}

	public double getReductionPercent() {
		return _reductionPercent;
	}

}
