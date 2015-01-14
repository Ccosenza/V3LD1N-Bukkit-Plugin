package com.v3ld1n.commands;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.PlayerData;
import com.v3ld1n.items.ratchet.RatchetBowType;
import com.v3ld1n.util.StringUtil;

public class RatchetsBowCommand extends V3LD1NCommand {
    public RatchetsBowCommand() {
        this.addUsage("<projectile>", "Set your Ratchet's Bow projectile");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getEyeLocation();
            if (args.length == 1) {
                String projectile = args[0].toLowerCase();
                RatchetBowType type = RatchetBowType.valueOf(projectile);
                if (type == RatchetBowType.FIREBALL) {
                    PlayerData.RATCHETS_BOW.set(p.getUniqueId(), null);
                } else {
                    if (Arrays.asList(RatchetBowType.values()).contains(type)) {
                        PlayerData.RATCHETS_BOW.set(p.getUniqueId(), args[0].toUpperCase());
                    } else {
                        p.sendMessage(Message.RATCHETSBOW_INVALID_PROJECTILE.toString());
                        return true;
                    }
                }
                type.getParticle().display(loc);
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