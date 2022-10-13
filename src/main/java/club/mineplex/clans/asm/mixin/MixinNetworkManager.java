package club.mineplex.clans.asm.mixin;

import club.mineplex.clans.events.client.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

    @Shadow
    private INetHandler packetListener;

    // Receive
    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    public void channelRead0Pre(final ChannelHandlerContext context,
                                final Packet packet,
                                final CallbackInfo callbackInfo) {

        final PacketEvent.Receive.Pre receivePre = new PacketEvent.Receive.Pre(this.packetListener, context, packet);
        MinecraftForge.EVENT_BUS.post(receivePre);

        if (receivePre.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    // Receive
    @Inject(method = "channelRead0*", at = @At("RETURN"))
    public void channelRead0Post(final ChannelHandlerContext context,
                                 final Packet packet,
                                 final CallbackInfo callbackInfo) {

        final PacketEvent.Receive.Post receivePre = new PacketEvent.Receive.Post(this.packetListener, context, packet);
        MinecraftForge.EVENT_BUS.post(receivePre);
    }

    // Send
    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    public void dispatchPacketHead(final Packet inPacket,
                                   final GenericFutureListener<? extends Future<? super Void>>[] futureListeners,
                                   final CallbackInfo callbackInfo) {

        final PacketEvent.Send.Pre sendPre = new PacketEvent.Send.Pre(futureListeners, inPacket);
        MinecraftForge.EVENT_BUS.post(sendPre);

        if (sendPre.isCanceled()) {
            callbackInfo.cancel();
        }
    }

    // Send
    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    public void dispatchPacketReturn(final Packet inPacket,
                                     final GenericFutureListener<? extends Future<? super Void>>[] futureListeners,
                                     final CallbackInfo callbackInfo) {

        final PacketEvent.Send.Post sendPost = new PacketEvent.Send.Post(futureListeners, inPacket);
        MinecraftForge.EVENT_BUS.post(sendPost);
    }

}
