package com.v3ld1n.items;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class WitherSword extends V3LD1NItem {
    private final PotionEffectType effect = PotionEffectType.WITHER;

    public WitherSword() {
        super("wither-sword");
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!entityIsHoldingItem(event.getDamager())) return;
        if (!(event.getEntity() instanceof Creature)) return;

        Creature creature = (Creature) event.getEntity();
        addEffect(creature);
    }

    /**
     * Adds the effect to the creature
     * @param creature the creature
     */
    private void addEffect(Creature creature) {
        int duration = settings.getInt("effect-duration");
        int level = settings.getInt("effect-level");

        creature.addPotionEffect(effect.createEffect(duration, level));
        Location eyeLocation = creature.getEyeLocation();
        displayParticles(eyeLocation);
        playSounds(eyeLocation);
    }
}