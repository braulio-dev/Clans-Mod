package club.mineplex.clans.item.value;

import java.util.Optional;

public class ValueRangeDisplay<K extends Number> {

    private final transient K value;
    private final transient ValueRange scale;
    private final transient boolean reverseScale;

    public ValueRangeDisplay(K value, ValueRange scale) {
        this(value, scale, false);
    }

    public ValueRangeDisplay(K value, ValueRange scale, boolean reverseScale) {
        this.value = value;
        this.scale = scale;
        this.reverseScale = reverseScale;
    }

    public K getValue() {
        return value;
    }

    public ValueRange getRange() {
        return scale;
    }

    public boolean isReverse() {
        return reverseScale;
    }

    public final Optional<String> getDisplay() {
        return Optional.ofNullable(displayOverride());
    }

    protected String displayOverride() {
        return null;
    }

    public K getDisplayValue() {
        return getValue();
    }

}
