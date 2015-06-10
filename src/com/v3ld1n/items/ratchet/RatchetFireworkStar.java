package com.v3ld1n.items.ratchet;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
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
import com.v3ld1n.util.ItemUtil;
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
        if (useActions.contains(a)) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                p.setItemInHand(ItemUtil.hideFlags(p.getItemInHand()));
                PlayerAnimation.SWING_ARM.play(p, 25);
                new ProjectileBuilder()
                    .withType(Snowball.class)
                    .withLaunchSound(this.getSoundSetting("sound"))
                    .launch(p, 1.5);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        boolean shooterIsPlayer = event.getEntity().getShooter() instanceof Player;
        if (event.getEntityType() == EntityType.SNOWBALL && shooterIsPlayer) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                int red = random.nextInt(this.getIntSetting("color-max-red"));
                int green = random.nextInt(this.getIntSetting("color-max-green"));
                int blue = random.nextInt(this.getIntSetting("color-max-blue"));
                int fadeR = random.nextInt(this.getIntSetting("color-max-red"));
                int fadeG = random.nextInt(this.getIntSetting("color-max-green"));
                int fadeB = random.nextInt(this.getIntSetting("color-max-blue"));
                Color color = Color.fromRGB(red, green, blue);
                Color fade = this.getBooleanSetting("color-fade") ? Color.fromRGB(fadeR, fadeG, fadeB) : color;
                Type type = Type.valueOf(this.getStringSetting("firework-type"));
                FireworkEffect effect = FireworkEffect.builder()
                        .with(type)
                        .withColor(color)
                        .withFade(fade)
                        .withFlicker()
                        .withTrail()
                        .build();
                EntityUtil.displayFireworkEffect(effect, pr.getLocation(), 1);
            }
        }
    }
}