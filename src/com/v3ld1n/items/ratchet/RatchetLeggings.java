package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.RepeatableRunnable;
import com.v3ld1n.util.Sound;

public class RatchetLeggings extends V3LD1NItem {
    public RatchetLeggings() {
        super("ratchets-leggings");
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        Player p = event.getPlayer();
        if (this.equalsItem(p.getInventory().getLeggings())) {
            event.setAmount((int) (event.getAmount() * this.getDoubleSetting("xp-multiplier")));
            this.getParticleSetting("xp-particle").display(p.getLocation());
            Sound.fromString(this.getStringSetting("xp-sound")).play(p.getLocation());
        }
    }

    @EventHandler
    public void onStatisticIncrement(PlayerStatisticIncrementEvent event) {
        if (event.getStatistic() == Statistic.JUMP) {
            final Player p = event.getPlayer();
            if (this.equalsItem(p.getInventory().getLeggings())) {
                String action = null;
                if (p.isSprinting()) {
                    action = "sprinting";
                }
                if (p.isSneaking()) {
                    action = "sneaking";
                }
                if (action != null) {
                    p.setVelocity(p.getLocation().getDirection().divide(this.getVectorSetting(action + ".divide-velocity")).add(this.getVectorSetting(action + ".velocity")));
                    RepeatableRunnable trailTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, this.getIntSetting("trail-ticks"), this.getIntSetting("trail-times")) {
                        @Override
                        public void onRun() {
                            if (!p.isDead()) {
                                Particle trail = getParticleSetting("trail-particle");
                                trail.setSpeed(trail.getSpeed() - (random.nextFloat() / 10));
                                trail.display(p.getLocation());
                            }
                        }
                    };
                    trailTask.run();
                }
            }
        }
    }
}