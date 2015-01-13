package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class TotalPlayersCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int players = Bukkit.getServer().getOfflinePlayers().length;
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String title = Message.TOTALPLAYERS_TITLE.toString();
            String subtitle = String.format(Message.TOTALPLAYERS_SUBTITLE.toString(), players);
            PlayerUtil.displayTitle(p, "{text:\"" + title + "\"}", 2, 2, 2);
            PlayerUtil.displaySubtitle(p, "{text:\"" + subtitle + "\"}", 2, 2, 2, false);
            return true;
        }
        String notPlayer = String.format(Message.TOTALPLAYERS_NOT_PLAYER.toString(), players);
        ChatUtil.sendMessage(sender, notPlayer, 2);
        return true;
    }
}