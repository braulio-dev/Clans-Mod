package club.mineplex.clans.item.rune.repo.weapon;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.value.ValueRangeDisplay;
import club.mineplex.clans.item.rune.AttackRuneImpl;
import club.mineplex.clans.item.rune.RuneType;


public class JaggedAttribute extends AttackRuneImpl {
    private static ValueRange attackGen = new ValueRange(2, 4);

    public JaggedAttribute() {
        super(RuneType.PREFIX, attackGen.rounded());
    }

    @Override
    public String getDisplayName() {
        return "Jagged";
    }

    @Override
    public String getDescriptionFormat() {
        return "Every %d attacks mini-stuns enemies";
    }

    @Override
    public ValueRangeDisplay<?>[] getValues() {
        return new ValueRangeDisplay[] {
                new ValueRangeDisplay<>(getAttackLimit(), attackGen, true)
        };
    }
}
