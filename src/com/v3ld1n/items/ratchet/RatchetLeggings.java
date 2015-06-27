package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.RepeatableRunnable;

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
            this.getSoundSetting("xp-sound").play(p.getLocation());
        }
    }

    @EventHandler
    public void onStatisticIncrement(PlayerStatisticIncrementEvent event) {
        if (event.getStatistic() == Statistic.JUMP) {
            final Player p = event.getPlayer();
            if (this.equalsItem(p.getInventory().getLeggings())) {
                Action action = null;
                if (p.isSprinting()) {
                    action = Action.SPRINT;
                }
                if (p.isSneaking()) {
                    action = Action.SNEAK;
                }
                if (action != null) {
                    jump(p, action);
                }
            }
        }
    }

    private void jump(final Player p, Action a) {
        Vector divide = this.getVectorSetting(a + ".divide-velocity");
        Vector velocity = this.getVectorSetting(a + ".velocity");
        p.setVelocity(p.getLocation().getDirection().divide(divide).add(velocity));
        this.getSoundSetting("sound").play(p.getLocation());
        long ticks = this.getIntSetting("trail-ticks");
        int times = this.getIntSetting("trail-times");
        RepeatableRunnable task = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, ticks, times) {
            @Override
            public void onRun() {
                if (!p.isDead()) {
                    List<String> setting = getStringListSetting("trail-particles");
                    List<Particle> trails = Particle.fromList(setting);
                    for (Particle trail : trails) {
                        trail.setSpeed(trail.getSpeed() - (random.nextFloat() / 10));
                        trail.display(p.getLocation());
                    }
                }
            }
        };
        task.run();
    }

    private enum Action {
        SPRINT,
        SNEAK;
    }
}