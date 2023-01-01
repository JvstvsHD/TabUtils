package de.jvstvshd.tabutils.v1_8_R3;

import de.jvstvshd.tabutils.common.AbstractTabUtilsPlugin;
import io.netty.channel.ChannelPipeline;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.entity.Player;

public class TabUtilsPlugin extends AbstractTabUtilsPlugin<PacketHandler_1_8_R3, PacketPlayOutPlayerInfo> {

    @Override
    public PacketHandler_1_8_R3 instantiatePacketHandler(Player player) {
        return new PacketHandler_1_8_R3(config);
    }

    @Override
    protected ChannelPipeline pipeline(Player player) {
        return ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
    }

    @Override
    protected PacketPlayOutPlayerInfo createPacket(Player player) {
        return new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_LATENCY, ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle());
    }
}
