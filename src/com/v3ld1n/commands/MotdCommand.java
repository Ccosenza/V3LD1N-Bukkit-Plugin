package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class MotdCommand extends V3LD1NCommand {
    public MotdCommand() {
        this.addUsage("", "Send the MOTD to yourself");
        this.addUsage("<player>", "Send the MOTD to a player", "v3ld1n.motd.others");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                ChatUtil.sendMotd((Player) sender);
                return true;
            }
            sendPlayerMessage(sender);
            return true;
        } else if (args.length > 0) {
            if (sender.hasPermission("v3ld1n.motd.others")) {
                if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                    Player p = PlayerUtil.getOnlinePlayer(args[0]);
                    ChatUtil.sendMotd(p);
                    Message.MOTD_SHOWING.sendF(sender, p.getName());
                    return true;
                }
                sendInvalidPlayerMessage(sender);
                return true;
            }
            Message.MOTD_NO_PERMISSION_OTHERS.send(sender);
            return true;
        }
        this.sendUsage(sender);
        return true;
    }
}