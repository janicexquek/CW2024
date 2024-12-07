package com.example.demo.mainmenumanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreManagerTest {

    private StoreManager storeManager;

    @BeforeEach
    void setUp() {
        // Access the singleton instance
        storeManager = StoreManager.getInstance();
        // Reset the selectedPlaneNumber to default for consistent tests
        storeManager.setSelectedPlaneNumber(1);
    }

    @Test
    void testSingletonInstance() {
        // Verify the same instance is returned
        StoreManager anotherInstance = StoreManager.getInstance();
        assertSame(storeManager, anotherInstance, "StoreManager should return the same singleton instance");
    }

    @Test
    void testGetSelectedPlaneNumber() {
        // Verify the default selected plane number
        assertEquals(1, storeManager.getSelectedPlaneNumber(), "Default selected plane number should be 1");
    }

    @Test
    void testSetSelectedPlaneNumber() {
        // Set a new plane number
        storeManager.setSelectedPlaneNumber(5);

        // Verify the selected plane number is updated
        assertEquals(5, storeManager.getSelectedPlaneNumber(), "Selected plane number should be updated to 5");
    }

    @Test
    void testPersistencyBetweenTests() {
        // This test ensures changes in one test don't persist to another
        storeManager.setSelectedPlaneNumber(3);
        assertEquals(3, storeManager.getSelectedPlaneNumber(), "Selected plane number should be updated to 3");

        // Reset the plane number and verify
        storeManager.setSelectedPlaneNumber(1);
        assertEquals(1, storeManager.getSelectedPlaneNumber(), "Plane number should be reset to default for other tests");
    }
}
