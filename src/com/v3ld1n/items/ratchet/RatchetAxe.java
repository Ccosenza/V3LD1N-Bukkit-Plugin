package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;

public class RatchetAxe extends V3LD1NItem {
    private final PotionEffectType effect = PotionEffectType.ABSORPTION;

    public RatchetAxe() {
        super("ratchets-axe");
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        if (!(entity instanceof Monster)) return;
        if (!entityIsHoldingItem(entity.getKiller())) return;
        if (entity.getLastDamageCause().getCause() != DamageCause.ENTITY_ATTACK) return;

        final Player killer = entity.getKiller();
        addEffect(killer, entity);
    }

    /**
     * Heals the player and spawns particles at the enemy's location
     * @param player the player
     * @param enemy the enemy
     */
    private void addEffect(final Player player, final LivingEntity enemy) {
        EntityUtil.heal(player, player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        displayParticles(enemy.getEyeLocation());
        increaseEffectLevel(player, enemy);
    }
    
    /**
     * Increases the level of the player's potion effect
     * @param player the player
     * @param enemy the enemy
     */
    private void increaseEffectLevel(final Player player, final LivingEntity enemy) {
        final int effectDuration = settings.getInt("effect-duration");
        final int amplifierLimit = settings.getInt("effect-level-limit") - 1;
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().equals(effect) && pe.getAmplifier() < amplifierLimit) {
                player.removePotionEffect(effect);
                Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        int newAmplifier;
                        if (pe.getAmplifier() < amplifierLimit) {
                            newAmplifier = pe.getAmplifier() + 1;
                        } else {
                            newAmplifier = amplifierLimit;
                        }
                        player.addPotionEffect(effect.createEffect(effectDuration, newAmplifier));
                        List<Particle> particles = Particle.fromList(settings.getStringList("effect-particles"));
                        for (Particle particle : particles) {
                            particle.setCount(25 * (pe.getAmplifier() + 1));
                            particle.display(enemy.getEyeLocation());
                        }
                    }
                }, 1L);
                return;
            }
        }
        player.addPotionEffect(effect.createEffect(effectDuration, 0));
    }
}