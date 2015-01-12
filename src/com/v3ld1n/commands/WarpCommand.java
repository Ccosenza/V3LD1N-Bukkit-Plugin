package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.SoundUtil;

public class WarpCommand implements Listener {
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player p = event.getPlayer();
        String message = event.getMessage();
        String warpName = message.substring(1, message.length());
        Warp warp = null;
        for (Warp warpObject : V3LD1N.getWarps()) {
            if (warpObject.getName().equalsIgnoreCase(warpName)) {
                warp = warpObject;
            }
        }
        if (warp != null) {
            event.setCancelled(true);
            Bukkit.getServer().dispatchCommand(p, "warp " + warpName);
            for (Particle particle : warp.getParticles()) {
                particle.display(p.getLocation(), p);
            }
            for (String sound : warp.getSounds()) {
                SoundUtil.playSoundString(sound, p.getLocation());
            }
        }
    }
}