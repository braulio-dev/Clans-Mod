package club.mineplex.clans.item.legendary;

import club.mineplex.clans.item.value.ValueRange;
import club.mineplex.clans.item.legendary.LegendaryItem;

import static net.minecraft.util.EnumChatFormatting.*;


public class AlligatorsTooth extends LegendaryItem
{
	public static final ValueRange BOOST_GEN = new ValueRange(0.8d, 1.4d);

	private double _swimSpeed;
	private int _soundUpdateCounter;
	
	public AlligatorsTooth() {
		super("Alligators Tooth", new String[] {
				WHITE + "This deadly tooth was stolen from",
				WHITE + "a nest of reptilian beasts long",
				WHITE + "ago. Legends say that the holder",
				WHITE + "is granted the underwater agility",
				WHITE + "of an Alligator.",
				" ",
				WHITE + "Deals " + YELLOW + "8 Damage" + WHITE + " with attack on land",
				WHITE + "Deals " + YELLOW + "12 Damage" + WHITE + " with attack in water",
				WHITE + "Right-Click" + WHITE  + " to use " + GREEN + "Gator Stroke",
		}, Type.ALLIGATORS_TOOTH);
		
		_swimSpeed = BOOST_GEN.decimal();
	}

	public double getSwimSpeed() {
		return _swimSpeed;
	}

}