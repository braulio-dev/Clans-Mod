package club.mineplex.clans.item.value;

public class ValueRange {
	
	private final double min;
	private final double max;

	public ValueRange(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public int rounded() {
		return (int) Math.round(decimal());
	}

	public double decimal() {
		return min + (Math.random() * Math.random()) * gap();
	}

	public double gap() {
		return max - min;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

}
