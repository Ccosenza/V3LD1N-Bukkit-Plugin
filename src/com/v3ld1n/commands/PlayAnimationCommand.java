package com.v3ld1n.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PlayAnimationCommand extends V3LD1NCommand {
    public PlayAnimationCommand() {
        this.addUsage("<animation>", "Play an animation on yourself");
        this.addUsage("<animation> <player>", "Play an animation on a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.playanimation")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    try {
                        PlayerAnimation animation = PlayerAnimation.valueOf(args[0].toUpperCase());
                        animation.play(p, 50);
                        ChatUtil.sendMessage(sender, String.format(Message.PLAYANIMATION_PLAY.toString(), StringUtil.fromEnum(animation, true)), 2);
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
                            PlayerAnimation animation = PlayerAnimation.valueOf(args[0].toUpperCase());
                            animation.play(p, 50);
                            ChatUtil.sendMessage(sender, String.format(Message.PLAYANIMATION_PLAY.toString(), StringUtil.fromEnum(animation, true)), 2);
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
            this.sendUsage(sender, label, command);
            ChatUtil.sendShortList(sender, Message.PLAYANIMATION_LIST_TITLE.toString(), Arrays.asList(PlayerAnimation.values()));
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}