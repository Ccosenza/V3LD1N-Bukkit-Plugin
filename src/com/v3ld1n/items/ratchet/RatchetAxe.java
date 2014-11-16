package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
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
        final LivingEntity e = event.getEntity();
        if (e.getKiller() != null) {
            if (e.getKiller().getType() == EntityType.PLAYER && e instanceof Monster) {
                if (e.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
                    final Player p = e.getKiller();
                    if (this.equalsItem(p.getItemInHand())) {
                        EntityUtil.healEntity(p, p.getMaxHealth());
                        Particle.fromString(this.getStringSetting("particle1")).display(e.getEyeLocation());
                        final Particle particle = Particle.fromString(this.getStringSetting("particle2"));
                        final int effectDuration = this.getIntSetting("effect-duration");
                        for (final PotionEffect pe : p.getActivePotionEffects()) {
                            if (pe.getType().equals(effect)) {
                                p.removePotionEffect(effect);
                                Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        p.addPotionEffect(effect.createEffect(effectDuration, pe.getAmplifier() + 1));
                                        particle.setCount(25 * (pe.getAmplifier() + 1));
                                        particle.display(e.getEyeLocation());
                                    }
                                }, 1L);
                                return;
                            }
                        }
                        p.addPotionEffect(effect.createEffect(effectDuration, 0));
                        particle.display(e.getEyeLocation());
                    }
                }
            }
        }
    }
}