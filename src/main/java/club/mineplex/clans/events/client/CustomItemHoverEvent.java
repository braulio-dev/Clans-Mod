package club.mineplex.clans.events.client;

import club.mineplex.clans.item.CustomItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomItemHoverEvent extends Event {

    private final CustomItem customItem;
    private final List<String> tooltip;

}
