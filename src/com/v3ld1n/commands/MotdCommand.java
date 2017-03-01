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
        Player sendTo;

        if (args.length == 0) {
            if (sendNotPlayerMessage(sender)) return true;
            sendTo = (Player) sender;
        } else {
            if (!sender.hasPermission("v3ld1n.motd.others")) {
                Message.get("motd-others-permission").send(sender);
                return true;
            }

            if (PlayerUtil.getOnlinePlayer(args[0]) == null) {
                sendInvalidPlayerMessage(sender);
                return true;
            }

            sendTo = PlayerUtil.getOnlinePlayer(args[0]);
        }

        send(sendTo, sender);
        return true;
    }

    // Sends the MOTD to the player
    private void send(Player player, CommandSender user) {
        ChatUtil.sendMotd(player);
        if (player.getName() != user.getName()) {
            Message.get("motd-show").sendF(user, player.getName());
        }
    }
}