package de.jvstvshd.tabutils.v1_8_R3;

import de.jvstvshd.tabutils.common.PacketHandler;
import de.jvstvshd.tabutils.common.Reflection;
import de.jvstvshd.tabutils.common.config.Config;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;

import java.lang.reflect.Field;
import java.util.List;

public class PacketHandler_1_8_R3 extends PacketHandler<PacketPlayOutPlayerInfo> {

    public PacketHandler_1_8_R3(Config config) {
        super(config);
    }

    @Override
    public boolean isSuitingPacket(Object packet) {
        return packet instanceof PacketPlayOutPlayerInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handlePacketWrite(ChannelHandlerContext ctx, PacketPlayOutPlayerInfo packet, ChannelPromise promise) throws Exception {
        Field entriesField = packet.getClass().getDeclaredField("b");
        entriesField.setAccessible(true);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> entries =
                (List<PacketPlayOutPlayerInfo.PlayerInfoData>) entriesField.get(packet);
        for (PacketPlayOutPlayerInfo.PlayerInfoData entry : entries) {
            Field field = entry.getClass().getDeclaredField("b");
            Reflection.modifyFinalField(field, entry, config.configData().getPing());
        }
        sendPacket(ctx, packet, promise);
    }
}
