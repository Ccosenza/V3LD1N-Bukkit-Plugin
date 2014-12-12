package com.v3ld1n.commands;

import java.util.UUID;

import com.v3ld1n.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;

public class AutoResourcePackCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            UUID uuid = p.getUniqueId();
            if (PlayerData.AUTO_RESOURCE_PACK.getBoolean(uuid)) {
                PlayerData.AUTO_RESOURCE_PACK.set(uuid, null);
                ChatUtil.sendMessage(p, Message.AUTORESOURCEPACK_DISABLE.toString(), 2);
                return true;
            }
            PlayerData.AUTO_RESOURCE_PACK.set(uuid, true);
            ChatUtil.sendMessage(p, Message.AUTORESOURCEPACK_ENABLE.toString(), 2);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}