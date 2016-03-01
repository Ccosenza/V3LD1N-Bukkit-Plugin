package com.v3ld1n.items.ratchet;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
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
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;

        event.setCancelled(true);

        // Hides the item's description
        player.setItemInHand(ItemUtil.hideFlags(player.getItemInHand()));

        PlayerAnimation.SWING_ARM.play(player);
        new ProjectileBuilder(Snowball.class)
            .setLaunchSounds(sounds)
            .setSpeed(1.5)
            .launch(player);
    }

    // Creates the firework effect
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectileIsValid(projectile, EntityType.SNOWBALL)) return;

        int maxRed = settings.getInt("color-max-red");
        int maxGreen = settings.getInt("color-max-green");
        int maxBlue = settings.getInt("color-max-blue");

        int red = random.nextInt(maxRed);
        int green = random.nextInt(maxGreen);
        int blue = random.nextInt(maxBlue);

        int fadeRed = random.nextInt(maxRed);
        int fadeGreen = random.nextInt(maxGreen);
        int fadeBlue = random.nextInt(maxBlue);

        Color fireworkColor = Color.fromRGB(red, green, blue);
        boolean fade = settings.getBoolean("color-fade");
        Color fadeColor = fade ? Color.fromRGB(fadeRed, fadeGreen, fadeBlue) : fireworkColor;

        Type fireworkType = Type.valueOf(settings.getString("firework-type"));
        FireworkEffect effect = FireworkEffect.builder()
                .with(fireworkType)
                .withColor(fireworkColor)
                .withFade(fadeColor)
                .withFlicker()
                .withTrail()
                .build();
        EntityUtil.displayFireworkEffect(effect, projectile.getLocation(), 1);
    }
}