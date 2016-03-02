package com.v3ld1n.items.ratchet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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

        PlayerAnimation.SWING_ARM.play(player);
        playSounds(player.getLocation());
        fly(player);
    }

    /**
     * Makes the player fly in the direction they are facing
     * @param player the player
     */
    private void fly(final Player player) {
        final String worldStartedIn = player.getWorld().getName();
        RepeatableRunnable firework = new RepeatableRunnable(0, 2, 50) {
            @Override
            public void onRun() {
                if (player.isDead()) return;
                if (!player.getWorld().getName().equals(worldStartedIn)) return;
                Vector divisor = settings.getVector("velocity-divisor");
                player.setVelocity(player.getLocation().getDirection().divide(divisor));
                player.setFallDistance(0);
            }
        };
        RepeatableRunnable particles = new RepeatableRunnable(0, 1, 110) {
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
        firework.run();
        particles.run();
    }
}