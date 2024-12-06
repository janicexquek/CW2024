package com.example.demo.styles;

public class TimeFormatter {
    /**
     * Formats time from seconds to MM:SS.
     *
     * @param totalSeconds the total seconds to format
     * @return the formatted time string
     */
    public static String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
