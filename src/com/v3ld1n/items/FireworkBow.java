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
                firework(pr, shooter);
            }
        }
    }

    private void firework(Projectile pr, Player p) {
        Type type = Type.BALL;
        PlayerData data = PlayerData.FIREWORK_ARROWS;
        if (data.get(p.getUniqueId()) != null) {
           type = Type.valueOf(data.getString(p.getUniqueId()));
        }
        int minR = this.getIntSetting("color-min.red");
        int minG = this.getIntSetting("color-min.green");
        int minB = this.getIntSetting("color-min.blue");
        int maxR = this.getIntSetting("color-max.red");
        int maxG = this.getIntSetting("color-max.green");
        int maxB = this.getIntSetting("color-max.blue");
        int r = (int) RandomUtil.getRandomDouble(minR, maxR);
        int g = (int) RandomUtil.getRandomDouble(minG, maxG);
        int b = (int) RandomUtil.getRandomDouble(minB, maxB);
        color = color.setRed(r);
        color = color.setGreen(g);
        color = color.setBlue(b);
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