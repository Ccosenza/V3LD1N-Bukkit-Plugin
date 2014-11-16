package com.v3ld1n.util;

public class RandomUtil {
    private RandomUtil() {
    }

    public static double getRandomDouble(double start, double end) {
        return start + Math.random() * (end - start);  
    }
}