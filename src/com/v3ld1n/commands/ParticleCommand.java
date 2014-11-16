package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.Particle;

public class ParticleCommand implements CommandExecutor {
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.particle")) {
            World world = null;
            if (args.length == 9) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    world = p.getWorld();
                } else if (sender instanceof BlockCommandSender) {
                    BlockCommandSender cb = (BlockCommandSender) sender;
                    world = cb.getBlock().getWorld();
                } else {
                    sender.sendMessage(String.format(Message.PARTICLE_NO_WORLD.toString(), Bukkit.getServer().getWorlds().get(0).getName()));
                    world = Bukkit.getServer().getWorlds().get(0);
                }
            } else if (args.length == 10) {
                if (Bukkit.getServer().getWorld(args[9]) != null) {
                    world = Bukkit.getServer().getWorld(args[9]);
                } else {
                    sender.sendMessage(String.format(Message.PARTICLE_INVALID_WORLD.toString(), Bukkit.getServer().getWorlds().get(0).getName()));
                    world = Bukkit.getServer().getWorlds().get(0);
                }
            }
            if (args.length > 8 && args.length < 11) {
                double locX;
                double locY;
                double locZ;
                if (args[1].equalsIgnoreCase("target")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locX = p.getTargetBlock(null, 100).getX();
                    } else {
                        sender.sendMessage(Message.PARTICLE_TARGET.toString());
                        return false;
                    }
                } else if (args[1].equals("~")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locX = p.getLocation().getX();
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locX = cb.getBlock().getLocation().getX();
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else if (args[1].startsWith("~") && !(args[1].equals("~"))) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locX = p.getLocation().getX() + Double.parseDouble(args[1].substring(1));
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locX = cb.getBlock().getLocation().getX() + Double.parseDouble(args[1].substring(1));
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else {
                    locX = Double.parseDouble(args[7]);
                }
                if (args[2].equalsIgnoreCase("target")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locY = p.getTargetBlock(null, 100).getY();
                    } else {
                        sender.sendMessage(Message.PARTICLE_TARGET.toString());
                        return false;
                    }
                } else if (args[2].equals("~")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locY = p.getLocation().getY();
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locY = cb.getBlock().getLocation().getY();
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else if (args[2].startsWith("~") && !(args[2].equals("~"))) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locY = p.getLocation().getY() + Double.parseDouble(args[2].substring(1));
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locY = cb.getBlock().getLocation().getY() + Double.parseDouble(args[2].substring(1));
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else {
                    locY = Double.parseDouble(args[2]);
                }
                if (args[3].equalsIgnoreCase("target")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locZ = p.getTargetBlock(null, 100).getZ();
                    } else {
                        sender.sendMessage(Message.PARTICLE_TARGET.toString());
                        return false;
                    }
                } else if (args[3].equals("~")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locZ = p.getLocation().getZ();
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locZ = cb.getBlock().getLocation().getZ();
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else if (args[3].startsWith("~") && !(args[3].equals("~"))) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        locZ = p.getLocation().getZ() + Double.parseDouble(args[3].substring(1));
                    } else if (sender instanceof BlockCommandSender) {
                        BlockCommandSender cb = (BlockCommandSender) sender;
                        locZ = cb.getBlock().getLocation().getZ() + Double.parseDouble(args[3].substring(1));
                    } else {
                        sender.sendMessage(Message.PARTICLE_RELATIVE.toString());
                        return false;
                    }
                } else {
                    locZ = Double.parseDouble(args[3]);
                }
                short count;
                float speed;
                float offsetX;
                float offsetY;
                float offsetZ;
                
                Location loc = new Location(world, locX, locY, locZ);
                try {
                count = Short.parseShort(args[8]);
                speed = Float.parseFloat(args[7]);
                offsetX = Float.parseFloat(args[4]);
                offsetY = Float.parseFloat(args[5]);
                offsetZ = Float.parseFloat(args[6]);
                } catch (IllegalArgumentException e) {
                    return false;
                }
                try {
                    Particle.builder()
                        .setName(args[0])
                        .setOffsetX(offsetX)
                        .setOffsetY(offsetY)
                        .setOffsetZ(offsetZ)
                        .setSpeed(speed)
                        .setCount(count)
                        .build()
                        .display(loc);
                    sender.sendMessage(String.format(Message.PARTICLE_SPAWN.toString(), count, args[0], loc.getX(), loc.getY(), loc.getZ(), loc.getWorld().getName()));
                    return true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Message.PARTICLE_INVALID.toString());
                    e.printStackTrace();
                    return true;
                }
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}