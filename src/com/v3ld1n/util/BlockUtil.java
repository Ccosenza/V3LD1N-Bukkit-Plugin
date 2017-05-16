package com.v3ld1n.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public final class BlockUtil {
    private BlockUtil() {
    }

    /**
     * Changes the text on a sign
     * @param block the sign to edit
     * @param line the line to edit
     * @param text the new text
     */
    public static void editSign(Block block, int line, String text) {
        if (block != null) {
            if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
                Sign sign = (Sign) block.getState();
                if (line > 0 && line < 5) {
                    sign.setLine(line - 1, text);
                    sign.update();
                }
            }
        }
    }

    /**
     * Adds text to a sign
     * @param block the sign to add to
     * @param line the line to add to
     * @param text the text to add
     */
    public static void addToSign(Block block, int line, String text) {
        if (block != null) {
            if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
                Sign sign = (Sign) block.getState();
                if (line > 0 && line < 5) {
                    sign.setLine(line - 1, sign.getLine(line - 1) + text);
                    sign.update();
                }
            }
        }
    }

    /**
     * Removes text from a sign
     * @param block the sig to remove from
     * @param line the line to remove from
     * @param text the text to remove
     */
    public static void removeFromSign(Block block, int line, String text) {
        if (block != null) {
            if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
                Sign sign = (Sign) block.getState();
                if (line > 0 && line < 5) {
                    sign.setLine(line - 1, sign.getLine(line - 1).replaceAll(text, ""));
                    sign.update();
                }
            }
        }
    }
}