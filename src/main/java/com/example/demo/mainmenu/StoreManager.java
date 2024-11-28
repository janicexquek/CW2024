package com.example.demo.mainmenu;

public class StoreManager {
    // Singleton instance
    private static StoreManager instance = null;

    // Default plane
    private int selectedPlaneNumber = 1;

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
    public int getSelectedPlaneNumber() {
        return selectedPlaneNumber;
    }

    // Setter for selectedPlane
    public void setSelectedPlaneNumber(int selectedPlaneNumber) {
        this.selectedPlaneNumber = selectedPlaneNumber;
        // Optionally, save the selection to persistent storage
    }
}
