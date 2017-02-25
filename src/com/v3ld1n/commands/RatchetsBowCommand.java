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
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            this.sendUsage(sender);
            return true;
        }

        set(player, args[0].toUpperCase());
        return true;
    }

    private void set(Player player, String projectile) {
        try {
            RatchetBowType type = RatchetBowType.valueOf(projectile);
            if (type == RatchetBowType.FIREBALL) {
                PlayerData.RATCHETS_BOW.set(player, null);
            } else {
                PlayerData.RATCHETS_BOW.set(player, projectile);
            }

            type.getParticle().display(player.getEyeLocation());
            Message.get("ratchetsbow-set").aSendF(player, StringUtil.fromEnum(type, true));
        } catch (Exception e) {
            Message.get("ratchetsbow-invalid-projectile").send(player);
            this.sendUsage(player);
        }
    }

    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        List<RatchetBowType> types = Arrays.asList(RatchetBowType.values());
        ChatUtil.sendList(user, Message.get("ratchetsbow-list-title").toString(), types, ListType.SHORT);
    }
}