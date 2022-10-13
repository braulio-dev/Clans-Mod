package club.mineplex.clans.events;

import club.mineplex.core.mineplex.champions.ChampionsBuild;
import lombok.Data;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
public abstract class ChampionsBuildEvent extends Event {

    private final ChampionsBuild build;

    public static class Equip extends ChampionsBuildEvent {

        private final ChampionsBuild nextBuild;

        public Equip(final ChampionsBuild build, final ChampionsBuild nextBuild) {
            super(build);
            this.nextBuild = nextBuild;
        }

        public ChampionsBuild getNextBuild() {
            return this.nextBuild;
        }

    }

    public static class Unequip extends ChampionsBuildEvent {

        public Unequip(final ChampionsBuild previous) {
            super(previous);
        }

    }

}
