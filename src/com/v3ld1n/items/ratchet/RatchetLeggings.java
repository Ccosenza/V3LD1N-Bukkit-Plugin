package com.v3ld1n.items.ratchet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.SoundUtil;

public class RatchetLeggings extends V3LD1NItem {
    public RatchetLeggings() {
        super("ratchets-leggings");
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        Player p = event.getPlayer();
        if (this.equalsItem(p.getInventory().getLeggings())) {
            event.setAmount((int) (event.getAmount() * this.getDoubleSetting("xp-multiplier")));
            Particle.fromString(this.getStringSetting("particle")).display(p.getLocation());
            SoundUtil.playSoundString(this.getStringSetting("sound"), p.getLocation());
        }
    }
}