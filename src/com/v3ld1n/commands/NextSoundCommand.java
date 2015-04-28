package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.tasks.SoundTask;

public class NextSoundCommand extends V3LD1NCommand {
    public NextSoundCommand() {
        this.addUsage("", "Play the next sound for all sound tasks");
        this.addUsage("<sound task>", "Play the next sound for a sound task");
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            if (args.length == 0) {
                for (final SoundTask task : V3LD1N.getSoundTasks()) {
                    task.run();
                    Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            Message.NEXTSOUND_NOW_PLAYING.sendF(sender, task.getName().toUpperCase(), task.getCurrentSoundName());
                        }
                    }, 1L);
                }
                return true;
            } else if (args.length == 1) {
                for (final SoundTask task : V3LD1N.getSoundTasks()) {
                    if (task.getName().contains(args[0])) {
                        task.run();
                        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                            @Override
                            public void run() {
                                Message.NEXTSOUND_NOW_PLAYING.aSendF(sender, task.getName().toUpperCase(), task.getCurrentSoundName());
                            }
                        }, 1L);
                        return true;
                    }
                }
                Message.NEXTSOUND_NO_SOUND_TASKS.sendF(sender, args[0]);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}