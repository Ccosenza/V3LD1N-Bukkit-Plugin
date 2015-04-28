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
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                String typeString = args[0].toUpperCase();
                List<Type> types = Arrays.asList(Type.values());
                boolean contains = false;
                for (Type type : types) {
                    if (type.name().equalsIgnoreCase(typeString)) {
                        contains = true;
                    }
                }
                if (contains) {
                    Type type = Type.valueOf(typeString);
                    if (type == Type.BALL) {
                        PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), null);
                    } else {
                        PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    }
                    FireworkEffect effect = FireworkEffect.builder()
                            .with(type)
                            .withColor(Color.WHITE)
                            .withFlicker()
                            .withTrail()
                            .build();
                    Location loc = p.getLocation();
                    loc.add(0, 5, 0);
                    EntityUtil.displayFireworkEffect(effect, loc, 2);
                    Message.FIREWORKARROWS_SET.sendF(p, StringUtil.fromEnum(type, true));
                    return true;
                }
                Message.FIREWORKARROWS_INVALID_SHAPE.send(p);
                ChatUtil.sendList(p, Message.FIREWORKARROWS_LIST_TITLE.toString(), types, ListType.SHORT);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}