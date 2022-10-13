package club.mineplex.clans.events;

import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Rey
 * @created 10/09/2022
 * @project Clans Mod
 */
@Value
@AllArgsConstructor
public class MineplexGameStatusEvent extends Event {

    MineplexGame game;
    MineplexGame.Status status;

}
