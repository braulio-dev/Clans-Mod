package club.mineplex.clans.cache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class ModCache<T> {

    ModCache() {
        this(30L);
    }

    private ModCache(final long minuteInterval) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updateCache();
            }
        }, 0, TimeUnit.MINUTES.toMillis(minuteInterval));
    }

    public abstract T get();

    public abstract void updateCache();

}
