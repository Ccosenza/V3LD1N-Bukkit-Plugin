package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ConfigUtil.isWarpEnabled(command.getName())) {
                Bukkit.getServer().dispatchCommand(sender, "warp " + command.getName());
                for (Particle particle : ConfigUtil.getWarpParticles(command.getName())) {
                    particle.display(p.getLocation(), p);
                }
                return true;
            }
            ChatUtil.sendMessage(p, String.format(Message.WARP_DISABLED.toString(), command.getName()), 2);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}