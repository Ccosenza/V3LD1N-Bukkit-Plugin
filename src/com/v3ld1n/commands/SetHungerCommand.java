package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class SetHungerCommand extends V3LD1NCommand {
    public SetHungerCommand() {
        this.addUsage("<hunger>", "Set your hunger level");
        this.addUsage("<hunger> <player>", "Set a player's hunger level");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.sethunger")) {
            int l = args.length;
            if (l == 1 || l == 2) {
                int hunger;
                try {
                    hunger = Integer.parseInt(args[0]);
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
                if (hunger >= 0 && hunger <= 20) {
                    p.setFoodLevel(hunger);
                    String message;
                    if (p.getName().equals(sender.getName())) {
                        message = String.format(Message.SETHUNGER_SET_OWN.toString(), args[0]);
                    } else {
                        message = String.format(Message.SETHUNGER_SET.toString(), p.getName(), args[0]);
                    }
                    ChatUtil.sendMessage(sender, message, 2);
                    return true;
                }
                sender.sendMessage(Message.SETHUNGER_LIMIT.toString());
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}