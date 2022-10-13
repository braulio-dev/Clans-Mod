package club.mineplex.clans.asm.mixin;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.modules.clans.ModuleDropPrevention;
import club.mineplex.clans.events.client.ItemDropEvent;
import club.mineplex.clans.util.UtilClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {

    @Inject(method = "dropOneItem", at = @At("HEAD"), cancellable = true)
    private void dropOneItem(final boolean dropAll, final CallbackInfoReturnable<EntityItem> callback) {
        final Minecraft mc = Minecraft.getMinecraft();
        final Slot heldSlot = UtilClient.getSlotInHotbar(mc.thePlayer.inventory.currentItem);

        // Updates the rendered selected slot and the ItemStack object being held
        mc.playerController.updateController();

        final ItemStack itemStack = heldSlot.getStack();
        if (mc.thePlayer.getHeldItem() != itemStack) {
            callback.cancel();
            return;
        }

        final ItemDropEvent itemDropEventPre = new ItemDropEvent.Pre(itemStack, dropAll, heldSlot);
        MinecraftForge.EVENT_BUS.post(itemDropEventPre);
        if (itemDropEventPre.isCanceled()) {
            callback.setReturnValue(null);
        }

        // Drop Prevention
        final ModuleDropPrevention dropPrevention = ClansMod.getInstance().getModuleThrow(ModuleDropPrevention.class);
        if (!dropPrevention.handleDrop(mc.thePlayer.getHeldItem())) {
            callback.setReturnValue(null);
        }

        final ItemDropEvent itemDropEventPost = new ItemDropEvent.Post(itemStack, dropAll, heldSlot);
        MinecraftForge.EVENT_BUS.post(itemDropEventPost);

    }

}