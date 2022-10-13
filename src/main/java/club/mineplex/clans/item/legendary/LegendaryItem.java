package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.CustomItem;
import club.mineplex.clans.item.value.ValueRange;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Optional;


public class LegendaryItem extends CustomItem
{

	private final transient Type type;
	protected long _lastBlock = 0;
	
	protected LegendaryItem(String name, String[] description, Type type) {
		super(type.getType(), name, description);

		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public static Optional<Type> fromType(Item type) {
		for (Type value : Type.values()) {
			if (value.getType() == type) {
				return Optional.of(value);
			}
		}
		return Optional.empty();
	}

	public enum Type {

		MAGNETIC_MAUL(Items.record_far),
		ALLIGATORS_TOOTH(Items.record_chirp),
		SCYTHE_OF_THE_FALLEN_LORD(Items.record_stal),
		GIANTS_BROADSWORD(Items.record_13),
		HYPER_AXE(Items.record_blocks),
		KNIGHTS_GREATLANCE(Items.record_wait),
		MERIDIAN_SCEPTER(Items.record_mall),
		WIND_BLADE(Items.record_cat);

		private final Item type;

		Type(Item type) {
			this.type = type;
		}

		public Item getType() {
			return type;
		}

	}

}