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
import com.v3ld1n.util.PlayerAnimation;
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
                Message.RIDE_USE.aSend(p);
                task = Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                    @Override
                    public void run() {
                        if (V3LD1N.usingRideCommand.get(p.getUniqueId()) != null) {
                            if (V3LD1N.usingRideCommand.get(p.getUniqueId()) == true) {
                                V3LD1N.usingRideCommand.remove(p.getUniqueId());
                                Message.RIDE_NO_TIME.aSend(p);
                            }
                        }
                    }
                }, 200L);
                return true;
            }
            sendPlayerMessage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        if (V3LD1N.usingRideCommand.containsKey(p.getUniqueId()) && V3LD1N.usingRideCommand.get(p.getUniqueId())) {
            PlayerAnimation.SWING_ARM.play(p, 64);
            Entity entity = event.getRightClicked();
            entity.setPassenger(p);
            V3LD1N.usingRideCommand.remove(p.getUniqueId());
            task.cancel();
            Message.RIDE_RIDE.aSendF(p, StringUtil.getEntityName(entity));
        }
    }
}