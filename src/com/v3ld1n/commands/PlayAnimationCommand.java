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
        if (sendPermissionMessage(sender, "v3ld1n.playanimation")) return true;

        if (args.length != 1 && args.length != 2) {
            this.sendUsage(sender);
            return true;
        }

        PlayerAnimation animation;
        try {
            animation = PlayerAnimation.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            this.sendUsage(sender);
            return true;
        }

        Player player;
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        } else if (args.length == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
            player = PlayerUtil.getOnlinePlayer(args[1]);
        } else if (args.length == 2 && !(sender.hasPermission("v3ld1n.playanimation.others"))) {
            Message.get("playanimation-others-permission").send(sender);
            return true;
        } else {
            sendInvalidPlayerMessage(sender);
            return true;
        }

        play(sender, player, animation);
        return true;
    }

    // Plays the animation
    private void play(CommandSender sender, Player player, PlayerAnimation animation) {
        animation.play(player);
        boolean playedOnSelf = player.getName().equals(sender.getName());
        Message message = playedOnSelf ? Message.get("playanimation-play") : Message.get("playanimation-play-other");
        message.aSendF(sender, StringUtil.fromEnum(animation, true), player.getName());
    }
    
    // Lists all valid animations when sending the command usage
    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        String title = Message.get("playanimation-list-title").toString();
        List<PlayerAnimation> animations = Arrays.asList(PlayerAnimation.values());
        ChatUtil.sendList(user, title, animations, ListType.SHORT);
    }
}