package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.v3ld1n.Message;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.PlayerUtil;

public class PushCommand implements CommandExecutor {
    final double SPEED_DEFAULT = 1.0;
    final double SPEED_LIMIT = 8.0;

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
                    sender.sendMessage(String.format(Message.PUSH_INVALID_SPEED.toString(), SPEED_DEFAULT));
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
                    sender.sendMessage(String.format(Message.PUSH_PUSH.toString(), p.getName(), speedX, speedY, speedZ));
                }
                sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                return true;
            } else if (args.length == 2 || args.length == 3) {
                double speed = SPEED_DEFAULT;
                if (PlayerUtil.getOnlinePlayer(args[0]) != null && PlayerUtil.getOnlinePlayer(args[1]) != null) {
                    Player player = PlayerUtil.getOnlinePlayer(args[0]);
                    Player toPlayer = PlayerUtil.getOnlinePlayer(args[1]);
                    try {
                        speed = Double.parseDouble(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(String.format(Message.PUSH_INVALID_SPEED.toString(), SPEED_DEFAULT));
                    }
                    EntityUtil.pushToward(player, toPlayer.getLocation(), speed, speed, speed);
                    sender.sendMessage(String.format(Message.PUSH_PUSH_TO_PLAYER.toString(), player.getName(), speed));
                    return true;
                }
                sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                return true;
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}