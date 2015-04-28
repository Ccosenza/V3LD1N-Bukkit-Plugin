package com.v3ld1n.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.BlockUtil;
import com.v3ld1n.util.StringUtil;

public class EditSignCommand extends V3LD1NCommand {
    public EditSignCommand() {
        this.addUsage("set <line> <text ...>", "Set the text");
        this.addUsage("add <line> <text ...>", "Add text");
        this.addUsage("remove <line> <text ...>", "Remove text");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("v3ld1n.editsign")) {
                Player p = (Player) sender;
                if (args.length >= 3) {
                    int line;
                    try {
                        line = Integer.parseInt(args[1]);
                    } catch (Exception e) {
                        Message.EDITSIGN_INVALID_LINE.send(p);
                        return true;
                    }
                    if (line > 4 || line < 1) {
                        Message.EDITSIGN_INVALID_LINE.send(p);
                        return true;
                    }
                    Block target = p.getTargetBlock((Set<Material>) null, 100);
                    if (target.getState() instanceof Sign) {
                        String text = StringUtil.fromArray(args, 2);
                        switch (args[0].toLowerCase()) {
                        case "set":
                            BlockUtil.editSign(target, line, StringUtil.formatText(text));
                            Message.EDITSIGN_SET.aSendF(p, line, text);
                            return true;
                        case "add":
                            BlockUtil.addToSign(target, line, StringUtil.formatText(text));
                            Message.EDITSIGN_ADD.aSendF(p, text, line);
                            return true;
                        case "remove":
                            BlockUtil.removeFromSign(target, line, StringUtil.formatText(text));
                            Message.EDITSIGN_REMOVE.aSendF(p, text, line);
                            return true;
                        default:
                            this.sendUsage(sender, label, command);
                            return true;
                        }
                    }
                    Message.EDITSIGN_INVALID_BLOCK.send(p);
                    return true;
                }
                this.sendUsage(sender, label, command);
                return true;
            }
            sendPermissionMessage(sender);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}