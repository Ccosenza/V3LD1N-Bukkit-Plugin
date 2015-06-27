package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.PlayerUtil;

public class UUIDCommand extends V3LD1NCommand {
    public UUIDCommand() {
        this.addUsage("", "Send your UUID");
        this.addUsage("<player>", "Send a player's UUID");
    }

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
        } else if (args.length == 1) {
            if (PlayerUtil.getUuid(args[0], true) != null) {
                uuid = PlayerUtil.getUuid(args[0], true).toString();
                name = args[0];
            } else {
                sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                return true;
            }
        } else {
            this.sendUsage(sender, label, command);
            return true;
        }
        if (sender instanceof Player) {
            String message = "{text:\"" + uuid + "\","
                    + "color:yellow,"
                    + "hoverEvent:{"
                    + "action:\"show_text\","
                    + "value:\"" + String.format(Message.UUID_HOVER.toString(), name) + "\"},"
                    + "insertion:\"" + uuid + "\"}";
            ChatUtil.sendJsonMessage(sender, message, MessageType.CHAT);
            return true;
        }
        sender.sendMessage(uuid);
        return true;
    }
}