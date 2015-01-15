package com.v3ld1n.util;

public final class RandomUtil {
    private RandomUtil() {
    }

    /**
     * Returns a random double in a range
     * @param start the minimum value
     * @param end the maximum value
     * @return a random double in the range
     */
    public static double getRandomDouble(double start, double end) {
        return start + Math.random() * (end - start);  
    }
}