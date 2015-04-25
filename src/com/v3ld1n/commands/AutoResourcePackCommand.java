package com.v3ld1n.commands;

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
                    ChatUtil.sendMessage(p, Message.AUTORESOURCEPACK_REMOVE.toString(), 2);
                    return true;
                }
                if (V3LD1N.getResourcePack(args[0]) != null) {
                    PlayerData.AUTO_RESOURCE_PACK.set(uuid, args[0]);
                    p.sendMessage(String.format(Message.AUTORESOURCEPACK_SET.toString(), args[0]));
                    p.sendMessage(Message.AUTORESOURCEPACK_REMOVE_COMMAND.toString());
                } else {
                    ChatUtil.sendList(p, Message.RESOURCEPACK_LIST_TITLE.toString(), V3LD1N.getResourcePackNames(), ListType.LONG);
                    p.sendMessage(String.format(Message.AUTORESOURCEPACK_ERROR.toString(), args[0]));
                }
                return true;
            }
            this.sendUsage(p, label, command);
            String currentPack = Message.NONE.toString();
            if (PlayerData.AUTO_RESOURCE_PACK.get(uuid) != null) {
                currentPack = PlayerData.AUTO_RESOURCE_PACK.getString(uuid);
            }
            p.sendMessage(String.format(Message.AUTORESOURCEPACK_INFO.toString(), currentPack));
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}