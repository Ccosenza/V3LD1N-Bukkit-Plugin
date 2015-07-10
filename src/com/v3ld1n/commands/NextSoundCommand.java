package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

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
            List<SoundTask> tasks = new ArrayList<>();
            if (args.length == 0) {
                tasks = V3LD1N.getSoundTasks();
            } else if (args.length == 1) {
                boolean containsArg = false;
                for (SoundTask task : V3LD1N.getSoundTasks()) {
                    if (task.getName().startsWith(args[0])) {
                        tasks.add(task);
                        containsArg = true;
                    }
                }
                if (!containsArg) {
                    Message.NEXTSOUND_NO_SOUND_TASKS.sendF(sender, args[0]);
                    return true;
                }
            } else {
                this.sendUsage(sender);
                return true;
            }
            runTasks(tasks, sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }

    private void runTasks(List<SoundTask> tasks, final CommandSender user) {
        for (final SoundTask task : tasks) {
            task.run();
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    String current = task.getCurrentSoundName();
                    String upper = task.getName().toUpperCase();
                    Message.NEXTSOUND_NOW_PLAYING.sendF(user, upper, current);
                }
            }, 1L);
        }
    }
}