package com.example.demo.mainmenu;

/**
 * Manages the store settings for the game, including the selected plane.
 * Implements a singleton pattern to ensure a single instance.
 */
public class StoreManager {
    // Singleton instance
    private static StoreManager instance = null;

    // Default plane
    private int selectedPlaneNumber = 1;

    /**
     * Private constructor to enforce singleton pattern.
     * Optionally, load saved selection from persistent storage.
     */
    private StoreManager() {
        // Optionally, load saved selection from persistent storage
    }

    /**
     * Method to get the singleton instance.
     *
     * @return the singleton instance of StoreManager
     */
    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager();
        }
        return instance;
    }

    /**
     * Getter for selectedPlaneNumber.
     *
     * @return the currently selected plane number
     */
    public int getSelectedPlaneNumber() {
        return selectedPlaneNumber;
    }

    /**
     * Setter for selectedPlaneNumber.
     * Optionally, save the selection to persistent storage.
     *
     * @param selectedPlaneNumber the new plane number to select
     */
    public void setSelectedPlaneNumber(int selectedPlaneNumber) {
        this.selectedPlaneNumber = selectedPlaneNumber;
        // Optionally, save the selection to persistent storage
    }
}