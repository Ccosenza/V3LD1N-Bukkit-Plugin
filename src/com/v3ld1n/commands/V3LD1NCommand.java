package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class V3LD1NCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                List<String> commands = new ArrayList<>();
                commands.add("debug");
                commands.add("reload");
                commands.add("resourcepackurl");
                commands.add("setresourcepack");
                commands.add("togglewarp");
                commands.add("version");
                commands.add("warps");
                sender.sendMessage(Message.V3LD1NPLUGIN_AVAILABLE.toString());
                if (!(sender instanceof Player)) {
                    for (String commandString : commands) {
                        sender.sendMessage(StringUtil.formatText(String.format(Message.V3LD1NPLUGIN_COMMAND_NOT_PLAYER.toString(), "/" + label + " " + commandString)));
                    }
                } else {
                    for (String commandString : commands) {
                        ChatUtil.sendJsonMessage(sender,
                        "{text:\"" + Message.V3LD1NPLUGIN_DASH + "\","
                        + "extra:[{"
                        + "text:\"/" + label + " " + commandString + "\","
                        + "color:gold,"
                        + "hoverEvent:{"
                        + "action:\"show_text\","
                        + "value:\"" + Message.COMMAND_HOVER + "\"},"
                        + "clickEvent:{"
                        + "action:\"suggest_command\","
                        + "value:\"/" + label + " " + commandString + "\"}}]}"
                        , 0);
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("debug") && args.length == 1) {
                Message message = null;
                if (ConfigSetting.DEBUG.getBoolean()) {
                    message = Message.V3LD1NPLUGIN_DISABLE_DEBUG;
                } else {
                    message = Message.V3LD1NPLUGIN_ENABLE_DEBUG;
                }
                ConfigUtil.toggleDebug();
                sender.sendMessage(message.toString());
                return true;
            } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                V3LD1N.getPlugin().reloadConfig();
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
            } else if (args[0].equalsIgnoreCase("setresourcepack") && args.length == 2) {
                ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_SETRESOURCEPACK.toString(), args[1]), 2);
                ConfigSetting.RESOURCE_PACK.setValue(args[1]);
                return true;
            } else if (args[0].equalsIgnoreCase("togglewarp") && args.length == 2) {
                if (args[1].equalsIgnoreCase("all")) {
                    for (String warp : ConfigUtil.getWarps()) {
                        ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_TOGGLE_ALL_WARPS.toString(), warp), 2);
                        ConfigUtil.toggleWarp(warp);
                    }
                    return true;
                }
                if (ConfigUtil.isWarpEnabled(args[1])) {
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_DISABLE_WARP.toString(), args[1]), 2);
                } else {
                    if (ConfigUtil.getWarps().contains(args[1])) {
                        ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_ENABLE_WARP.toString(), args[1]), 2);
                    } else {
                        ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_INVALID_WARP.toString(), args[1]), 2);
                    }
                }
                ConfigUtil.toggleWarp(args[1]);
                return true;
            } else if (args[0].equalsIgnoreCase("version") && args.length == 1) {
                ChatUtil.sendMessage(sender, String.format(Message.V3LD1NPLUGIN_VERSION.toString(), V3LD1N.getPlugin().getDescription().getName(), V3LD1N.getPlugin().getDescription().getVersion()), 2);
                return true;
            } else if (args[0].equalsIgnoreCase("warps") && args.length == 1) {
                StringBuilder sb = new StringBuilder();
                for (String warp : ConfigUtil.getWarps()) {
                    ChatColor color;
                    if (ConfigUtil.isWarpEnabled(warp)) {
                        color = ChatColor.GREEN;
                    } else {
                        color = ChatColor.RED;
                    }
                    sb.append(String.format(Message.V3LD1NPLUGIN_WARPS_ITEM.toString(), color + warp));
                }
                String message = Message.V3LD1NPLUGIN_WARPS.toString() + sb.toString().substring(0, sb.toString().length() - 2);
                sender.sendMessage(message);
                return true;
            }
        } else {
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        return false;
    }
}