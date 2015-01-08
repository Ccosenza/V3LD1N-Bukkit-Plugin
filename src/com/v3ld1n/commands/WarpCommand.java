package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;

public class WarpCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Bukkit.getServer().dispatchCommand(sender, "warp " + label);
            for (Particle particle : ConfigUtil.getWarpParticles(label)) {
                particle.display(p.getLocation(), p);
            }
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}