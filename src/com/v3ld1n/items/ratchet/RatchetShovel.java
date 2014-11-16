package com.v3ld1n.items.ratchet;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.ProjectileBuilder;

public class RatchetShovel extends V3LD1NItem {
    public RatchetShovel() {
        super("ratchets-shovel");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                new ProjectileBuilder()
                    .withType(Snowball.class)
                    .withLaunchSound(this.getStringSetting("throw-sound"))
                    .launch(p, 1.5);
                PlayerAnimation.SWING_ARM.play(p, 64);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.SNOWBALL) {
            Snowball snowball = (Snowball) event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                Player p = (Player) snowball.getShooter();
                if (this.equalsItem(p.getItemInHand())) {
                    if (event.getEntityType() != EntityType.PLAYER) {
                        double damage = snowball.getTicksLived() * this.getIntSetting("damage-multiplier");
                        event.setDamage(damage);
                        /*
                            TODO add action bar message saying "&6Ratchet's Shovel&e: &b%damage% &edamage"
                         */
                    }
                    Particle.fromString(this.getStringSetting("hit-particle")).display(snowball.getLocation());
                }
            }
        }
    }
}