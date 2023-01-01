package de.jvstvshd.tabutils.common;

import de.jvstvshd.tabutils.common.config.Config;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
public abstract class PacketHandler<T> extends ChannelDuplexHandler {

    protected final Config config;

    public PacketHandler(Config config) {
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        if (isSuitingPacket(o) && config.configData().getModifyPing()) {
            handlePacketWrite(channelHandlerContext, (T) o, channelPromise);
        } else {
            super.write(channelHandlerContext, o, channelPromise);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (isSuitingPacket(o)) {
            handlePacketRead(channelHandlerContext, (T) o);
        } else {
            super.channelRead(channelHandlerContext, o);
        }
    }

    public abstract boolean isSuitingPacket(Object packet);

    public void handlePacketWrite(ChannelHandlerContext ctx, T packet, ChannelPromise promise) throws Exception {
        super.write(ctx, packet, promise);
    }

    public void handlePacketRead(ChannelHandlerContext ctx, T packet) throws Exception {
        super.channelRead(ctx, packet);
    }

    public final void sendPacket(ChannelHandlerContext ctx, T packet, ChannelPromise promise) throws Exception {
        super.write(ctx, packet, promise);
    }
}
