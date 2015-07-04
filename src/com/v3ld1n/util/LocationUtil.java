package com.v3ld1n.util;

import org.bukkit.Location;

public final class LocationUtil {
    private LocationUtil() {
    }

    /**
     * Returns whether a location is in an area
     * @param loc the location
     * @param start the area starting location
     * @param end the area end location
     * @return whether the location is in the area
     */
    public static boolean isInArea(Location loc, Location start, Location end) {
        if (!loc.getWorld().getName().equals(start.getWorld().getName())) {
            return false;
        }
        boolean inX = loc.getX() >= start.getX() && loc.getX() <= end.getX();
        boolean inY = loc.getY() >= start.getY() && loc.getY() <= end.getY();
        boolean inZ = loc.getZ() >= start.getZ() && loc.getZ() <= end.getZ();
        return inX && inY && inZ;
    }
}