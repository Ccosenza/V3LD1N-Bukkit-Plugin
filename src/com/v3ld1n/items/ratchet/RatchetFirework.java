package com.v3ld1n.items.ratchet;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.BlockUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.RepeatableRunnable;

public class RatchetFirework extends V3LD1NItem {
    public RatchetFirework() {
        super("ratchets-firework-rocket");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;

        event.setCancelled(true);
        Location location = player.getLocation();
        Material blockBelow = location.getBlock().getRelative(BlockFace.DOWN).getType();
        if (blockBelow == Material.AIR) return;

        fly(player);
    }

    /**
     * Makes the player fly in the direction they are facing
     * @param player the player
     */
    private void fly(final Player player) {
        final UUID uuid = player.getUniqueId();
        final String worldStartedIn = player.getWorld().getName();
        RepeatableRunnable firework = new RepeatableRunnable() {
            @Override
            public void onRun() {
                if (player.isDead()) return;
                if (!player.getWorld().getName().equals(worldStartedIn)) return;
                V3LD1N.usingRatchetFirework.add(uuid);
                Vector divisor = settings.getVector("velocity-divisor");
                player.setVelocity(player.getLocation().getDirection().divide(divisor));
                player.setFallDistance(0);
            }

            @Override
            public void onLastRun() {
                if (V3LD1N.usingRatchetFirework.contains(uuid)) {
                    V3LD1N.usingRatchetFirework.remove(uuid);
                }
            }
        };
        RepeatableRunnable particles = new RepeatableRunnable() {
            @Override
            public void onRun() {
                if (player.isDead()) return;
                if (!player.getWorld().getName().equals(worldStartedIn)) return;
                Location location = player.getLocation();
                Block block = location.getBlock();
                String setting;
                if (BlockUtil.isWater(block)) {
                    setting = "water-particles";
                } else if (BlockUtil.isLava(block)) {
                    setting = "lava-particles";
                } else {
                    displayParticles(location);
                    return;
                }
                Particle.displayList(settings.getParticles(setting), location);
            }
        };
        if (!V3LD1N.usingRatchetFirework.contains(uuid)) {
            PlayerAnimation.SWING_ARM.play(player);
            playSounds(player.getLocation());
            firework.start(0, 2, 50);
            particles.start(0, 1, 110);
        }
    }
}