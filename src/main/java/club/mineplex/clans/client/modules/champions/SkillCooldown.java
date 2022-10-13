package club.mineplex.clans.client.modules.champions;

import club.mineplex.clans.client.cooldown.Cooldown;
import club.mineplex.core.mineplex.champions.ChampionsSkill;

import java.util.Optional;

/**
 * @author Rey
 * @created 21/09/2022
 * @project Clans Mod
 */
public class SkillCooldown extends Cooldown {

    private final Cooldown activeAfer;
    private final ChampionsSkill skill;

    public SkillCooldown(ChampionsSkill skill, float cooldownSeconds) {
        this(skill, 0, cooldownSeconds, null);
    }

    public SkillCooldown(ChampionsSkill skill, float activateAfter, float cooldownSeconds) {
        this(skill, activateAfter, cooldownSeconds, null);
    }

    public SkillCooldown(ChampionsSkill skill, float activateAfter, float cooldownSeconds, Runnable onExpire) {
        super(cooldownSeconds, onExpire);
        this.activeAfer = new Cooldown(activateAfter, this::start);
        this.skill = skill;
    }

    @Override
    public void start() {
        if (!activeAfer.hasStarted()) {
            activeAfer.start();
        } else {
            super.start();
        }
    }

    @Override
    public void cancel() {
        activeAfer.cancel();
        super.cancel();
    }

    public float getActivateAfter() {
        return activeAfer.getBaseCooldown();
    }

    public Optional<ChampionsSkill> getSkill() {
        return Optional.ofNullable(skill);
    }

    public float getAbsoluteTimeLeft() {
        return super.getTimeLeft() + Math.max(0, activeAfer.getTimeLeft());
    }

}
