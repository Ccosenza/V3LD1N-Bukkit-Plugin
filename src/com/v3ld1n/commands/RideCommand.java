package com.v3ld1n.commands;

import java.util.HashMap;
import java.util.UUID;

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

    public RideCommand() {
        this.addUsage("", "Ride an entity");
        this.addUsage("hold", "Hold an entity");
        this.addUsage("drop", "Drop the entity you are holding");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.ride")) {
            if (sender instanceof Player) {
                final Player p = (Player) sender;
                if (task != null) {
                    task.cancel();
                }
                RideType type = null;
                if (args.length == 0) {
                    type = RideType.RIDE;
                } else if (args.length == 1) {
                	if (args[0].equalsIgnoreCase("drop")) {
                		if (p.getPassenger() != null) {
                    		String entityname = StringUtil.getEntityName(p.getPassenger());
                			p.eject();
                			Message.get("ride-drop").aSendF(p, entityname);
                		}
                		else {
                			Message.get("ride-drop-not-holding").aSend(p);
                		}
                		return true;
                	}
                    try {
                        type = RideType.valueOf(args[0].toUpperCase());
                    } catch (Exception e) {
                    	this.sendUsage(p);
                        return true;
                    }
                } else {
                    this.sendUsage(p);
                    return true;
                }
                V3LD1N.usingRideCommand.put(p.getUniqueId(), type);
                Message.get("ride-click").aSend(p);
                task = Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                    @Override
                    public void run() {
                        if (V3LD1N.usingRideCommand.get(p.getUniqueId()) != null) {
                            V3LD1N.usingRideCommand.remove(p.getUniqueId());
                            Message.get("ride-no-selection").aSend(p);
                        }
                    }
                }, 200);
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
        HashMap<UUID, RideType> using = V3LD1N.usingRideCommand;
        if (using.containsKey(p.getUniqueId())) {
            PlayerAnimation.SWING_ARM.play(p);
            Entity entity = event.getRightClicked();
            RideType type = using.get(p.getUniqueId());
            Message message = null;
            switch (type) {
            case RIDE:
                entity.setPassenger(p);
                message = Message.get("ride-ride");
                break;
            case HOLD:
                p.setPassenger(entity);
                message = Message.get("ride-hold");
                break;
            default:
                break;
            }
            V3LD1N.usingRideCommand.remove(p.getUniqueId());
            task.cancel();
            message.aSendF(p, StringUtil.getEntityName(entity));
        }
    }
}