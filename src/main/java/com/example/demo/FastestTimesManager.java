// File: FastestTimesManager.java
package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

public class FastestTimesManager {
    private static FastestTimesManager instance;
    private Map<String, Long> fastestTimes;
    private Preferences prefs;
    private static final String PREF_FASTEST_TIMES = "fastestTimes";

    // Private constructor to enforce singleton pattern
    private FastestTimesManager() {
        fastestTimes = new HashMap<>();
        prefs = Preferences.userNodeForPackage(FastestTimesManager.class);
        loadFastestTimes();
    }

    // Public method to provide access to the singleton instance
    public static synchronized FastestTimesManager getInstance() {
        if (instance == null) {
            instance = new FastestTimesManager();
        }
        return instance;
    }

    // Load fastest times from Preferences into the map
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

    // Save the current fastest times map to Preferences
    private void saveFastestTimes() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> entry : fastestTimes.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
        }
        prefs.put(PREF_FASTEST_TIMES, sb.toString());
    }

    // Retrieve the fastest time for a specific level
    public long getFastestTime(String levelName) {
        return fastestTimes.getOrDefault(levelName, Long.MAX_VALUE);
    }

    // Update the fastest time for a specific level if the new time is better
    public void updateFastestTime(String levelName, long newTime) {
        if (!fastestTimes.containsKey(levelName) || newTime < fastestTimes.get(levelName)) {
            fastestTimes.put(levelName, newTime);
            saveFastestTimes();
            System.out.println("Updated fastest time for " + levelName + ": " + newTime + " seconds");
        }
    }

    // Retrieve all fastest times (used for the scoreboard)
    public Map<String, Long> getAllFastestTimes() {
        return new HashMap<>(fastestTimes);
    }
}
