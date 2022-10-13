package club.mineplex.clans.asm.mixin;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.events.client.CustomItemHoverEvent;
import club.mineplex.clans.item.CustomItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @Inject(at = @At("RETURN"), method = "getTooltip", locals = LocalCapture.CAPTURE_FAILHARD)
    private void getTooltip(EntityPlayer playerIn, boolean advanced, CallbackInfoReturnable<List<String>> cir, List<String> list) {

        final Optional<CustomItem> customItem = ClansMod.getInstance().getItemManager().read(((ItemStack) ((Object) this)));
        if (customItem.isPresent()) {
            CustomItemHoverEvent event = new CustomItemHoverEvent(customItem.get(), list);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }
}
