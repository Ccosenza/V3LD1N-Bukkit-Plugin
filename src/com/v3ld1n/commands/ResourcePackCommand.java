package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;

public class ResourcePackCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ConfigSetting.RESOURCE_PACK.getString() != null) {
                p.setResourcePack(ConfigSetting.RESOURCE_PACK.getString());
            } else {
                p.sendMessage(Message.RESOURCEPACK_ERROR.toString());
            }
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}