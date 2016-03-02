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

    // Sets the arrow on fire
    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (!entityIsHoldingItem(event.getEntity())) return;

        EntityUtil.infiniteFire(event.getProjectile());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectileIsValid(projectile, EntityType.ARROW)) return;
        createFireworkEffect(projectile);
    }

    /**
     * Creates the firework effect
     * @param projectile the projectile
     */
    private void createFireworkEffect(Projectile projectile) {
        Player player = (Player) projectile.getShooter();
        PlayerData setting = PlayerData.FIREWORK_ARROWS;
        Type fireworkType;
        try {
            fireworkType = Type.valueOf(setting.getString(player));
        } catch (Exception e) {
            fireworkType = Type.BALL;
        }

        int minRed = settings.getInt("color-min.red");
        int minGreen = settings.getInt("color-min.green");
        int minBlue = settings.getInt("color-min.blue");

        int maxRed = settings.getInt("color-max.red");
        int maxGreen = settings.getInt("color-max.green");
        int maxBlue = settings.getInt("color-max.blue");

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