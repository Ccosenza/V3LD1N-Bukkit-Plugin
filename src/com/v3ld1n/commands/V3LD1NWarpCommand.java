package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class V3LD1NWarpCommand extends V3LD1NCommand {
    public V3LD1NWarpCommand() {
        this.addUsage("add <name>", "Add a warp");
        this.addUsage("remove <name>", "Remove a warp");
        this.addUsage("list", "Send a list of warps");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;

        if (args.length == 0) {
            this.sendUsage(sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            add(args[1], sender);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            remove(args[1], sender);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            list(sender);
            return true;
        }
        this.sendUsage(sender);
        return true;
    }

    // Adds the warp command
    private void add(String warpName, CommandSender user) {
        V3LD1N.addWarp(new Warp(warpName, new ArrayList<Particle>(), new ArrayList<Sound>()));
        Message.get("v3ld1nwarp-add").aSendF(user, warpName);
    }

    // Removes the warp command
    private void remove(String warpName, CommandSender user) {
        boolean warpExists = false;
        String exactWarpName = null;
        for (Warp warp : V3LD1N.getWarps()) {
            if (warp.getName().equalsIgnoreCase(warpName)) {
                warpExists = true;
                exactWarpName = warp.getName();
            }
        }

        if (warpExists) {
            V3LD1N.removeWarp(exactWarpName);
            Message.get("v3ld1nwarp-remove").aSendF(user, exactWarpName);
            return;
        }
        Message.get("v3ld1nwarp-invalid").sendF(user, warpName);
    }

    // Sends a list of all warp commands
    private void list(CommandSender user) {
        List<String> warpNames = new ArrayList<>();
        for (Warp warp : V3LD1N.getWarps()) {
            warpNames.add(warp.getName());
        }
        ChatUtil.sendList(user, Message.get("v3ld1nwarp-list-title").toString(), warpNames, ListType.SHORT);
    }
}