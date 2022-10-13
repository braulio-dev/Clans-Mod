package club.mineplex.clans.client.modules.clans;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.modules.AbstractMod;
import club.mineplex.clans.client.settings.SettingCategory;
import club.mineplex.clans.client.settings.repo.ClansSettings;
import club.mineplex.clans.events.client.ItemDropEvent;
import club.mineplex.clans.gamestate.mineplex.MineplexData;
import club.mineplex.clans.gamestate.mineplex.MineplexGame;
import club.mineplex.clans.util.UtilClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.stream.Stream;

public class ModuleSlotLock extends AbstractMod {

    private int[] lockedSlots;

    public ModuleSlotLock() {
        super("Slot Lock");

        final Configuration configuration = ClansMod.getInstance().getRootConfiguration();

        configuration.load();
        this.lockedSlots = configuration
                .get(SettingCategory.CLANS.getAbsoluteKey(), "locked-slots", new int[0])
                .getIntList();
        configuration.save();
    }

    public int[] getLockedSlots() {
        return this.lockedSlots.clone();
    }

    @SubscribeEvent
    public void onPreItemDrop(final ItemDropEvent.Pre event) {
        if (!this.isSlotInteractable(event.getItemSlot())) {
            event.setCanceled(true);
        }
    }

    private void lockUnlockSlot(final int slotIndex) {
        if (!this.isEnabled()) {
            return;
        }

        final boolean lock = Arrays.stream(this.lockedSlots).noneMatch(i -> i == slotIndex);
        if (lock) {
            this.lockedSlots = Stream.concat(
                                             Arrays.stream(this.lockedSlots).boxed(),
                                             Stream.of(slotIndex)
                                     )
                                     .mapToInt(Integer::intValue)
                                     .toArray();
        } else {
            this.lockedSlots = Arrays.stream(this.lockedSlots).filter(i -> i != slotIndex).toArray();
        }
        UtilClient.playSound("random.door_open", 2F, 2F);

        final Configuration configuration = ClansMod.getInstance().getRootConfiguration();
        configuration.load();
        configuration.get(SettingCategory.CLANS.getAbsoluteKey(), "locked-slots", new int[0]).set(this.lockedSlots);
        configuration.save();
    }

    public void lockUnlockSlot(final Slot slot) {
        this.lockUnlockSlot(slot.getSlotIndex());
    }

    public boolean isSlotInteractable(final int slotIndex) {
        final Slot slot = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(slotIndex);
        return this.isSlotInteractable(slot);
    }

    public boolean isSlotInteractable(final Slot slot) {
        if (!this.isEnabled()) {
            return true;
        }
        if (slot == null || !(slot.inventory instanceof InventoryPlayer)) {
            return true;
        }
        return Arrays.stream(this.lockedSlots).noneMatch(i -> i == slot.getSlotIndex());
    }

    @Override
    public boolean isModuleUsable() {
        return ClansSettings.LOCK_INVENTORY_SLOTS
                && this.gameState.isMineplex()
                && this.gameState.getMultiplayerData().as(MineplexData.class).getGame().equals(
                MineplexGame.CLANS);
    }

}
