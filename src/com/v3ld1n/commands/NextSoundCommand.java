package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.tasks.SoundTask;
import com.v3ld1n.util.ChatUtil;

public class NextSoundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                for (final SoundTask task : V3LD1N.getSoundTasks()) {
                    task.run();
                    Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            ChatUtil.sendMessage(sender, String.format(Message.NEXTSOUND_NOW_PLAYING.toString(), task.getName().toUpperCase(), task.getCurrentSoundName()), 2);
                        }
                    }, 1L);
                    return true;
                }
            } else {
                for (final SoundTask task : V3LD1N.getSoundTasks()) {
                    if (task.getName().contains(args[0])) {
                        task.run();
                        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                ChatUtil.sendMessage(sender, String.format(Message.NEXTSOUND_NOW_PLAYING.toString(), task.getName().toUpperCase(), task.getCurrentSoundName()), 2);
                            }
                        }, 1L);
                    } else {
                        sender.sendMessage(String.format(Message.NEXTSOUND_NO_SOUND_PLAYERS.toString(), args[0]));
                        return true;
                    }
                }
            }
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}