package club.mineplex.clans.client.modules.champions.cache;

import club.mineplex.clans.client.cooldown.Cooldown;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.client.modules.champions.SkillCooldown;
import club.mineplex.clans.events.ChampionsBuildEvent;
import club.mineplex.core.cache.Cache;
import club.mineplex.core.mineplex.champions.ChampionsBuild;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IActivatable;
import club.mineplex.core.mineplex.champions.shop.IPreparable;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.shop.IRechargeable;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChampionsRechargeCache extends Cache<EnumMap<ChampionsKit, EnumMap<ChampionsSkill.Type, SkillCooldown>>> {

    private static final Pattern COOLDOWN_PATTERN = Pattern.compile(
            "(Ranger|Brute|Mage|Assassin|Knight|Skill)> You (fired|prepared|prepared to|failed to|used) ([a-zA-Z ]+)(?: \\d+)?\\.");

    private final EnumMap<ChampionsKit, EnumMap<ChampionsSkill.Type, SkillCooldown>> rechargeMap = new EnumMap<>(ChampionsKit.class);
    private final ModuleChampions championsModule;
    private boolean isArrowPrepared = false;

    public ChampionsRechargeCache(final ModuleChampions championsModule) {
        super(-1L);
        this.championsModule = championsModule;
        for (ChampionsKit value : ChampionsKit.values()) {
            rechargeMap.put(value, new EnumMap<>(ChampionsSkill.Type.class));
        }
    }

    private Pair<ChampionsSkill, ChampionsSkill.SubSkill> getSubSkill(final String name) {
        for (ChampionsKit kit : ChampionsKit.values()) {
            for (final ChampionsSkill skill : kit.getSkills()) {
                for (final ChampionsSkill.SubSkill subSkill : skill.getSubSkills()) {
                    if (subSkill.getName().equals(name)) {
                        return Pair.of(skill, subSkill);
                    }
                }
            }
        }
        return null;
    }

    public boolean isArrowPrepared() {
        return isArrowPrepared;
    }

    public EnumMap<ChampionsSkill.Type, SkillCooldown> getRechargeMap(ChampionsKit kit) {
        return this.rechargeMap.get(kit);
    }

    @Override
    public EnumMap<ChampionsKit, EnumMap<ChampionsSkill.Type, SkillCooldown>> get() {
        return this.rechargeMap;
    }

    @Override
    protected void updateCache() {
        // Ignore
    }

    @SubscribeEvent
    public void onBuildSwitch(ChampionsBuildEvent.Equip event) {
        isArrowPrepared = false;
    }

    @SubscribeEvent
    public void onBuildUnequip(ChampionsBuildEvent.Unequip event) {
        isArrowPrepared = false;
    }


    @SubscribeEvent
    public void onArrowShoot(final ArrowLooseEvent event) {
        isArrowPrepared = false;
    }

    @SubscribeEvent
    protected void handleChat(final ClientChatReceivedEvent event) {
        final Matcher matcher = COOLDOWN_PATTERN.matcher(event.message.getUnformattedText());
        if (!matcher.matches()) {
            return;
        }

        final String prefix = matcher.group(1);
        final String action = matcher.group(2);
        final String name = matcher.group(3);

        ChampionsKit championsKit = null;
        ChampionsSkill skill;
        ChampionsSkill parent;

        try {
            // Finding the correct parent skill
            skill = ChampionsSkill.ofName(name);
            parent = skill;
        } catch (final IllegalArgumentException e) {
            // Finding the correct sub skill
            final Pair<ChampionsSkill, ChampionsSkill.SubSkill> subSkill = this.getSubSkill(name);
            if (subSkill != null) {
                parent = subSkill.getLeft();
                skill = subSkill.getRight();
            } else {
                return; // because there aren't any subskills with that name
            }
        }

        try {
            championsKit = ChampionsKit.valueOf(prefix);
        } catch (IllegalArgumentException e) {
            // The message starts with "Skill>" rather than "KIT_NAME>"
            String parentName = parent.getName();
            for (ChampionsKit kit : ChampionsKit.values()) {
                final Optional<ChampionsSkill> kitOpt = Arrays.stream(kit.getSkills()).filter(_skill -> _skill.getName().equals(parentName)).findFirst();
                if (kitOpt.isPresent()) {
                    championsKit = kit;
                    break;
                }
            }
        }

        if (championsKit == null) {
            return; // No kit associated to the skill found
        }

        if (!(skill instanceof IRechargeable) && !(skill instanceof IPreparable)) {
            return;
        }

        final ChampionsBuild equippedBuild = this.championsModule.getEquippedBuild();
        if (equippedBuild == null) {
            return;
        }

        final ChampionsSkill.Type skillType = parent.getType();
        if (!this.isSkillInBuild(equippedBuild, parent)) {
            return;
        }

        final Pair<ChampionsSkill, Integer> skillPair = equippedBuild
                .getSkill(parent.getType())
                .orElseThrow(() -> new RuntimeException("Unexpected error"));

        final boolean hasBooster = this.championsModule.hasBoosterWeapon(skillPair.getLeft().getType());
        final int level = this.getBoostedLevel(skillPair.getLeft(), hasBooster, skillPair.getRight());

        switch (action) {
            case "failed to":
                if (!(skill instanceof IPreparable)) {
                    break;
                }

                this.assignCooldown(championsKit, skill, 0, (float) ((IPreparable) skill).getFailRecharge(level));
                break;
            case "prepared to":
            case "prepared":
            case "used":
                if (!(skill instanceof IRechargeable)) {
                    break;
                }

                if (!"used".equals(action) && skill instanceof IPreparableArrow) {
                    isArrowPrepared = true;
                }

                float recharge = (float) ((IRechargeable) skill).getRecharge(level);
                float activateAfter = 0;
                if (skill instanceof IActivatable) {
                    activateAfter += ((IActivatable) skill).applyRechargeAfter(level);
                }

                this.assignCooldown(championsKit, skill, activateAfter, recharge);
                break;
            case "fired":
                if (skill instanceof IPreparableArrow) {
                    isArrowPrepared = false;
                }
                break;
            default:
                break;
        }
    }

    private int getBoostedLevel(final ChampionsSkill skill, final boolean hasBoosterWeapon, final int level) {
        return Math.min(skill.getMaxLevel() + 1, level + (hasBoosterWeapon ? 2 : 0));
    }

    private boolean isSkillInBuild(final ChampionsBuild build, final ChampionsSkill skill) {
        if (build == null) {
            return false;
        }

        final Optional<Pair<ChampionsSkill, Integer>> skillInBuild = build.getSkill(skill.getType());
        return skillInBuild.isPresent() && skillInBuild.get().getLeft() == skill;
    }

    private void assignCooldown(ChampionsKit kit, final ChampionsSkill skill, final float activateAfter, final float cooldown) {
        if (cooldown < 0) {
            return;
        }

        final EnumMap<ChampionsSkill.Type, SkillCooldown> kitCooldowns = this.rechargeMap.get(kit);
        final ChampionsSkill.Type type = skill.getType();
        if (kitCooldowns.containsKey(type)) {
            kitCooldowns.get(type).cancel();
        }

        final SkillCooldown cooldownObj = new SkillCooldown(skill, activateAfter, cooldown, () -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (ChampionsRechargeCache.this.rechargeMap.get(kit).get(type).getTimeLeft() <= 0) {
                    ChampionsRechargeCache.this.rechargeMap.get(kit).remove(type);
                }
            }
        }, 1000L));
        cooldownObj.start();

        kitCooldowns.put(type, cooldownObj);
    }

}
