package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;

public class FireworkArrowsCommand extends V3LD1NCommand {
    public FireworkArrowsCommand() {
        this.addUsage("<firework type>", "Set your firework arrow type");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                String type = args[0].toUpperCase();
                switch (type) {
                case "BALL":
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), null);
                    p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), "Small Ball"));
                    break;
                case "BALL_LARGE":
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), "Large Ball"));
                    break;
                case "STAR":
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), "Star"));
                    break;
                case "CREEPER":
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), "Creeper"));
                    break;
                case "BURST":
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), "Burst"));
                    break;
                default:
                    p.sendMessage(Message.FIREWORKARROWS_INVALID_SHAPE.toString());
                }
                return true;
            }
            this.sendUsage(sender, label, command.getDescription());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}