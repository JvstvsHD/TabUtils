package de.jvstvshd.tabutils.v1_19_R2;

import de.jvstvshd.tabutils.common.AbstractTabUtilsPlugin;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class TabUtilsPlugin extends AbstractTabUtilsPlugin<PacketHandler_1_19_R2, ClientboundPlayerInfoUpdatePacket> {

    @Override
    public PacketHandler_1_19_R2 instantiatePacketHandler(Player player) {
        return new PacketHandler_1_19_R2(config);
    }

    @Override
    protected ChannelPipeline pipeline(Player player) {
        return ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
    }

    @Override
    protected ClientboundPlayerInfoUpdatePacket createPacket(Player player) {
        return ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(((CraftPlayer) player).getHandle()));
    }
}
