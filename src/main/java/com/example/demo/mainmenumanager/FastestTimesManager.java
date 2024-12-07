// File: FastestTimesManager.java
package com.example.demo.mainmenumanager;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Manages the fastest times for game levels.
 * Implements a singleton pattern to ensure a single instance.
 */
public class FastestTimesManager {
    private static FastestTimesManager instance;
    private Map<String, Long> fastestTimes;
    private Preferences prefs;
    private static final String PREF_FASTEST_TIMES = "fastestTimes";

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the fastest times map and loads data from preferences.
     */
    private FastestTimesManager() {
        fastestTimes = new HashMap<>();
        prefs = Preferences.userNodeForPackage(FastestTimesManager.class);
        loadFastestTimes();
    }

    /**
     * Provides access to the singleton instance.
     *
     * @return the singleton instance of FastestTimesManager
     */
    public static synchronized FastestTimesManager getInstance() {
        if (instance == null) {
            instance = new FastestTimesManager();
        }
        return instance;
    }

    /**
     * Loads the fastest times from Preferences into the map.
     */
    private void loadFastestTimes() {
        String storedTimes = prefs.get(PREF_FASTEST_TIMES, "");
        if (!storedTimes.isEmpty()) {
            String[] entries = storedTimes.split(";");
            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String[] keyValue = entry.split("=");
                    if (keyValue.length == 2) {
                        String levelName = keyValue[0];
                        try {
                            long time = Long.parseLong(keyValue[1]);
                            fastestTimes.put(levelName, time);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid time format for level: " + levelName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Saves the current fastest times map to Preferences.
     */
    private void saveFastestTimes() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : fastestTimes.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        prefs.put(PREF_FASTEST_TIMES, sb.toString());
    }

    /**
     * Retrieves the fastest time for a specific level.
     *
     * @param levelName the name of the level
     * @return the fastest time for the level, or Long.MAX_VALUE if not found
     */
    public long getFastestTime(String levelName) {
        return fastestTimes.getOrDefault(levelName, Long.MAX_VALUE);
    }

    /**
     * Updates the fastest time for a specific level if the new time is better.
     *
     * @param levelName the name of the level
     * @param newTime the new time to be considered
     */
    public void updateFastestTime(String levelName, long newTime) {
        if (!fastestTimes.containsKey(levelName) || newTime < fastestTimes.get(levelName)) {
            fastestTimes.put(levelName, newTime);
            saveFastestTimes();
        }
    }

    /**
     * Retrieves all fastest times (used for the scoreboard).
     *
     * @return a map of all fastest times
     */
    public Map<String, Long> getAllFastestTimes() {
        return new HashMap<>(fastestTimes);
    }
}