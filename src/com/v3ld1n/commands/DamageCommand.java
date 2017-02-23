package com.v3ld1n.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class DamageCommand extends V3LD1NCommand {
    private final List<GameMode> INVINCIBLE_MODES = Arrays.asList(GameMode.CREATIVE, GameMode.ADVENTURE);
    
    public DamageCommand() {
        this.addUsage("<amount>", "Damage yourself");
        this.addUsage("<amount> <player>", "Damage a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.damage")) return true;

        if (args.length != 1 && args.length != 2) {
            this.sendUsage(sender);
            return true;
        }

        double damageAmount;
        try {
            damageAmount = Double.parseDouble(args[0]);
        } catch (IllegalArgumentException e) {
            this.sendUsage(sender);
            return true;
        }

        if (damageAmount < 0) {
        	this.sendUsage(sender);
        	return true;
        }

        Player player;
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        } else if (args.length == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
            player = PlayerUtil.getOnlinePlayer(args[1]);
        } else {
            sendInvalidPlayerMessage(sender);
            return true;
        }

        damage(sender, player, damageAmount);
        return true;
    }

    // Damages the player
    private void damage(CommandSender sender, Player player, double damageAmount) {
        if (INVINCIBLE_MODES.contains(player.getGameMode())) {
        	Message.get("damage-invulnerable").sendF(sender, StringUtil.fromEnum(player.getGameMode(), true));
            return;
        }
        player.damage(damageAmount);
        boolean damagedSelf = player.getName().equals(sender.getName());
        Message message = damagedSelf ? Message.get("damage-damage") : Message.get("damage-damage-other");
        message.aSendF(sender, damageAmount, player.getName());
    }
}