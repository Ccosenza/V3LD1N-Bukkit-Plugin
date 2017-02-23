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
        if (sendPermissionMessage(sender, "v3ld1n.editsign")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        if (args.length < 3) {
            this.sendUsage(sender);
            return true;
        }

        if (!StringUtil.isInteger(args[1])) {
            Message.get("editsign-invalid-line").send(player);
            return true;
        }
        int line = Integer.parseInt(args[1]);

        if (line > 4 || line < 1) {
            Message.get("editsign-invalid-line").send(player);
            return true;
        }

        Block target = player.getTargetBlock((Set<Material>) null, 100);
        if (!(target.getState() instanceof Sign)) {
            Message.get("editsign-not-sign").send(player);
            return true;
        }

        String text = StringUtil.fromArray(args, 2);
        try {
            EditSignType editType = EditSignType.valueOf(args[0].toUpperCase());
            edit(target, line, editType, text);
            editType.getMessage().aSendF(player, text, line);
        } catch (Exception e) {
            this.sendUsage(sender);
        }
        return true;
    }

    // Edits the sign
    private void edit(Block sign, int line, EditSignType editType, String text) {
        switch (editType) {
        case SET:
            BlockUtil.editSign(sign, line, StringUtil.formatText(text));
            break;
        case ADD:
            BlockUtil.addToSign(sign, line, StringUtil.formatText(text));
            break;
        case REMOVE:
            BlockUtil.removeFromSign(sign, line, StringUtil.formatText(text));
            break;
        }
    }
}