package de.jvstvshd.tabutils.v1_19_R2;

import de.jvstvshd.tabutils.common.PacketHandler;
import de.jvstvshd.tabutils.common.Reflection;
import de.jvstvshd.tabutils.common.config.Config;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;

public class PacketHandler_1_19_R2 extends PacketHandler<ClientboundPlayerInfoUpdatePacket> {

    public PacketHandler_1_19_R2(Config config) {
        super(config);
    }

    @Override
    public boolean isSuitingPacket(Object packet) {
        return packet instanceof ClientboundPlayerInfoUpdatePacket;
    }

    @Override
    public void handlePacketWrite(ChannelHandlerContext ctx, ClientboundPlayerInfoUpdatePacket packet, ChannelPromise promise) throws Exception {
        var oldEntries = packet.entries();
        var newEntries = oldEntries.stream().map(entry ->
                        new ClientboundPlayerInfoUpdatePacket.Entry(entry.profileId(), entry.profile(), entry.listed(), config.configData().getPing(), entry.gameMode(), entry.displayName(), entry.chatSession()))
                .toList();
        Reflection.modifyFinalFieldUnsafe(packet.getClass().getDeclaredField("b"), packet, newEntries);
        sendPacket(ctx, packet, promise);
    }
}
