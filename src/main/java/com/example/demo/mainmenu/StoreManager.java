package com.example.demo.mainmenu;

public class StoreManager {
    // Singleton instance
    private static StoreManager instance = null;

    // Default plane
    private String selectedPlane = "userplane.png";

    // Private constructor to enforce singleton pattern
    private StoreManager() {
        // Optionally, load saved selection from persistent storage
    }

    // Method to get the singleton instance
    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
        }
        return instance;
    }

    // Getter for selectedPlane
    public String getSelectedPlane() {
        return selectedPlane;
    }

    // Setter for selectedPlane
    public void setSelectedPlane(String selectedPlane) {
        this.selectedPlane = selectedPlane;
        // Optionally, save the selection to persistent storage
    }
}
