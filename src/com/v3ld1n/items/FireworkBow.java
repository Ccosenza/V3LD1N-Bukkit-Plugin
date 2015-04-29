package com.v3ld1n.items;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.v3ld1n.PlayerData;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.RandomUtil;

public class FireworkBow extends V3LD1NItem {
    private Color color = Color.AQUA;

    public FireworkBow() {
        super("firework-bow");
    }

    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            final Player p = (Player) event.getEntity();
            if (this.equalsItem(p.getItemInHand())) {
                event.getProjectile().setFireTicks(Integer.MAX_VALUE);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        ProjectileSource source = pr.getShooter();
        if (pr.getType() == EntityType.ARROW && source instanceof Player) {
            Player shooter = (Player) source;
            if (this.equalsItem(shooter.getItemInHand())) {
                Type type = Type.BALL;
                if (PlayerData.FIREWORK_ARROWS.get(shooter.getUniqueId()) != null) {
                   type = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(shooter.getUniqueId()));
                }
                int minRed = this.getIntSetting("color-min.red");
                int minGreen = this.getIntSetting("color-min.green");
                int minBlue = this.getIntSetting("color-min.blue");
                int maxRed = this.getIntSetting("color-max.red");
                int maxGreen = this.getIntSetting("color-max.green");
                int maxBlue = this.getIntSetting("color-max.blue");
                int red = (int) RandomUtil.getRandomDouble(minRed, maxRed);
                int green = (int) RandomUtil.getRandomDouble(minGreen, maxGreen);
                int blue = (int) RandomUtil.getRandomDouble(minBlue, maxBlue);
                color = color.setRed(red);
                color = color.setGreen(green);
                color = color.setBlue(blue);
                FireworkEffect effect = FireworkEffect.builder()
                        .with(type)
                        .withColor(color)
                        .withFade(color)
                        .withFlicker()
                        .withTrail()
                        .build();
                EntityUtil.detonateFireworkProjectile(pr, effect, pr.getLocation());
            }
        }
    }
}