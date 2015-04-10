package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigAccessor;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.PlayerUtil;

public class V3LD1NPluginCommand extends V3LD1NCommand {
    private String usageSetResourcePack = "setresourcepack <url>";

    public V3LD1NPluginCommand() {
        this.addUsage("debug", "Toggle debug mode");
        this.addUsage("help", "Show a list of all plugin commands");
        this.addUsage("reload", "Reload the plugin config");
        this.addUsage("resourcepackurl", "Send the URL to the resource pack");
        this.addUsage(usageSetResourcePack, "Set the URL to the resource pack");
        this.addUsage("version", "Send the plugin version");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                this.sendUsage(sender, label, command);
                return true;
            }
            if (args[0].equalsIgnoreCase("debug") && args.length == 1) {
                Message message = null;
                if (ConfigSetting.DEBUG.getBoolean()) {
                    message = Message.V3LD1NPLUGIN_DISABLE_DEBUG;
                } else {
                    message = Message.V3LD1NPLUGIN_ENABLE_DEBUG;
                }
                ConfigUtil.toggleSetting(ConfigSetting.DEBUG);
                ChatUtil.sendMessage(sender, message.toString(), 2);
                return true;
            } else if (args[0].equalsIgnoreCase("help") && args.length == 1) {
                sender.sendMessage(Message.V3LD1NPLUGIN_HELP.toString());
                for (String v3ld1ncommand : V3LD1N.getCommands().keySet()) {
                    V3LD1N.getCommands().get(v3ld1ncommand).sendUsageNoTitle(sender, v3ld1ncommand);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                for (ConfigAccessor accessor : V3LD1N.getConfigs()) {
                    accessor.reloadConfig();
                }
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (ConfigSetting.PLAYER_LIST_HEADER.getString() != null && ConfigSetting.PLAYER_LIST_FOOTER.getString() != null) {
                        PlayerUtil.sendPlayerListHeaderFooter(p, ConfigSetting.PLAYER_LIST_HEADER.getString(), ConfigSetting.PLAYER_LIST_FOOTER.getString());
                    }
                }
                ChatUtil.sendMessage(sender, Message.V3LD1NPLUGIN_RELOAD.toString(), 2);
                return true;
            } else if (args[0].equalsIgnoreCase("resourcepackurl") && args.length == 1) {
                String pack = ConfigSetting.RESOURCE_PACK.getString();
                if (sender instanceof Player) {
                   ChatUtil.sendJsonMessage(sender,
                   "{text:\"" + pack + "\","
                   + "underlined:true,"
                   + "hoverEvent:{action:'show_text',value:'" + Message.LINK_HOVER + "'},"
                   + "clickEvent:{action:'open_url',value:'" + pack + "'}}"
                   , 0);
                   return true;
                }
                sender.sendMessage(ConfigSetting.RESOURCE_PACK.getString());
                return true;
            } else if (args[0].equalsIgnoreCase("setresourcepack")) {
                if (args.length == 2) {
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_SETRESOURCEPACK.toString(), args[1]), 2);
                    ConfigSetting.RESOURCE_PACK.setValue(args[1]);
                } else {
                    this.sendArgumentUsage(sender, label, command, usageSetResourcePack);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("version") && args.length == 1) {
                ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_VERSION.toString(), V3LD1N.getPlugin().getDescription().getName(), V3LD1N.getPlugin().getDescription().getVersion()), 2);
                return true;
            }
        } else {
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        this.sendUsage(sender, label, command);
        return true;
    }
}