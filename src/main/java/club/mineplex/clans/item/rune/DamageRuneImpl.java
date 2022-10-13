package club.mineplex.clans.item.rune;

import club.mineplex.clans.item.value.ValueRange;


public abstract class DamageRuneImpl extends RuneImpl {

	private double _bonusDamage;
	
	protected DamageRuneImpl(RuneType type, ValueRange damageGen) {
		super(type);
		_bonusDamage = damageGen.decimal();
	}

	public double getBonusDamage() {
		return _bonusDamage;
	}

}
