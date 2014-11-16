package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;

public class SetHealthCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.hasPermission("v3ld1n.sethealth")) {
                if (args.length == 1) {
                    double arg;
                    try {
                        arg = Double.parseDouble(args[0]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(Message.COMMAND_INVALID_ARGUMENT.toString());
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
                return false;
            }
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER_ENTITY.toString());
        return true;
    }
}