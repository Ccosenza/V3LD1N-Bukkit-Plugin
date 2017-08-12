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
        if (sendPermissionMessage(sender, "v3ld1n.ride")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        final Player player = (Player) sender;

        if (task != null) {
            task.cancel();
        }

        RideType type = null;
        if (args.length == 0) {
            // No arguments, use default type
            type = RideType.RIDE;
        } else if (args.length == 1) {
            // Drop command
            if (args[0].equalsIgnoreCase("drop")) {
                drop(player);
                return true;
            }

            // Use first argument as type
            try {
                type = RideType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                this.sendUsage(player);
                return true;
            }
        } else {
            // Too many arguments
            this.sendUsage(player);
            return true;
        }
        addToList(player, type);
        return true;
    }

    // Adds the player to the list of players using /ride
    private void addToList(Player player, RideType type) {
        UUID uuid = player.getUniqueId();
        V3LD1N.usingRideCommand.put(uuid, type);
        Message.get("ride-click").aSend(player);
        task = Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (V3LD1N.usingRideCommand.get(uuid) != null) {
                    V3LD1N.usingRideCommand.remove(uuid);
                    Message.get("ride-no-selection").aSend(player);
                }
            }
        }, 200);
        
    }

    // Drops the entity the player is holding
    private void drop(Player player) {
        if (player.getPassengers().isEmpty()) {
            Message.get("ride-drop-not-holding").send(player);
        } else {
            String entityname = StringUtil.getEntityName(player.getPassengers().get(0));
            player.eject();
            Message.get("ride-drop").aSendF(player, entityname);
        }
    }

    // Player finishes ride/hold command by right-clicking an entity
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        HashMap<UUID, RideType> using = V3LD1N.usingRideCommand;
        if (!using.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        PlayerAnimation.SWING_ARM.play(player);
        Entity entity = event.getRightClicked();
        RideType type = using.get(player.getUniqueId());
        switch (type) {
        case RIDE:
            entity.addPassenger(player);
            break;
        case HOLD:
            player.addPassenger(entity);
            Message.get("ride-hold").aSendF(player, StringUtil.getEntityName(entity));
            break;
        }

        V3LD1N.usingRideCommand.remove(player.getUniqueId());
        task.cancel();
    }
}