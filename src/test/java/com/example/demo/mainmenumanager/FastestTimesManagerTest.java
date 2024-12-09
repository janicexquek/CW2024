package com.example.demo.mainmenumanager;

import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FastestTimesManagerTest {

    private FastestTimesManager fastestTimesManager;

    @BeforeEach
    void setup() {
        fastestTimesManager = FastestTimesManager.getInstance();
        fastestTimesManager.clearAllFastestTimes(); // Clear state before each test
    }

    @Test
    void testUpdateFastestTime() {
        String levelName = "LEVEL_ONE";

        // Update the fastest time
        fastestTimesManager.updateFastestTime(levelName, 100L);

        // Assert that the time was updated
        assertEquals(100L, fastestTimesManager.getFastestTime(levelName), "Fastest time should be updated to 100L");

        // Update with a better time
        fastestTimesManager.updateFastestTime(levelName, 90L);

        // Assert that the time was updated to the better time
        assertEquals(90L, fastestTimesManager.getFastestTime(levelName), "Fastest time should be updated to 90L");

        // Try to update with a worse time
        fastestTimesManager.updateFastestTime(levelName, 120L);

        // Assert that the time was not updated
        assertEquals(90L, fastestTimesManager.getFastestTime(levelName), "Fastest time should remain 90L");
    }

    @Test
    void testGetFastestTimeForNonexistentLevel() {
        String levelName = "NON_EXISTENT_LEVEL";

        // Assert that the default value is returned for a non-existent level
        assertEquals(Long.MAX_VALUE, fastestTimesManager.getFastestTime(levelName),
                "Fastest time for a non-existent level should be Long.MAX_VALUE");
    }

    @Test
    void testGetAllFastestTimes() {
        // Add some test data
        fastestTimesManager.updateFastestTime("LEVEL_ONE", 100L);
        fastestTimesManager.updateFastestTime("LEVEL_TWO", 200L);

        Map<String, Long> allFastestTimes = fastestTimesManager.getAllFastestTimes();

        // Assert that all levels are included in the map
        assertEquals(2, allFastestTimes.size(), "There should be two levels in the fastest times map");
        assertEquals(100L, allFastestTimes.get("LEVEL_ONE"), "Fastest time for LEVEL_ONE should be 100L");
        assertEquals(200L, allFastestTimes.get("LEVEL_TWO"), "Fastest time for LEVEL_TWO should be 200L");
    }

    @Test
    void testSaveAndLoadFastestTimes() {
        // Add some test data
        fastestTimesManager.updateFastestTime("LEVEL_ONE", 150L);
        fastestTimesManager.updateFastestTime("LEVEL_TWO", 250L);

        // Simulate restarting the application by recreating the instance
        FastestTimesManager newInstance = FastestTimesManager.getInstance();

        // Assert that the data was persisted and reloaded correctly
        assertEquals(150L, newInstance.getFastestTime("LEVEL_ONE"), "Fastest time for LEVEL_ONE should be 150L");
        assertEquals(250L, newInstance.getFastestTime("LEVEL_TWO"), "Fastest time for LEVEL_TWO should be 250L");
    }

    @AfterAll
    void tearDown() {
        // Clear preferences after tests to avoid polluting state
        fastestTimesManager.getAllFastestTimes().clear();
    }
}
