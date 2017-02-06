package com.v3ld1n.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;

public class TimeUtil {
    private TimeUtil() {
    }

    /**
     * Returns the current time in milliseconds
     * @return the current time
     */
    public static long getTime() {
        return System.currentTimeMillis();
    }

    /**
     * Formats time
     * @param time the time in milliseconds
     * @param timeFormat the time format
     * @return the formatted time
     */
    public static String format(long time, String timeFormat) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }

    /**
     * Formats a date
     * @param time the time in milliseconds
     * @return the formatted date
     */
    public static String formatDate(long time) {
        return format(time, "MMMM d, YYYY");
    }

    /**
     * Formats time (without the date)
     * @param time the time in milliseconds
     * @return the formatted time
     */
    public static String formatTime(long time) {
        return format(time, "h:mm:ss a");
    }

    /**
     * Formats an amount of time
     * @param seconds the time in seconds
     * @return the formatted time
     */
    public static String fromSeconds(long seconds) {
        long hour = seconds / 3600;
        long minute = ((seconds / 60) % 60);
        long second = seconds % 60;
        ChatColor gold = ChatColor.GOLD;
        ChatColor red = ChatColor.RED;
        StringBuilder sb = new StringBuilder();
        sb.append(hour != 0 ? "" + red + hour + gold + "hr " : "");
        sb.append(minute != 0 ? "" + red + minute + gold + "min " : "");
        sb.append("" + red + second + gold + "sec ");
        String sbs = sb.toString();
        return sbs.substring(0, sbs.length() - 1);
    }

    /**
     * Converts ticks to milliseconds
     * @param ticks the time in ticks
     * @return the time as milliseconds
     */
    public static long ticksToMillis(long ticks) {
        long milliseconds = ticks * 50;
        return milliseconds;
    }
}