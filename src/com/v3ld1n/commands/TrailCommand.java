package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class TrailCommand extends V3LD1NCommand {
    public TrailCommand() {
        this.addUsage("<particle>", "Set your trail");
        this.addUsage("<particle> <player>", "Set a player's trail", "v3ld1n.trail.others");
        this.addUsage("remove", "Remove your trail");
        this.addUsage("remove <player>", "Remove a player's trail", "v3ld1n.trail.others");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.trail")) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            PlayerData.TRAILS.set(p.getUniqueId(), null);
                            ChatUtil.sendMessage(sender, Message.TRAIL_REMOVE_OWN.toString(), 2);
                            return true;
                        }
                    } else if (args.length == 2) {
                        if (sender.hasPermission("v3ld1n.trail.others")) {
                            if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                                Player p = PlayerUtil.getOnlinePlayer(args[1]);
                                PlayerData.TRAILS.set(p.getUniqueId(), null);
                                ChatUtil.sendMessage(sender, String.format(Message.TRAIL_REMOVE_OTHER.toString(), p.getName()), 2);
                                return true;
                            }
                            sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                            return true;
                        }
                        sender.sendMessage(Message.TRAIL_NO_PERMISSION_OTHERS.toString());
                        return true;
                    }
                    this.sendUsage(sender, label, command);
                    return true;
                }
            }
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    PlayerData.TRAILS.set(p.getUniqueId(), args[0]);
                    ChatUtil.sendMessage(sender, String.format(Message.TRAIL_SET_OWN.toString(), args[0]), 2);
                    return true;
                }
            } else if (args.length == 2) {
                if (sender.hasPermission("v3ld1n.trails.others")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (p.getName().equals(args[1])) {
                            PlayerData.TRAILS.set(p.getUniqueId(), args[0]);
                            ChatUtil.sendMessage(sender, String.format(Message.TRAIL_SET_OTHER.toString(), p.getName(), args[0], p.getName()), 2);
                            return true;
                        }
                        sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                        return true;
                    }
                }
                sender.sendMessage(Message.TRAIL_NO_PERMISSION_OTHERS.toString());
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}