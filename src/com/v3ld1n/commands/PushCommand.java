package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.v3ld1n.Message;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PushCommand extends V3LD1NCommand {
    public PushCommand() {
        this.addUsage("<player> <speedX> <speedY> <speedZ>", "Push a player in a direction");
        this.addUsage("<player> <toPlayer> <speed>", "Push a player toward another player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.push")) return true;

        if (args.length != 3 && args.length != 4) {
            this.sendUsage(sender);
            return true;
        }

        if (PlayerUtil.getOnlinePlayer(args[0]) == null) {
            sendInvalidPlayerMessage(sender);
            return true;
        }
        Player player = PlayerUtil.getOnlinePlayer(args[0]);

        if (args.length == 3) {
            // Push player toward another player
            if (PlayerUtil.getOnlinePlayer(args[1]) == null) {
                sendInvalidPlayerMessage(sender);
                return true;
            }
            Player toPlayer = PlayerUtil.getOnlinePlayer(args[1]);

            if (!StringUtil.isDouble(args[2])) {
                Message.get("push-invalid-speed").send(sender);
                return true;
            }
            double speed = Double.parseDouble(args[2]);

            pushToPlayer(player, toPlayer, speed, sender);
            return true;
        } else {
            // Push player in a direction
            double speedX, speedY, speedZ;
            try {
                speedX = Double.parseDouble(args[1]);
                speedY = Double.parseDouble(args[2]);
                speedZ = Double.parseDouble(args[3]);
            } catch (Exception e) {
                Message.get("push-invalid-speed").send(sender);
                return true;
            }

            pushInDirection(player, speedX, speedY, speedZ, sender);
            return true;
        }
    }

    // Pushes the player in a direction
    private void pushInDirection(Player player, double speedX, double speedY, double speedZ, CommandSender user) {
        Vector velocity = new Vector(speedX, speedY, speedZ);
        player.setVelocity(velocity);
        Message.get("push-push").aSendF(user, player.getName(), speedX, speedY, speedZ);
    }

    // Pushes the player toward another player
    private void pushToPlayer(Player player, Player toPlayer, double speed, CommandSender user) {
        EntityUtil.pushToward(player, toPlayer.getLocation(), new Vector(speed, speed, speed), false);
        Message.get("push-push-to-player").aSendF(user, player.getName(), speed);
    }
}