package com.v3ld1n.commands;

import java.util.Arrays;

import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
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
                Type type = Type.valueOf(typeString);
                if (type == Type.BALL) {
                    PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), null);
                } else {
                    if (Arrays.asList(Type.values()).contains(type)) {
                        PlayerData.FIREWORK_ARROWS.set(p.getUniqueId(), args[0].toUpperCase());
                    } else {
                        p.sendMessage(Message.FIREWORKARROWS_INVALID_SHAPE.toString());
                        return true;
                    }
                }
                p.sendMessage(String.format(Message.FIREWORKARROWS_SET.toString(), StringUtil.fromEnum(type, true)));
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}