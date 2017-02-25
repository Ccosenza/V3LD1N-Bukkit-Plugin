package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.StringUtil;

public class PlayerListCommand extends V3LD1NCommand {
    public PlayerListCommand() {
        this.addUsage("set <header> <footer>", "Set the header and footer");
        this.addUsage("reset", "Reset the header and footer");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;

        if (args.length == 0) {
            this.sendUsage(sender);
            return true;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            set(args[1], args[2], sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            reset(sender);
        }
        return true;
    }

    private void set(String header, String footer, CommandSender user) {
        String headerFormat = StringUtil.formatText(header.replaceAll("_", " "));
        String footerFormat = StringUtil.formatText(footer.replaceAll("_", " "));
        ConfigUtil.setPlayerListHeaderFooter(headerFormat, footerFormat);
        Message.get("playerlist-set").aSendF(user, headerFormat, footerFormat);
    }

    private void reset(CommandSender user) {
        ConfigUtil.setPlayerListHeaderFooter("{text:\"\"}", "{text:\"\"}");
        Message.get("playerlist-reset").aSend(user);
    }
}