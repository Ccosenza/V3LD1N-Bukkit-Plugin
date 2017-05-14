package com.v3ld1n.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;

public class ResourcePackCommand extends V3LD1NCommand {
    public ResourcePackCommand() {
        this.addUsage("", "Download the default resource pack");
        this.addUsage("<resource pack>", "Download a resource pack");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        List<ResourcePack> packs = V3LD1N.getResourcePacks();
        if (packs.isEmpty()) {
            Message.get("resourcepack-no-resourcepacks").send(player);
            return true;
        }

        String packName = null;
        if (args.length == 0) {
            // Use the default resource pack
            packName = packs.get(0).getName();
        } else if (args.length == 1) {
            // Use first argument as the resource pack
            if (V3LD1N.getResourcePack(args[0]) == null) {
                this.sendUsage(player);
                return true;
            }
            packName = args[0];
        } else {
            // Too many arguments
            this.sendUsage(player);
            return true;
        }

        set(player, packName);
        return true;
    }

    // Asks the player to download the resource pack
    private void set(Player player, String packName) {
        player.setResourcePack(V3LD1N.getResourcePack(packName).getUrl());
    }

    // Sends command usage with resource pack list
    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        List<String> names = V3LD1N.getResourcePackNames();
        ChatUtil.sendList(user, Message.get("resourcepack-list-title").toString(), names, ListType.LONG);
    }
}