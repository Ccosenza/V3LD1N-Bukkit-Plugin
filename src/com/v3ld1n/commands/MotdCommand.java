package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class MotdCommand extends V3LD1NCommand {
    public MotdCommand() {
        this.addUsage("", "Send the MOTD to yourself");
        this.addUsage("<player>", "Send the MOTD to a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                ChatUtil.sendMotd((Player) sender);
                return true;
            }
            sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
            return true;
        } else if (args.length > 0) {
            if (sender.hasPermission("v3ld1n.motd.others")) {
                if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                    Player p = PlayerUtil.getOnlinePlayer(args[0]);
                    ChatUtil.sendMotd(p);
                    sender.sendMessage(String.format(StringUtil.formatText(Message.MOTD_SHOWING.toString()), p.getName()));
                    return true;
                }
                sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                return true;
            }
            sender.sendMessage(Message.MOTD_NO_PERMISSION_OTHERS.toString());
            return true;
        }
        this.sendUsage(sender, label, command);
        return true;
    }
}