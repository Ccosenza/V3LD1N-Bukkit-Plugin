package com.v3ld1n.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.items.ratchet.RatchetBowType;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.StringUtil;

public class RatchetsBowCommand extends V3LD1NCommand {
    public RatchetsBowCommand() {
        this.addUsage("<projectile>", "Set your Ratchet's Bow projectile");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                String projectile = args[0].toUpperCase();
                List<RatchetBowType> types = Arrays.asList(RatchetBowType.values());
                boolean contains = false;
                for (RatchetBowType type : types) {
                    if (type.name().equalsIgnoreCase(projectile)) {
                        contains = true;
                    }
                }
                if (contains) {
                    RatchetBowType type = RatchetBowType.valueOf(projectile);
                    if (type == RatchetBowType.FIREBALL) {
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), null);
                    } else {
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toUpperCase());
                    }
                    type.getParticle().display(p.getEyeLocation());
                    p.sendMessage(String.format(Message.RATCHETSBOW_SET.toString(), StringUtil.fromEnum(type, true)));
                    return true;
                }
                p.sendMessage(Message.RATCHETSBOW_INVALID_PROJECTILE.toString());
                ChatUtil.sendList(p, Message.RATCHETSBOW_LIST_TITLE.toString(), types, ListType.SHORT);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}