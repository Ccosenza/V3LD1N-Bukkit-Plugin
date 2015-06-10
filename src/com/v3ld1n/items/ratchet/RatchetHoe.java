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

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.PlayerAnimation;

public class RatchetHoe extends V3LD1NItem {
    public RatchetHoe() {
        super("ratchets-hoe");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR) {
            if (this.equalsItem(p.getItemInHand())) {
                Block target = p.getTargetBlock((Set<Material>) null, this.getIntSetting("range"));
                List<Material> types = new ArrayList<>();
                types.add(Material.GRASS);
                types.add(Material.MYCEL);
                types.add(Material.DIRT);
                Block up = target.getRelative(BlockFace.UP, 1);
                if (types.contains(target.getType()) && up.getType() == Material.AIR) {
                    boolean playerCanBuild;
                    if (V3LD1N.getWorldGuard() != null) {
                        WorldGuardPlugin wg = V3LD1N.getWorldGuard();
                        playerCanBuild = wg.canBuild(p, target);
                    } else {
                        playerCanBuild = true;
                    }
                    if (playerCanBuild) {
                        use(p, target);
                    } else {
                        Message.WORLDGUARD_PERMISSION.send(p);
                    }
                }
            }
        }
    }
    
    private void use(Player p, Block block) {
        PlayerAnimation.SWING_ARM.play(p, 50);
        block.setType(Material.SOIL);
        Location bl = block.getLocation();
        double x = bl.getX() + 0.5;
        double y = bl.getY() + 1;
        double z = bl.getZ() + 0.5;
        Location loc = new Location(block.getWorld(), x, y, z);
        this.displayParticles(loc);
        this.getSoundSetting("sound").play(loc);
    }
}