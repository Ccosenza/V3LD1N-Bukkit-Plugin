package com.v3ld1n.items.ratchet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.Message;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.PlayerUtil;

public class RatchetHoe extends V3LD1NItem {
    public RatchetHoe() {
        super("ratchets-hoe");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        int range = settings.getInt("range");
        Block target = player.getTargetBlock((Set<Material>) null, range);
        List<Material> types = new ArrayList<>();
        types.add(Material.GRASS);
        types.add(Material.MYCEL);
        types.add(Material.DIRT);

        Block blockAbove = target.getRelative(BlockFace.UP, 1);

        boolean targetIsDirt = types.contains(target.getType());
        
        if (targetIsDirt && blockAbove.getType() == Material.AIR) {
            if (PlayerUtil.canBuild(player, target.getLocation())) {
                use(player, target);
            } else {
                Message.WORLDGUARD_PERMISSION.send(player);
            }
        }
    }

    private void use(Player p, Block block) {
        PlayerAnimation.SWING_ARM.play(p);
        block.setType(Material.SOIL);

        Location blockLocation = block.getLocation();
        double x = blockLocation.getX() + 0.5;
        double y = blockLocation.getY() + 1;
        double z = blockLocation.getZ() + 0.5;
        Location effectLocation = new Location(block.getWorld(), x, y, z);

        displayParticles(effectLocation);
        playSounds(effectLocation);
    }
}