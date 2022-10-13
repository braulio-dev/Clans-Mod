package club.mineplex.clans.item.rune;

import club.mineplex.clans.item.value.ValueRange;


public abstract class ReductionRuneImpl extends RuneImpl {

	private double _reduction;

	protected ReductionRuneImpl(RuneType type, ValueRange reductionGen) {
		super(type);
		_reduction = reductionGen.decimal();
	}

	public double getFlatReduction() {
		return _reduction;
	}

}
