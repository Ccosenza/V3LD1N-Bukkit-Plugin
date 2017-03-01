package com.v3ld1n.commands;

import java.util.List;

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
        if (sendPermissionMessage(sender, "v3ld1n.trail")) return true;

        if (args.length == 0 || args.length > 2) {
            this.sendUsage(sender);
            return true;
        }

        Player player;
        if (args.length == 1 && sender instanceof Player) {
            // No player argument, command user is player
            player = (Player) sender;
        } else if (args.length == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
            // Player is second argument
            player = PlayerUtil.getOnlinePlayer(args[1]);
        } else {
            // Player doesn't exist
            sendInvalidPlayerMessage(sender);
            return true;
        }

        if (!player.getName().equals(sender.getName()) && !sender.hasPermission("v3ld1n.trail.others")) {
            Message.get("trail-others-permission").send(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            remove(player, sender);
        } else {
            set(player, args[0], sender);
        }
        return true;
    }

    // Sets the player's trail, or sends a message if the trail is blocked
    private void set(Player player, String trail, CommandSender user) {
        List<String> blockedTrails = ConfigSetting.PARTICLE_TRAILS_BLOCKED.getStringList();
        if (blockedTrails.contains(trail) && !player.hasPermission("v3ld1n.trail.useblocked")) {
            Message.get("trail-blocked").aSendF(player, trail);
            return;
        }

        PlayerData.TRAILS.set(player, trail);
        boolean playerIsSender = player.getName().equals(user.getName());
        Message ownMessage = Message.get("trail-set");
        Message otherMessage = Message.get("trail-set-other");
        Message message = playerIsSender ? ownMessage : otherMessage;
        message.aSendF(user, player.getName(), trail);
    }

    // Removes the player's trail
    private void remove(Player player, CommandSender user) {
        PlayerData.TRAILS.set(player, null);
        boolean playerIsSender = player.getName().equals(user.getName());
        Message ownMessage = Message.get("trail-remove");
        Message otherMessage = Message.get("trail-remove-other");
        Message message = playerIsSender ? ownMessage : otherMessage;
        message.aSendF(user, player.getName());
    }
}