package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class UUIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String uuid = "";
        String name = "";
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                uuid = p.getUniqueId().toString();
                name = p.getName();
            } else {
                sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
                return true;
            }
        } else {
            if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                Player p = PlayerUtil.getOnlinePlayer(args[0]);
                uuid = p.getUniqueId().toString();
                name = p.getName();
            } else {
                sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                return true;
            }
        }
        if (sender instanceof Player) {
            String message = "{text:\"" + uuid + "\","
                    + "hoverEvent:{"
                    + "action:\"show_text\","
                    + "value:\"" + String.format(Message.UUID_HOVER.toString(), name) + "\"},"
                    + "clickEvent:{"
                    + "action:\"suggest_command\","
                    + "value:\"" + uuid + "\"}}";
            ChatUtil.sendJsonMessage(sender, message, 0);
            return true;
        }
        sender.sendMessage(uuid);
        return true;
    }
}