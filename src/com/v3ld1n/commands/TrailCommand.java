package com.v3ld1n.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
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
                            PlayerData.TRAILS.set(p, null);
                            Message.get("trail-remove").send(sender);
                            return true;
                        }
                        Message.get("command-not-player").send(sender);
                        return true;
                    } else if (args.length == 2) {
                        if (sender.hasPermission("v3ld1n.trail.others")) {
                            if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                                Player p = PlayerUtil.getOnlinePlayer(args[1]);
                                PlayerData.TRAILS.set(p, null);
                                Message removeOther = Message.get("trail-remove-other");
                                removeOther.sendF(sender, p.getName());
                                return true;
                            }
                            sendInvalidPlayerMessage(sender);
                            return true;
                        }
                        Message.get("trail-others-permission").send(sender);
                        return true;
                    }
                    this.sendUsage(sender);
                    return true;
                }
            }
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    List<String> blockedTrails = ConfigSetting.PARTICLE_TRAILS_BLOCKED.getStringList();
                    if (blockedTrails.contains(args[0]) && !p.hasPermission("v3ld1n.trails.useblocked")) {
                    	Message.get("trail-blocked").aSendF(p, args[0]);
                    	return true;
                    }
                    PlayerData.TRAILS.set(p, args[0]);
                    Message.get("trail-set").sendF(sender, args[0]);
                    return true;
                }
                Message.get("command-not-player").send(sender);
                return true;
            } else if (args.length == 2) {
                if (sender.hasPermission("v3ld1n.trails.others")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (p.getName().equals(args[1])) {
                            PlayerData.TRAILS.set(p, args[0]);
                            Message setOther = Message.get("trail-set-other");
                            setOther.sendF(sender, p.getName(), args[0], p.getName());
                            return true;
                        }
                        sendInvalidPlayerMessage(sender);
                        return true;
                    }
                }
                Message.get("trail-others-permission").send(sender);
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}