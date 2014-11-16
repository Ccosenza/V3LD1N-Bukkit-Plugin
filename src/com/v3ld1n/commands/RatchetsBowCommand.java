package com.v3ld1n.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.util.Particle;

public class RatchetsBowCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getEyeLocation();
            if (args.length > 0) {
                String projectile = args[0].toLowerCase();
                switch (projectile) {
                    case "fireball":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), null);
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_FIREBALL.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Fireball"));
                        return true;
                    case "arrow":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_ARROW.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Arrow"));
                        return true;
                    case "triplearrows":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_TRIPLE_ARROWS.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Triple Arrows"));
                        return true;
                    case "fireworkarrow":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_FIREWORK.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Firework Arrow"));
                        return true;
                    case "snowball":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_SNOWBALL.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Snowball"));
                        return true;
                    case "enderpearl":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_ENDER_PEARL.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Ender Pearl"));
                        return true;
                    case "egg":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_EGG.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Egg"));
                        return true;
                    case "witherskull":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_SKULL.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Wither Skull"));
                        return true;
                    case "bluewitherskull":
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toLowerCase());
                        Particle.fromString(ConfigSetting.PARTICLE_RATCHETSBOW_BLUE_SKULL.getString()).display(loc);
                        p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), "Blue Wither Skull"));
                        return true;
                    default:
                        p.sendMessage(Message.RATCHETSBOW_INVALID_PROJECTILE.toString());
                        return true;
                }
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}