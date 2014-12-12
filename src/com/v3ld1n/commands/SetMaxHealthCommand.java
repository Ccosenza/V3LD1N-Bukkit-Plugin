package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;

public class SetMaxHealthCommand implements CommandExecutor {
    final double LIMIT = 1000;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.hasPermission("v3ld1n.setmaxhealth")) {
                if (args.length == 1) {
                    double arg;
                    try {
                        arg = Double.parseDouble(args[0]);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                    if (arg > 0 && arg <= LIMIT) {
                        ((LivingEntity) sender).setMaxHealth(arg);
                        sender.sendMessage(String.format(Message.SETMAXHEALTH_SET.toString(), args[0]));
                        return true;
                    }
                    sender.sendMessage(String.format(Message.SETMAXHEALTH_LIMIT.toString(), LIMIT));
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