package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class RideCommand extends V3LD1NCommand implements Listener {
    private BukkitTask task;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.ride")) {
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (task != null) {
                    task.cancel();
                }
                V3LD1N.usingRideCommand.put(p.getUniqueId(), true);
                ChatUtil.sendMessage(p, Message.RIDE_USE.toString(), 2);
                task = Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                    @Override
                    public void run() {
                        if (V3LD1N.usingRideCommand.get(p.getUniqueId()) != null) {
                            if (V3LD1N.usingRideCommand.get(p.getUniqueId()) == true) {
                                V3LD1N.usingRideCommand.remove(p.getUniqueId());
                                ChatUtil.sendMessage(p, Message.RIDE_NO_TIME.toString(), 2);
                            }
                        }
                    }
                }, 200L);
                return true;
            }
            sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (V3LD1N.usingRideCommand.containsKey(p.getUniqueId()) && V3LD1N.usingRideCommand.get(p.getUniqueId())) {
            Entity entity = event.getRightClicked();
            entity.setPassenger(p);
            V3LD1N.usingRideCommand.remove(p.getUniqueId());
            task.cancel();
            ChatUtil.sendMessage(p, String.format(Message.RIDE_RIDE.toString(), StringUtil.getEntityName(entity)), 2);
        }
    }
}