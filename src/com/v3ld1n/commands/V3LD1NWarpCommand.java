package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.Particle;

public class V3LD1NWarpCommand extends V3LD1NCommand {
    public V3LD1NWarpCommand() {
        this.addUsage("add <name>", "Add a warp");
        this.addUsage("remove <name>", "Remove a warp");
        this.addUsage("list", "Send a list of warps");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            List<Warp> warps = V3LD1N.getWarps();
            if (args.length > 1) {
                String warpString;
                warpString = args[1];
                if (args[0].equalsIgnoreCase("add")) {
                    V3LD1N.addWarp(new Warp(warpString, new ArrayList<Particle>(), new ArrayList<String>()));
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NWARP_ADD.toString(), warpString), 2);
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args[1].equalsIgnoreCase("all")) {
                        warps.clear();
                    } else {
                        boolean warpExists = false;
                        for (Warp warp : warps) {
                            if (warp.getName().equalsIgnoreCase(warpString)) {
                                warpExists = true;
                            }
                        }
                        if (warpExists) {
                            V3LD1N.removeWarp(warpString);
                        } else {
                            sender.sendMessage(String.format(Message.V3LD1NWARP_INVALID.toString(), warpString));
                            return true;
                        }
                    }
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NWARP_REMOVE.toString(), warpString), 2);
                    return true;
                }
                this.sendUsage(sender, label, command);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    List<String> warpNames = new ArrayList<>();
                    for (Warp warp : warps) {
                        warpNames.add(warp.getName());
                    }
                    ChatUtil.sendList(sender, Message.V3LD1NWARP_LIST_TITLE.toString(), warpNames);
                    return true;
                }
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}