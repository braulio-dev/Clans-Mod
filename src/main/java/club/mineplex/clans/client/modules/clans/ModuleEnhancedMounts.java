package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.SettingCategory;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.util.object.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleEnhancedMounts extends AbstractMod {

    private int selectedMount;
    private boolean queued = false;

    public ModuleEnhancedMounts() {
        super("Enhanced Mounts");

        this.selectedMount = ClansMod.getInstance()
                                     .getRootConfiguration()
                                     .get(SettingCategory.CLANS.getAbsoluteKey(), "selected-mount", -1)
                                     .getInt();
    }

    public void spawnMount() {
        if (!this.isEnabled()) {
            return;
        }

        Minecraft.getMinecraft().thePlayer.sendChatMessage("/mount");
        this.queued = true;
    }

    @SubscribeEvent
    public void onGuiOpen(final GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (this.isMountsGui(event.gui) && this.queued) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(final GuiScreenEvent.InitGuiEvent.Pre event) {
        if (!this.isMountsGui(event.gui) || !this.queued || this.selectedMount == -1) {
            return;
        }

        final ContainerChest container = (ContainerChest) ((GuiContainer) event.gui).inventorySlots;
        new DelayedTask(() ->
                                this.getPlayerController().windowClick(
                                        container.windowId,
                                        this.selectedMount,
                                        0,
                                        0,
                                        Minecraft.getMinecraft().thePlayer
                                ),
                        0
        );

        new DelayedTask(() -> {
            this.getPlayer().closeScreen();
            this.queued = false;
        }, 0);
    }

    public void onGuiClick(final ContainerChest container, final Slot click) {
        if (!this.isMountsGui(container) || click == null) {
            return;
        }

        this.selectedMount = click.getSlotIndex();

        final Configuration configuration = ClansMod.getInstance().getRootConfiguration();
        configuration.load();
        configuration.getCategory(SettingCategory.CLANS.getAbsoluteKey()).get("selected-mount").set(this.selectedMount);
        configuration.save();
    }

    @Override
    public boolean isModuleUsable() {

        return ClansSettings.USE_MOUNT_SPAWN_KEY
                && this.gameState.isMineplex()
                && this.gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS);
    }

    private boolean isMountsGui(final GuiScreen screen) {
        if (!(screen instanceof GuiChest)) {
            return false;
        }
        if (!(((GuiContainer) screen).inventorySlots instanceof ContainerChest)) {
            return false;
        }

        if (!this.isEnabled()) {
            return false;
        }

        final GuiChest chest = (GuiChest) screen;
        final ContainerChest container = (ContainerChest) chest.inventorySlots;
        if (!container.getLowerChestInventory().hasCustomName()) {
            return false;
        }

        return container.getLowerChestInventory().getDisplayName().getUnformattedText().equals("Manage Mounts");
    }

    private boolean isMountsGui(final ContainerChest container) {
        if (!container.getLowerChestInventory().hasCustomName()) {
            return false;
        }
        if (!this.isEnabled()) {
            return false;
        }
        return container.getLowerChestInventory().getDisplayName().getUnformattedText().equals("Manage Mounts");
    }

}
