package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;

import com.v3ld1n.Message;

public class SetFullTimeCommand extends V3LD1NCommand {
    public SetFullTimeCommand() {
        this.addUsage("<time>", "Set the full time for the world you're in");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof LivingEntity) {
            if (sender.hasPermission("v3ld1n.owner")) {
                if (args.length == 1) {
                    LivingEntity p = (LivingEntity) sender;
                    long time = p.getWorld().getFullTime();
                    long newTime = Long.parseLong(args[0]);
                    p.getWorld().setFullTime(newTime);
                    Message.SETFULLTIME_SET.sendF(sender, time, newTime);
                    return true;
                }
                this.sendUsage(sender, label, command);
                return true;
            }
            sendPermissionMessage(sender);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}