package com.v3ld1n.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;
import com.v3ld1n.util.TimeUtil;

public class NamesCommand extends V3LD1NCommand {
    public NamesCommand() {
        this.addUsage("<player name>", "Show information about a player's names");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String playerName = args[0];
            String uuid = PlayerUtil.getUuid(playerName, false);
            String previousUrl = "https://api.mojang.com/user/profiles/" + uuid + "/names";
            String currentUrl = "https://api.mojang.com/users/profiles/minecraft/" + playerName + "?at=1422921600";
            JsonElement currentElement = StringUtil.readJsonFromUrl(currentUrl);
            if (currentElement != null) {
            	Message.get("names-border").send(sender);
                String currentName = currentElement.getAsJsonObject().get("name").toString().replaceAll("\"", "");
                Message.get("names-current").sendF(sender, playerName, currentName);
            } else {
                sendInvalidPlayerMessage(sender);
                return true;
            }
            JsonElement namesElement = StringUtil.readJsonFromUrl(previousUrl);
            JsonArray array = namesElement.getAsJsonArray();
            if (array.size() > 1) {
                HashMap<String, Long> previousNames = new HashMap<>();
                for (JsonElement jsonElement : array) {
                    try {
                        String name = jsonElement.getAsJsonObject().get("name").toString().replaceAll("\"", "");
                        long changedToAt = jsonElement.getAsJsonObject().get("changedToAt").getAsLong();
                        previousNames.put(name, changedToAt);
                    } catch (Exception e) {
                    }
                }
                String originalName = array.get(0).getAsJsonObject().get("name").toString().replaceAll("\"", "");
                Message.get("names-original").sendF(sender, originalName);
                for (String name : previousNames.keySet()) {
                    long time = previousNames.get(name);
                    String fDate = TimeUtil.formatDate(time);
                    String fTime = TimeUtil.formatTime(time);
                    Message.get("names-changed").sendF(sender, name, fDate, fTime);
                }
            } else {
            	Message.get("names-no-changes").send(sender);
            }
            Message.get("names-border").send(sender);
        } else {
            this.sendUsage(sender);
        }
        return true;
    }
}