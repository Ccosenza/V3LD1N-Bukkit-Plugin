package com.v3ld1n.items.ratchet;

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
import com.v3ld1n.util.SoundUtil;

public class RatchetHoe extends V3LD1NItem {
    public RatchetHoe() {
        super("ratchets-hoe");
    }
    
    public void use(Player p, Block block) {
        PlayerAnimation.SWING_ARM.play(p, 50);
        block.setType(Material.SOIL);
        Location loc = new Location(block.getWorld(), block.getLocation().getX() + 0.5, block.getLocation().getY() + 1, block.getLocation().getZ() + 0.5);
        this.getParticleSetting("particle").display(loc);
        SoundUtil.playSoundString(this.getStringSetting("sound"), loc);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR) {
            if (this.equalsItem(p.getItemInHand())) {
                Block target = p.getTargetBlock(null, this.getIntSetting("range"));
                if ((target.getType() == Material.GRASS || target.getType() == Material.MYCEL || target.getType() == Material.DIRT) && target.getRelative(BlockFace.UP, 1).getType() == Material.AIR) {
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
                        p.sendMessage(Message.WORLDGUARD_PERMISSION.toString());
                    }
                }
            }
        }
    }
}