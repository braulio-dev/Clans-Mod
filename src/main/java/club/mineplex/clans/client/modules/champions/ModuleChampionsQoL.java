package club.mineplex.clans.client.modules.champions;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.modules.champions.cache.ChampionsRechargeCache;
import club.mineplex.clans.client.settings.repo.ChampionsSettings;
import club.mineplex.clans.events.client.ItemDropEvent;
import club.mineplex.clans.events.client.PacketEvent;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.util.UtilItem;
import club.mineplex.clans.util.UtilText;
import club.mineplex.core.mineplex.champions.ChampionsBuild;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.ChampionsSkill;
import club.mineplex.core.mineplex.champions.shop.IPreparableArrow;
import club.mineplex.core.mineplex.champions.skill.assassin.Recall;
import club.mineplex.core.mineplex.champions.skill.brute.Intimidation;
import club.mineplex.core.mineplex.champions.skill.knight.LevelField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ModuleChampionsQoL extends AbstractMod {

    private ModuleChampions championsModule = null;

    public ModuleChampionsQoL() {
        super("Champions Quality of Life");
    }

    @Override
    public void init() {
        championsModule = ClansMod.getInstance().getModuleThrow(ModuleChampions.class);
    }

    /**
     * Disables preparing your arrow multiple times in Champions
     */
    @SubscribeEvent
    public void onBowCharge(PacketEvent.Send.Pre event) {
        if (!(event.getPacket() instanceof C02PacketUseEntity) && !(event.getPacket() instanceof C0APacketAnimation)
                && !(event.getPacket() instanceof C07PacketPlayerDigging)) {
            return;
        }

        if (!ChampionsSettings.PREVENT_PREPARING_ARROW_TWICE) {
            return;
        }

        if (event.getPacket() instanceof C02PacketUseEntity) {
            if (!((C02PacketUseEntity) event.getPacket()).getAction().equals(C02PacketUseEntity.Action.ATTACK)) {
                return;
            }
        }

        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            final C07PacketPlayerDigging.Action action = ((C07PacketPlayerDigging) event.getPacket()).getStatus();
            if (action.equals(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS) || action.equals(
                    C07PacketPlayerDigging.Action.DROP_ITEM) || action.equals(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM)) {
                return;
            }
        }

        final ItemStack currentItem = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
        if (currentItem == null || !currentItem.getItem().equals(Items.bow)) {
            return;
        }

        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        final ChampionsBuild build = championsModule.getEquippedBuild();
        if (player == null || build == null) {
            return;
        }

        final Optional<Pair<ChampionsSkill, Integer>> skillOpt = championsModule.getEquippedBuild().getSkill(ChampionsSkill.Type.BOW);
        if (!skillOpt.isPresent()) {
            return;
        }

        final Pair<ChampionsSkill, Integer> bowSkill = skillOpt.get();
        if (!(bowSkill.getKey() instanceof IPreparableArrow) || !championsModule.getRechargeCache().isArrowPrepared()) {
            return;
        }

        event.setCanceled(true);
        // TODO: Also cancel block breaking so you can't create ghost blocks for yourself
    }

    @SubscribeEvent
    public void onItemDrop(ItemDropEvent.Pre event) {
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == null) {
            return;
        }

        final Item item = itemStack.getItem();
        if (!UtilItem.isAxe(item) && !UtilItem.isSword(item)) {
            return;
        }

        final Optional<ChampionsKit> kitOpt = championsModule.getPlayerKit(mc.thePlayer);
        if (!kitOpt.isPresent()) {
            return;
        }

        final ChampionsKit kit = kitOpt.get();
        final ChampionsRechargeCache rechargeCache = championsModule.getRechargeCache();
        final EnumMap<ChampionsSkill.Type, SkillCooldown> recharge = rechargeCache.getRechargeMap(kit);

        final SkillCooldown passiveA = recharge.getOrDefault(ChampionsSkill.Type.PASSIVE_A, null);
        final SkillCooldown passiveB = recharge.getOrDefault(ChampionsSkill.Type.PASSIVE_B, null);
        final List<SkillCooldown> cooldowns = Arrays.asList(passiveA, passiveB);

        for (SkillCooldown cooldown : cooldowns) {
            if (cooldown == null) {
                continue;
            }

            if (!cooldown.getSkill().isPresent()) {
                continue;
            }

            final ChampionsSkill skill = cooldown.getSkill().get();
            if (skill instanceof Intimidation || skill instanceof LevelField) {
                final float timeLeft = cooldown.getAbsoluteTimeLeft();
                if (timeLeft < 0) {
                    continue;
                }

                final ChampionsBuild equippedBuild = championsModule.getEquippedBuild();
                int level = equippedBuild == null ? 0 : equippedBuild.getSkill(skill.getType()).get().getValue();

                UtilText.sendPlayerMessageWithPrefix("Recharge", String.format(
                        "You cannot use &a%s%s&r for &a%.1f Seconds&r.",
                        skill.getName(),
                        level == 0 ? "" : " " + level,
                        timeLeft
                ));

                event.setCanceled(true);
                return;
            }
        }


    }

    @Override
    public boolean isModuleUsable() {
        final MineplexGame game = gameState.getMultiplayerData().as(MineplexData.class).getGame();
        return gameState.isMineplex()
                && (game.equals(MineplexGame.CHAMPIONS_CTF) || game.equals(
                MineplexGame.CHAMPIONS_DOMINATE)
                || game.equals(MineplexGame.CLANS));
    }

}
