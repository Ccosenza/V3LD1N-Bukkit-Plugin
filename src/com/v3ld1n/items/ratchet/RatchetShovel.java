package com.v3ld1n.items.ratchet;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
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
        Action a = event.getAction();
        if (useActions.contains(a)) {
            Player p = event.getPlayer();
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                new ProjectileBuilder(Snowball.class)
                    .setLaunchSound(this.getSoundSetting("throw-sound"))
                    .setSpeed(1.5)
                    .launch(p);
                PlayerAnimation.SWING_ARM.play(p, 64);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getDamager().getType() == EntityType.SNOWBALL) {
            Snowball snowball = (Snowball) event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                Player p = (Player) snowball.getShooter();
                if (this.equalsItem(p.getItemInHand())) {
                    if (event.getEntityType() != EntityType.PLAYER) {
                        double damage = snowball.getTicksLived() * this.getIntSetting("damage-multiplier");
                        event.setDamage(damage);
                        Message.RATCHETS_SHOVEL_DAMAGE.aSendF(p, (int) damage);
                    }
                    Particle.displayList(snowball.getLocation(), this.getStringListSetting("hit-particles"));
                }
            }
        }
    }
}