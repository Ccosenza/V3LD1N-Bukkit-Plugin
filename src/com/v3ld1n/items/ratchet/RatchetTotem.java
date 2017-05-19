package com.v3ld1n.items.ratchet;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import com.v3ld1n.Message;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;

public class RatchetTotem extends V3LD1NItem {
    public RatchetTotem() {
        super("ratchets-totem");
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof LivingEntity)) return;

        event.setCancelled(true);
        heal(player, (LivingEntity) event.getRightClicked());
    }

    private void heal(Player player, LivingEntity entity) {
        int oldHealth = (int) entity.getHealth();
        EntityUtil.heal(entity, settings.getDouble("heal-amount"));
        int newHealth = (int) entity.getHealth();
        int maxHealth = (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        Message.get("item-ratchetstotem-heal").aSendF(player, entity.getName(), newHealth, maxHealth);
        playSounds(entity.getEyeLocation());
        if (oldHealth != newHealth) {
            displayParticles(entity.getEyeLocation());
        }
    }
}