package com.v3ld1n.items.ratchet;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.ProjectileBuilder;

public class RatchetFireworkStar extends V3LD1NItem {
    public RatchetFireworkStar() {
        super("ratchets-firework-star");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                PlayerAnimation.SWING_ARM.play(p, 25);
                new ProjectileBuilder()
                    .withType(Snowball.class)
                    .withLaunchSound(this.getStringSetting("sound"))
                    .launch(p, 1.5);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        if (event.getEntityType() == EntityType.SNOWBALL && event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                int red = random.nextInt(this.getIntSetting("color-max-red"));
                int green = random.nextInt(this.getIntSetting("color-max-green"));
                int blue = random.nextInt(this.getIntSetting("color-max-blue"));
                int fadeRed = random.nextInt(this.getIntSetting("color-max-red"));
                int fadeGreen = random.nextInt(this.getIntSetting("color-max-green"));
                int fadeBlue = random.nextInt(this.getIntSetting("color-max-blue"));
                Color color = Color.fromRGB(red, green, blue);
                Color fade = this.getBooleanSetting("color-fade") ? Color.fromRGB(fadeRed, fadeGreen, fadeBlue) : color;
                Type type = Type.valueOf(this.getStringSetting("firework-type"));
                EntityUtil.displayFireworkEffect(pr.getLocation(), type, color, fade);
            }
        }
    }
}