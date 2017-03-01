package com.v3ld1n.commands;

import java.util.List;

import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.V3LD1N;

public class AutoResourcePackCommand extends V3LD1NCommand {
    public AutoResourcePackCommand() {
        this.addUsage("<resource pack>", "Download a resource pack when you join the server");
        this.addUsage("remove", "Remove your auto resource pack");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            this.sendUsage(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            removeResourcePack(player);
            return true;
        }

        if (V3LD1N.getResourcePack(args[0]) != null) {
            setResourcePack(player, args[0]);
        } else {
            sendError(player, args[0]);
        }
        return true;
    }

    // Removes a player's auto resource pack
    public void removeResourcePack(Player player) {
        PlayerData.AUTO_RESOURCE_PACK.set(player, null);
        Message.get("autoresourcepack-remove").aSend(player);
    }

    // Sets a player's auto resource pack
    public void setResourcePack(Player player, String resourcePack) {
        PlayerData.AUTO_RESOURCE_PACK.set(player, resourcePack);
        Message.get("autoresourcepack-set").sendF(player, resourcePack);
        Message.get("autoresourcepack-remove-help").send(player);
    }

    // Displays an error if the resource pack doesn't exist
    public void sendError(Player player, String enteredPack) {
        List<String> names = V3LD1N.getResourcePackNames();
        ChatUtil.sendList(player, Message.get("resourcepack-list-title").toString(), names, ListType.LONG);
        Message.get("autoresourcepack-error").sendF(player, enteredPack);
    }

    // Displays the player's current auto resource pack when sending the command usage
    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        List<String> names = V3LD1N.getResourcePackNames();
        ChatUtil.sendList(user, Message.get("resourcepack-list-title").toString(), names, ListType.LONG);
        if (!(user instanceof Player)) {
            return;
        }
        Player player = (Player) user;
        if (PlayerData.AUTO_RESOURCE_PACK.get(player) != null) {
            String currentPack = PlayerData.AUTO_RESOURCE_PACK.getString(player);
            Message.get("autoresourcepack-current").sendF(user, currentPack);
        }
    }
}