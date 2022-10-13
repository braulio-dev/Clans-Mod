package club.mineplex.clans.client.modules.champions.cache;

import club.mineplex.clans.client.modules.champions.BuildUpdater;
import club.mineplex.clans.client.modules.champions.ModuleChampions;
import club.mineplex.clans.events.ChampionsBuildEvent;
import club.mineplex.clans.events.MineplexServerSwitchEvent;
import club.mineplex.clans.util.object.Pair;
import club.mineplex.core.cache.Cache;
import club.mineplex.core.mineplex.champions.ChampionsBuild;
import club.mineplex.core.mineplex.champions.ChampionsKit;
import club.mineplex.core.mineplex.champions.skill.knight.LevelField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChampionsBuildCache extends Cache<ChampionsBuild> {

    private final Set<BuildUpdater> buildUpdaters;
    private final ModuleChampions championsModule;
    private ChampionsBuild equippedBuild = null;
    private boolean manuallyUpdating;

    public ChampionsBuildCache(final ModuleChampions championsModule) {
        super(-1L);
        this.championsModule = championsModule;
        this.buildUpdaters = new HashSet<>(Arrays.asList(
                new BuildUpdater.Automatic(championsModule),
                new BuildUpdater.SkillCommand(championsModule)
        ));
    }

    @Override
    public ChampionsBuild get() {
        return this.equippedBuild;
    }

    @Override
    protected void updateCache() {
        // We do not update this cache because it is dynamically modified when the player un/equips a kit
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof EntityPlayerSP) || !this.championsModule.isModuleUsable()) {
            return;
        }

        if (event.player != Minecraft.getMinecraft().thePlayer) {
            return;
        }

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        final Optional<ChampionsKit> playerKit = this.championsModule.getPlayerKit((EntityPlayerSP) event.player);
        if (!playerKit.isPresent()) {
            if (this.equippedBuild != null) {
                ChampionsBuildEvent buildEvent = new ChampionsBuildEvent.Unequip(this.equippedBuild);
                MinecraftForge.EVENT_BUS.post(buildEvent);
            }

            this.equippedBuild = null;
            return;
        }

        final ChampionsKit equippedKit = playerKit.get();
        final ChampionsKit buildKit = this.equippedBuild == null ? null : this.equippedBuild.getKit();
        if (buildKit == equippedKit) {
            return;
        }

        // The equipped build does not match with the kit, so let's update it
//        this.queueBuildUpdate();
    }

    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (!this.championsModule.  isEnabled()) {
            return;
        }

        // A chest gui is opened
        if (event.gui instanceof GuiChest) {
            final GuiChest gui = (GuiChest) event.gui;
            final ContainerChest container = (ContainerChest) gui.inventorySlots;
            final IInventory chestInventory = container.getLowerChestInventory();

            if (!chestInventory.hasCustomName()) {
                return;
            }

            if (!chestInventory.getDisplayName().getUnformattedText().contains("Select Skills")) {
                return;
            }

            this.manuallyUpdating = true;
        }

        // The gui is closed
        if (event.gui == null && this.manuallyUpdating) {
            this.queueBuildUpdate();
            this.manuallyUpdating = false;
        }
    }

    private void queueBuildUpdate() {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/skill");
        this.buildUpdaters.forEach(buildUpdater -> {
            if (buildUpdater instanceof BuildUpdater.SkillCommand) {
                ((BuildUpdater.SkillCommand) buildUpdater).setInvisible(true);
            }
        });
    }

    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!this.championsModule.isEnabled()) {
            return;
        }

        for (final BuildUpdater buildUpdater : this.buildUpdaters) {

            final boolean invisible = buildUpdater.isInvisible();
            if (buildUpdater.isBuilding()) {
                final Pair<Boolean, ChampionsBuild> endResult = buildUpdater.attemptEnd(event.message);
                final boolean hasEnded = endResult.getKey();
                if (hasEnded) {
                    if (invisible) {
                        event.setCanceled(true);
                    }
                    ChampionsBuildEvent buildEvent = new ChampionsBuildEvent.Equip(this.equippedBuild, endResult.getValue());
                    this.equippedBuild = endResult.getValue();
                    MinecraftForge.EVENT_BUS.post(buildEvent);
                } else {
                    buildUpdater.addChatMessage(event.message);
                }
            } else {
                buildUpdater.attemptBuild(event.message);
            }

            if (buildUpdater.isBuilding() && invisible) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onServerSwitch(final MineplexServerSwitchEvent event) {
        if (this.equippedBuild != null) {
            ChampionsBuildEvent buildEvent = new ChampionsBuildEvent.Unequip(this.equippedBuild);
            MinecraftForge.EVENT_BUS.post(buildEvent);
            this.equippedBuild = null;
        }
    }

}
