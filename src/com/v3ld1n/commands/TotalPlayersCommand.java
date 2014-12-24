package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;

public class TotalPlayersCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int players = Bukkit.getServer().getOfflinePlayers().length;
        ChatUtil.sendMessage(sender, String.format(Message.TOTALPLAYERS_PLAYERS.toString(), players), 2);
        return true;
    }
}