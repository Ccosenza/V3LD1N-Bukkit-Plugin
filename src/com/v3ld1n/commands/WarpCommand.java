package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.SoundUtil;

public class WarpCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Warp warp = null;
            for (Warp warpObject : V3LD1N.getWarps()) {
                if (warpObject.getName().equalsIgnoreCase(label)) {
                    warp = warpObject;
                }
            }
            if (warp != null) {
                Bukkit.getServer().dispatchCommand(sender, "warp " + label);
                for (Particle particle : warp.getParticles()) {
                    particle.display(p.getLocation(), p);
                }
                for (String sound : warp.getSounds()) {
                    SoundUtil.playSoundString(sound, p.getLocation());
                }
                return true;
            }
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}