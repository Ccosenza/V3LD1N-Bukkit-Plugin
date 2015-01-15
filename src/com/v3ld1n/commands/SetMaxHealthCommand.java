package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;

public class SetMaxHealthCommand extends V3LD1NCommand {
    private static final double LIMIT = 2000;

    public SetMaxHealthCommand() {
        this.addUsage("<health>", "Set your maximum health");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.hasPermission("v3ld1n.setmaxhealth")) {
                if (args.length == 1) {
                    double arg;
                    try {
                        arg = Double.parseDouble(args[0]);
                    } catch (IllegalArgumentException e) {
                        this.sendUsage(sender, label, command);
                        return true;
                    }
                    if (arg > 0 && arg <= LIMIT) {
                        ((LivingEntity) sender).setMaxHealth(arg);
                        ChatUtil.sendMessage(sender, String.format(Message.SETMAXHEALTH_SET.toString(), args[0]), 2);
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
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}