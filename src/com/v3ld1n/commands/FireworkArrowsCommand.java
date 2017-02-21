package com.v3ld1n.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.StringUtil;

public class FireworkArrowsCommand extends V3LD1NCommand {
    public FireworkArrowsCommand() {
        this.addUsage("<firework type>", "Set your firework arrow type");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendPlayerMessage(sender);
            return true;
        }	
        Player player = (Player) sender;

        if (args.length != 1) {
            this.sendUsage(sender);
            return true;
        }

        Type type;
        try {
            type = Type.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
        	Message.get("fireworkarrows-invalid-shape").send(sender);
            sendTypeList(sender);
            return true;
        }
        setType(player, type);
        return true;
    }

    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        sendTypeList(user);
    }

    private void setType(Player player, Type type) {
        Object typeData = type == Type.BALL ? null : type.toString();
        PlayerData.FIREWORK_ARROWS.set(player, typeData);
        FireworkEffect effect = FireworkEffect.builder()
                .with(type)
                .withColor(Color.WHITE)
                .withFlicker()
                .withTrail()
                .build();
        Location loc = player.getLocation();
        loc.add(0, 5, 0);
        EntityUtil.displayFireworkEffect(effect, loc, 1);
        Message.get("fireworkarrows-set").aSendF(player, StringUtil.fromEnum(type, true));
    }

    private void sendTypeList(CommandSender user) {
        List<Type> types = Arrays.asList(Type.values());
        ChatUtil.sendList(user, Message.get("fireworkarrows-list-title").toString(), types, ListType.SHORT);
    }
}