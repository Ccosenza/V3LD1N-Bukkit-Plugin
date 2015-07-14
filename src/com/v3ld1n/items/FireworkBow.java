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

import com.v3ld1n.PlayerData;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.RandomUtil;

public class FireworkBow extends V3LD1NItem {
    Color fireworkColor = Color.AQUA;

    public FireworkBow() {
        super("firework-bow");
    }

    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (this.equalsItem(player.getItemInHand())) {
            event.getProjectile().setFireTicks(Integer.MAX_VALUE);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectileIsValid(projectile, EntityType.ARROW)) {
            return;
        }
        Player shooter = (Player) projectile.getShooter();
        createFireworkEffect(projectile, shooter);
    }

    private void createFireworkEffect(Projectile projectile, Player player) {
        PlayerData setting = PlayerData.FIREWORK_ARROWS;
        Type fireworkType;
        try {
            fireworkType = Type.valueOf(setting.getString(player.getUniqueId()));
        } catch (Exception e) {
            fireworkType = Type.BALL;
        }
        int minRed = this.getIntSetting("color-min.red");
        int minGreen = this.getIntSetting("color-min.green");
        int minBlue = this.getIntSetting("color-min.blue");
        int maxRed = this.getIntSetting("color-max.red");
        int maxGreen = this.getIntSetting("color-max.green");
        int maxBlue = this.getIntSetting("color-max.blue");
        int red = RandomUtil.getRandomInt(minRed, maxRed);
        int green = RandomUtil.getRandomInt(minGreen, maxGreen);
        int blue = RandomUtil.getRandomInt(minBlue, maxBlue);
        fireworkColor = fireworkColor.setRed(red);
        fireworkColor = fireworkColor.setGreen(green);
        fireworkColor = fireworkColor.setBlue(blue);
        FireworkEffect effect = FireworkEffect.builder()
                .with(fireworkType)
                .withColor(fireworkColor)
                .withFade(fireworkColor)
                .withFlicker()
                .withTrail()
                .build();
        EntityUtil.detonateFireworkProjectile(projectile, effect, projectile.getLocation());
    }
}