package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;

public class SetHealthCommand extends V3LD1NCommand {
    public SetHealthCommand() {
        this.addUsage("<health>", "Set your health");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.hasPermission("v3ld1n.sethealth")) {
                if (args.length == 1) {
                    double arg;
                    try {
                        arg = Double.parseDouble(args[0]);
                    } catch (IllegalArgumentException e) {
                        this.sendUsage(sender, label, command);
                        return true;
                    }
                    if (arg >= 0 && arg <= ((LivingEntity) sender).getMaxHealth()) {
                        ((LivingEntity) sender).setHealth(arg);
                        sender.sendMessage(String.format(Message.SETHEALTH_SET.toString(), args[0]));
                        return true;
                    }
                    sender.sendMessage(Message.SETHEALTH_LIMIT.toString());
                    return true;
                }
                this.sendUsage(sender, label, command);
                return true;
            }
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER_ENTITY.toString());
        return true;
    }
}