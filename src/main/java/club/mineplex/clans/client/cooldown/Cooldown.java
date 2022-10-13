package club.mineplex.clans.client.cooldown;

import club.mineplex.clans.events.CooldownEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Timer;
import java.util.TimerTask;

public class Cooldown {

    private final float cooldown;
    private long timeStarted = -1;
    private final Timer timer;
    private final Runnable onExpire;

    private boolean started = false;

    public Cooldown(final float cooldownSeconds) {
        this(cooldownSeconds, null);
    }

    public Cooldown(final float cooldownSeconds, final Runnable onExpire) {
        this.cooldown = cooldownSeconds;
        this.timer = new Timer();
        this.onExpire = onExpire;
    }

    public void start() {
        if (this.started) {
            return;
        }

        this.started = true;
        this.timeStarted = System.currentTimeMillis();
        MinecraftForge.EVENT_BUS.post(new CooldownEvent.Start(this));
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Cooldown.this.onExpire != null) {
                    Cooldown.this.onExpire.run();
                }
                MinecraftForge.EVENT_BUS.post(new CooldownEvent.End(Cooldown.this));
            }
        }, (long) (this.cooldown * 1000L));
    }

    public void cancel() {
        this.timer.cancel();
    }

    public float getBaseCooldown() {
        return this.cooldown;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public float getPercentageLeft() {
        return this.getTimeLeft() / this.cooldown;
    }

    public float getTimeLeft() {
        long millisPassed = timeStarted == -1 ? 0L : System.currentTimeMillis() - this.timeStarted;
        return Math.max(-1, this.cooldown - (millisPassed) / 1000F);
    }

}
