package club.mineplex.clans.events;

import club.mineplex.clans.gamestate.mineplex.MineplexData;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
@AllArgsConstructor
public class MineplexServerSwitchEvent extends Event {

    private final MineplexData from;
    private final MineplexData to;

}
