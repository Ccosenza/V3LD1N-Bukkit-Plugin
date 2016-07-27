package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.util.Vector;

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
        Player player = event.getPlayer();
        if (!equalsItem(player.getInventory().getLeggings())) return;

        double xpMultiplier = settings.getDouble("xp-multiplier");
        double newXp = event.getAmount() * xpMultiplier;

        event.setAmount((int) newXp);

        Location location = player.getLocation();
        Particle.displayList(settings.getParticles("xp-particles"), location);
        Sound.playList(settings.getSounds("xp-sounds"), location);
    }

    @EventHandler
    public void onStatisticIncrement(PlayerStatisticIncrementEvent event) {
        if (event.getStatistic() != Statistic.JUMP) return;
        Player player = event.getPlayer();
        if (!equalsItem(player.getInventory().getLeggings())) return;
        Action action = null;
        if (player.isSprinting()) {
            action = Action.SPRINT;
        }
        if (player.isSneaking()) {
            action = Action.SNEAK;
        }
        if (action != null) {
            jump(player, action);
        }
    }

    private void jump(final Player player, Action action) {
        Vector divisor = settings.getVector(action + ".divide-velocity");
        Vector velocity = settings.getVector(action + ".velocity");

        player.setVelocity(player.getLocation().getDirection().divide(divisor).add(velocity));
        playSounds(player.getLocation());

        long ticks = settings.getInt("trail-ticks");
        int times = settings.getInt("trail-times");
        RepeatableRunnable trail = new RepeatableRunnable() {
            @Override
            public void onRun() {
                if (player.isDead()) return;
                List<Particle> trails = settings.getParticles("trail-particles");

                // Adds a random speed to the particles
                for (Particle particle : trails) {
                    particle.setSpeed(particle.getSpeed() - random.nextFloat() / 10);
                    particle.display(player.getLocation());
                }
            }
        };
        trail.start(0, ticks, times);
    }

    private enum Action {
        SPRINT,
        SNEAK;
    }
}