package club.mineplex.clans.events;

import club.mineplex.clans.client.cooldown.Cooldown;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
@AllArgsConstructor
public abstract class CooldownEvent extends Event {

    private final Cooldown cooldown;

    public static class Start extends CooldownEvent {

        public Start(final Cooldown cooldown) {
            super(cooldown);
        }

    }

    public static class End extends CooldownEvent {

        public End(final Cooldown cooldown) {
            super(cooldown);
        }

    }

}
