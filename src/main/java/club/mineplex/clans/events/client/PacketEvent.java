package club.mineplex.clans.events.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Data
public abstract class PacketEvent extends Event {

    @Data
    @AllArgsConstructor
    public abstract static class Send extends PacketEvent {

        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
        private Packet packet;

        @Cancelable
        public static class Pre extends Send {
            public Pre(final GenericFutureListener<? extends Future<? super Void>>[] futureListeners,
                       final Packet packet) {
                super(futureListeners, packet);
            }
        }

        public static class Post extends Send {
            public Post(final GenericFutureListener<? extends Future<? super Void>>[] futureListeners,
                        final Packet packet) {
                super(futureListeners, packet);
            }
        }

    }

    @Data
    @AllArgsConstructor
    public abstract static class Receive extends PacketEvent {

        private final INetHandler packetListener;
        private final ChannelHandlerContext channelHandlerContext;
        private Packet packet;

        @Cancelable
        public static class Pre extends Receive {
            public Pre(final INetHandler packetListener, final ChannelHandlerContext channelHandlerContext,
                       final Packet packet) {
                super(packetListener, channelHandlerContext, packet);
            }
        }

        public static class Post extends Receive {
            public Post(final INetHandler packetListener, final ChannelHandlerContext channelHandlerContext,
                        final Packet packet) {
                super(packetListener, channelHandlerContext, packet);
            }
        }

    }

}
