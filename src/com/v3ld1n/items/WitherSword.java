package com.v3ld1n.items;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class WitherSword extends V3LD1NItem {
    private final PotionEffectType effect = PotionEffectType.WITHER;

    public WitherSword() {
        super("wither-sword");
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER && event.getEntity() instanceof Creature) {
            Player p = (Player) event.getDamager();
            Creature e = (Creature) event.getEntity();
            if (this.equalsItem(p.getItemInHand())) {
                e.addPotionEffect(effect.createEffect(this.getIntSetting("effect-duration"), this.getIntSetting("effect-level")));
                Particle.fromString(this.getStringSetting("particle")).display(e.getEyeLocation());
                Sound.fromString(this.getStringSetting("sound")).play(e.getEyeLocation());
            }
        }
    }
}