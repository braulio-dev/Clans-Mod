package club.mineplex.clans.asm.mixin;

import club.mineplex.clans.ClansMod;
import club.mineplex.clans.client.modules.clans.ModuleResourcepack;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {

    @Final
    @Shadow
    private NetworkManager netManager;

    @Inject(at = @At("HEAD"), method = "handleResourcePack", cancellable = true)
    public void handleResourcePack(final S48PacketResourcePackSend send, final CallbackInfo callbackInfo) {
        final ModuleResourcepack resourceModule = ClansMod.getInstance().getModuleThrow(ModuleResourcepack.class);
        if (!resourceModule.shouldCancelResourcepack(send.getURL())) {
            return;
        }

        // Empty hash because Mineplex does that in their server resourcepacks
        this.netManager.sendPacket(new C19PacketResourcePackStatus("", C19PacketResourcePackStatus.Action.ACCEPTED));
        callbackInfo.cancel();
    }

}
