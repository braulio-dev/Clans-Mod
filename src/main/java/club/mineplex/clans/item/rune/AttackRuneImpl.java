package club.mineplex.clans.item.rune;


public abstract class AttackRuneImpl extends RuneImpl {

	private int _attackLimit;
	private int _attackCount = 0;

	protected AttackRuneImpl(RuneType type, int attackLimit) {
		super(type);
		_attackLimit = attackLimit;
	}

	public int getAttackLimit() { return _attackLimit; }

	public int getAttackCount() {
		return _attackCount;
	}
}
