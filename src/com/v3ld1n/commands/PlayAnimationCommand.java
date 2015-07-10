package com.v3ld1n.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PlayAnimationCommand extends V3LD1NCommand {
    public PlayAnimationCommand() {
        this.addUsage("<animation>", "Play an animation on yourself");
        this.addUsage("<animation> <player>", "Play an animation on a player", "v3ld1n.playanimation.others");
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
                        Message.PLAYANIMATION_PLAY.aSendF(sender, StringUtil.fromEnum(animation, true));
                        return true;
                    } catch (Exception e) {
                        Message.PLAYANIMATION_ERROR.send(p);
                        return true;
                    }
                }
                Message.COMMAND_NOT_PLAYER.send(sender);
                return true;
            } else if (args.length >= 2) {
                if (sender.hasPermission("v3ld1n.playanimation.others")) {
                    if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                        Player p = PlayerUtil.getOnlinePlayer(args[1]);
                        try {
                            PlayerAnimation animation = PlayerAnimation.valueOf(args[0].toUpperCase());
                            animation.play(p, 50);
                            Message.PLAYANIMATION_PLAY.sendF(sender, StringUtil.fromEnum(animation, true));
                            return true;
                        } catch (Exception e) {
                            Message.PLAYANIMATION_ERROR.send(sender);
                            return true;
                        }
                    }
                    sendInvalidPlayerMessage(sender);
                    return true;
                }
                Message.PLAYANIMATION_NO_PERMISSION_OTHERS.send(sender);
                return true;
            }
            this.sendUsage(sender);
            String title = Message.PLAYANIMATION_LIST_TITLE.toString();
            List<PlayerAnimation> animations = Arrays.asList(PlayerAnimation.values());
            ChatUtil.sendList(sender, title, animations, ListType.SHORT);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}