package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class SetMaxHealthCommand extends V3LD1NCommand {
    private static final double LIMIT = 2000;

    public SetMaxHealthCommand() {
        this.addUsage("<health>", "Set your maximum health");
        this.addUsage("<health> <player>", "Set a player's maximum health");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.setmaxhealth")) {
            int l = args.length;
            if (l == 1 || l == 2) {
                double health;
                try {
                    health = Double.parseDouble(args[0]);
                } catch (IllegalArgumentException e) {
                    this.sendUsage(sender, label, command);
                    return true;
                }
                Player p;
                if (l == 1 && sender instanceof Player) {
                    p = (Player) sender;
                } else if (l == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
                    p = PlayerUtil.getOnlinePlayer(args[1]);
                } else {
                    sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                    return true;
                }
                if (health >= 0 && health <= LIMIT) {
                    p.setMaxHealth(health);
                    String message;
                    if (p.getName().equals(sender.getName())) {
                        message = String.format(Message.SETMAXHEALTH_SET_OWN.toString(), args[0]);
                    } else {
                        message = String.format(Message.SETMAXHEALTH_SET.toString(), p.getName(), args[0]);
                    }
                    ChatUtil.sendMessage(sender, message, 2);
                    return true;
                }
                sender.sendMessage(String.format(Message.SETMAXHEALTH_LIMIT.toString(), (int) LIMIT));
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}