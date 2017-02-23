package com.v3ld1n.items.ratchet;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.Message;
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
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction(), event.getHand())) return;

        event.setCancelled(true);
        new ProjectileBuilder(Snowball.class)
            .setLaunchSounds(settings.getSounds("throw-sounds"))
            .setSpeed(1.5)
            .launch(player);
        PlayerAnimation.SWING_ARM.play(player);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!projectileIsValid(event.getDamager(), EntityType.SNOWBALL)) return;
        if (event.getEntityType() == EntityType.PLAYER) return;

        Snowball snowball = (Snowball) event.getDamager();
        Player player = (Player) snowball.getShooter();
        double damage = snowball.getTicksLived() * settings.getInt("damage-multiplier");
        event.setDamage(damage);
        Message.get("item-ratchetsshovel-damage").aSendF(player, (int) damage);
        Particle.displayList(settings.getParticles("hit-particles"), snowball.getLocation());
    }
}