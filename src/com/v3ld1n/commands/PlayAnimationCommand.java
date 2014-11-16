package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.PlayerUtil;

public class PlayAnimationCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.playanimation")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    try {
                        PlayerAnimation.valueOf(args[0].toUpperCase()).play(p, 50);
                        p.sendMessage(String.format(Message.PLAYANIMATION_PLAY.toString(), args[0]));
                        return true;
                    } catch (Exception e) {
                        p.sendMessage(Message.PLAYANIMATION_ERROR.toString());
                        return true;
                    }
                }
                sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
                return true;
            } else if (args.length >= 2) {
                if (sender.hasPermission("v3ld1n.playanimation.others")) {
                    if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                        Player p = PlayerUtil.getOnlinePlayer(args[1]);
                        try {
                            PlayerAnimation.valueOf(args[0].toUpperCase()).play(p, 50);
                            sender.sendMessage(String.format(Message.PLAYANIMATION_PLAY.toString(), args[0]));
                            return true;
                        } catch (Exception e) {
                            sender.sendMessage(Message.PLAYANIMATION_ERROR.toString());
                            return true;
                        }
                    }
                    sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                    return true;
                }
                sender.sendMessage(Message.PLAYANIMATION_NO_PERMISSION_OTHERS.toString());
                return true;
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}