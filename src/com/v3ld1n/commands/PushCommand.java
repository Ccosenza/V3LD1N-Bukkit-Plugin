package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.v3ld1n.Message;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.PlayerUtil;

public class PushCommand extends V3LD1NCommand {
    private static final double SPEED_DEFAULT = 1.0;
    private static final double SPEED_LIMIT = 8.0;

    public PushCommand() {
        this.addUsage("<player> <speedX> <speedY> <speedZ>", "Push a player in a direction");
        this.addUsage("<player> <toPlayer> [speed]", "Push a player toward another player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.push")) {
            if (args.length == 4) {
                double speedX = SPEED_DEFAULT;
                double speedY = SPEED_DEFAULT;
                double speedZ = SPEED_DEFAULT;
                try {
                    speedX = Double.parseDouble(args[1]);
                    speedY = Double.parseDouble(args[2]);
                    speedZ = Double.parseDouble(args[3]);
                } catch (Exception e) {
                    Message.PUSH_INVALID_SPEED.sendF(sender, SPEED_DEFAULT);
                }
                if (speedX > SPEED_LIMIT) {
                    speedX = SPEED_LIMIT;
                }
                if (speedY > SPEED_LIMIT) {
                    speedY = SPEED_LIMIT;
                }
                if (speedZ > SPEED_LIMIT) {
                    speedZ = SPEED_LIMIT;
                }
                Vector velocity = new Vector(speedX, speedY, speedZ);
                if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                    Player p = PlayerUtil.getOnlinePlayer(args[0]);
                    p.setVelocity(velocity);
                    Message.PUSH_PUSH.aSendF(sender, p.getName(), speedX, speedY, speedZ);
                    return true;
                }
                sendInvalidPlayerMessage(sender);
                return true;
            } else if (args.length == 2 || args.length == 3) {
                double speed = SPEED_DEFAULT;
                if (PlayerUtil.getOnlinePlayer(args[0]) != null && PlayerUtil.getOnlinePlayer(args[1]) != null) {
                    Player player = PlayerUtil.getOnlinePlayer(args[0]);
                    Player toPlayer = PlayerUtil.getOnlinePlayer(args[1]);
                    try {
                        speed = Double.parseDouble(args[2]);
                    } catch (Exception e) {
                        Message.PUSH_INVALID_SPEED.sendF(sender, SPEED_DEFAULT);
                    }
                    EntityUtil.pushToward(player, toPlayer.getLocation(), new Vector(speed, speed, speed), false);
                    Message.PUSH_PUSH_TO_PLAYER.aSendF(sender, player.getName(), speed);
                    return true;
                }
                sendInvalidPlayerMessage(sender);
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}