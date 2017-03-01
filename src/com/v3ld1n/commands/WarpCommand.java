package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class WarpCommand implements Listener {
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String command = event.getMessage();
        Warp warp = V3LD1N.getWarp(command.substring(1, command.length()));

        if (warp != null) {
            event.setCancelled(true);
            warp(player, warp);
        }
    }

    private void warp(Player player, Warp warp) {
        Bukkit.getServer().dispatchCommand(player, "warp " + warp.getName());

        for (Particle particle : warp.getParticles()) {
            particle.displayToPlayer(player.getLocation(), player);
        }
        for (Sound sound : warp.getSounds()) {
            sound.playToPlayer(player.getLocation(), player);
        }
    }
}