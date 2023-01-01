package de.jvstvshd.tabutils.common;

import de.jvstvshd.tabutils.common.config.Config;
import io.netty.channel.ChannelPipeline;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class AbstractTabUtilsPlugin<T extends PacketHandler<P>, P> extends JavaPlugin implements Listener, TabExecutor {

    protected Config config;

    protected ServerVersion serverVersion;

    @Override
    public final void onEnable() {
        config = new Config(this);
        try {
            if (!Files.exists(config.path())) {
                Files.createDirectories(config.path().getParent());
                Files.createFile(config.path());
                config.save();
            }
            Thread.currentThread().setContextClassLoader(getClassLoader());
            config.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverVersion = ServerVersion.determine();
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getOnlinePlayers().forEach(this::addPacketHandler);
        getCommand("tabutils").setExecutor(this);
        enable();
    }

    public void enable() {
    }

    @Override
    public final void onDisable() {
        getServer().getOnlinePlayers().forEach(this::removePacketHandler);
        disable();
    }

    public void disable() {
    }

    public abstract T instantiatePacketHandler(Player player);

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        addPacketHandler(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        removePacketHandler(event.getPlayer());
    }

    public void addPacketHandler(Player player) {
        pipeline(player).addBefore("packet_handler", "tabutils", instantiatePacketHandler(player));
    }

    public void removePacketHandler(Player player) {
        if (pipeline(player).get("tabutils") != null) {
            pipeline(player).remove("tabutils");
        }
    }

    //protected abstract void sendPacket(Player player, P packet);

    protected abstract ChannelPipeline pipeline(Player player);

    protected abstract P createPacket(Player player);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        if (!sender.hasPermission("tabutils.commands")) {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to execute this command.");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "modify":
                boolean newModify = Boolean.parseBoolean(args[1]);
                config.configData().setModifyPing(newModify);
                if (!saveConfig(sender)) return true;
                sender.sendMessage(ChatColor.GRAY + "The ping in the tablist will " + (newModify ? ChatColor.GREEN + "now" : "no longer") + ChatColor.GRAY + " be modified.");
                sender.sendMessage(ChatColor.GRAY + "It may take some time until the changes will be applied.");
                break;
            case "ping":
                int newPing;
                try {
                    newPing = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "The ping must be a number.");
                    return true;
                }
                config.configData().setPing(newPing);
                if (!saveConfig(sender)) return true;
                sender.sendMessage(ChatColor.GRAY + "The ping in the tablist will now be " + ChatColor.GREEN + newPing + ChatColor.GRAY + ".");
                sender.sendMessage(ChatColor.GRAY + "It may take some time until the changes will be applied.");
                break;
            default:
                sendHelp(sender);
                return true;
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            P packet = createPacket(onlinePlayer);
            //pipeline(onlinePlayer).writeAndFlush(packet);
            pipeline(onlinePlayer).channel().writeAndFlush(packet);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completeValues = new ArrayList<>();
        if (args.length == 1) {
            completeValues.addAll(Arrays.asList("modify", "ping"));
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("modify")) {
                completeValues.addAll(Arrays.asList("true", "false"));
            }
        }
        return completeValues.stream().filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    public void sendHelp(CommandSender sender) {

        sender.sendMessage(ChatColor.GREEN + "TabUtils " + ChatColor.GRAY + "by " + ChatColor.GOLD + "JvstvsHD");
        sender.sendMessage(ChatColor.GRAY + "Version: " + ChatColor.GOLD + getDescription().getVersion());
        if (!sender.hasPermission("tabutils.commands")) {
            return;
        }
        sender.sendMessage(ChatColor.GOLD + "Recognized Commands:");
        sender.sendMessage(ChatColor.GOLD + "/tabutils modify <true/false> " + ChatColor.GRAY + "- Whether the ping should be modified in tab.");
        sender.sendMessage(ChatColor.GOLD + "/tabutils ping <number> " + ChatColor.GRAY + "- The ping that should be displayed in tab.");
    }

    public boolean saveConfig(CommandSender sender) {
        try {
            config.save();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to save config.", e);
            if (sender != null)
                sender.sendMessage(ChatColor.RED + "Failed to save config.");
            return false;
        }
        return true;
    }
}
