package com.v3ld1n.commands;

import java.util.List;
import java.util.UUID;

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
        this.addUsage("[resource pack]", "Download a resource pack when you join the server");
        this.addUsage("remove", "Remove your auto resource pack");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("remove")) {
                    PlayerData.AUTO_RESOURCE_PACK.set(uuid, null);
                    Message.AUTORESOURCEPACK_REMOVE.aSend(p);
                    return true;
                }
                if (V3LD1N.getResourcePack(args[0]) != null) {
                    PlayerData.AUTO_RESOURCE_PACK.set(uuid, args[0]);
                    Message.AUTORESOURCEPACK_SET.sendF(p, args[0]);
                    Message.AUTORESOURCEPACK_REMOVE_COMMAND.send(p);
                } else {
                    List<String> names = V3LD1N.getResourcePackNames();
                    ChatUtil.sendList(p, Message.RESOURCEPACK_LIST_TITLE.toString(), names, ListType.LONG);
                    Message.AUTORESOURCEPACK_ERROR.sendF(p, args[0]);
                }
                return true;
            }
            this.sendUsage(p, label, command);
            String currentPack = Message.NONE.toString();
            if (PlayerData.AUTO_RESOURCE_PACK.get(uuid) != null) {
                currentPack = PlayerData.AUTO_RESOURCE_PACK.getString(uuid);
            }
            Message.AUTORESOURCEPACK_INFO.sendF(p, currentPack);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}