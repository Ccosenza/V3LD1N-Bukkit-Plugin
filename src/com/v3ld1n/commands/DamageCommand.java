package com.v3ld1n.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class DamageCommand extends V3LD1NCommand {
    public DamageCommand() {
        this.addUsage("<amount>", "Damage yourself");
        this.addUsage("<amount> <player>", "Damage a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.damage")) {
            int l = args.length;
            if (l == 1 || l == 2) {
                double damageAmount;
                try {
                    damageAmount = Double.parseDouble(args[0]);
                } catch (IllegalArgumentException e) {
                    this.sendUsage(sender, label, command);
                    return true;
                }
                Player p;
                if (l == 1 && sender instanceof Player) {
                    p = (Player) sender;
                } else if (l == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
                    p = PlayerUtil.getOnlinePlayer(args[1]);
                } else {
                    sendInvalidPlayerMessage(sender);
                    return true;
                }
                GameMode gm = p.getGameMode();
                if (gm != GameMode.CREATIVE && gm != GameMode.SPECTATOR) {
                    p.damage(damageAmount);
                    boolean damagedSelf = p.getName().equals(sender.getName());
                    Message message = damagedSelf ? Message.DAMAGE_DAMAGE_SELF : Message.DAMAGE_DAMAGE;
                    message.aSendF(sender, args[0], p.getName());
                    return true;
                }
                Message.DAMAGE_INVULNERABLE.sendF(sender, StringUtil.fromEnum(gm, true));
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}