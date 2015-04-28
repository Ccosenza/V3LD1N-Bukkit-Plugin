package com.v3ld1n.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;

public class ResourcePackCommand extends V3LD1NCommand {
    public ResourcePackCommand() {
        this.addUsage("[resource pack]", "Download a resource pack");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<ResourcePack> packs = V3LD1N.getResourcePacks();
            if (!packs.isEmpty()) {
                String pack = null;
                if (args.length == 0) {
                    pack = packs.get(0).getName();
                } else if (args.length == 1) {
                    if (V3LD1N.getResourcePack(args[0]) != null) {
                        pack = args[0];
                    } else {
                        this.sendUsage(p, label, command);
                        ChatUtil.sendList(p, Message.RESOURCEPACK_LIST_TITLE.toString(), V3LD1N.getResourcePackNames(), ListType.LONG);
                        return true;
                    }
                }
                p.setResourcePack(V3LD1N.getResourcePack(pack).getUrl());
                return true;
            }
            Message.RESOURCEPACK_ERROR.send(p);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}