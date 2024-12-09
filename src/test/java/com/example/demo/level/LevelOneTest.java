package com.example.demo.level;

import com.example.demo.overlay.OverlayManager;
import com.example.demo.plane.EnemyPlane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class LevelOneTest {

    private LevelOne levelOne;

    @BeforeAll
    public static void initializeJavaFX() {
        // Initialize the JavaFX platform
        Platform.startup(() -> {
            // No-op: Ensures the toolkit is initialized
        });
    }

    @BeforeEach
    public void setUp() {
        CountDownLatch latch = new CountDownLatch(1); // Use CountDownLatch for synchronization
        Platform.runLater(() -> {
            levelOne = new LevelOne(800, 600);
            levelOne.initializeFriendlyUnits();
            latch.countDown(); // Signal that initialization is complete
        });

        // Wait for initialization to complete
        try {
            latch.await(); // Wait until the latch is counted down
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Initialization interrupted", e);
        }

        assertNotNull(levelOne.levelView, "LevelView should be initialized");
    }

    @Test
    public void testInitialization() {
        try {
            runAndWait(() -> {
                assertEquals(5, levelOne.getUser().getHealth(), "Player initial health should be 5");
                assertEquals(0, levelOne.getUser().getNumberOfKills(), "Player initial kills should be 0");
            });
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testSpawnEnemyUnits() {
        try {
            runAndWait(() -> {
                levelOne.spawnEnemyUnits();
                int enemyCount = levelOne.getEnemyUnits().size();
                assertTrue(enemyCount > 0 && enemyCount <= LevelOne.TOTAL_ENEMIES, "Enemy count should be between 1 and " + LevelOne.TOTAL_ENEMIES);
                assertTrue(levelOne.getEnemyUnits().stream().allMatch(enemy -> enemy instanceof EnemyPlane), "All spawned enemies should be of type EnemyPlane");
            });
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testGameOver_Loss() {
        try {
            runAndWait(() -> {
                levelOne.getUser().takeDamage(); // Simulate player losing all health
                levelOne.getUser().takeDamage();
                levelOne.getUser().takeDamage();
                levelOne.getUser().takeDamage();
                levelOne.getUser().takeDamage(); // Total 5 damage = health depletion

                levelOne.checkIfGameOver();
                assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, levelOne.getActiveOverlay(), "Game over overlay should be active when user is destroyed");
            });
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testGameOver_Win() {
        try {
            runAndWait(() -> {
                // Increment the kill count one by one to reach the kill target
                for (int i = 0; i < LevelOne.KILLS_TO_ADVANCE; i++) {
                    levelOne.getUser().incrementKillCount();
                }

                levelOne.checkIfGameOver();
                assertEquals(OverlayManager.ActiveOverlay.WIN, levelOne.getActiveOverlay(), "Win overlay should be active after user reaches the kill target");
            });
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }
    }

    @Test
    public void testDisplayKillCount() {
        try {
            runAndWait(() -> {
                for (int i = 0; i < LevelOne.KILLS_TO_ADVANCE; i++) {
                    levelOne.getUser().incrementKillCount();
                    levelOne.updateCustomDisplay();
                }
            });

            Thread.sleep(500); // Allow time for the UI to reflect the changes

            String killDisplay = levelOne.levelView.getDisplayManager().getCustomDisplay();
            assertTrue(killDisplay.contains("10"), "Custom display should show the current kill count");
        } catch (InterruptedException e) {
            fail("Test interrupted: " + e.getMessage());
        }
    }


    /**
     * Helper method to run tasks on the JavaFX Application Thread.
     */
    private void runAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Exception> exceptionRef = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Exception e) {
                exceptionRef.set(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await(); // Wait until the latch is counted down
        if (exceptionRef.get() != null) {
            throw new RuntimeException("Exception in JavaFX thread", exceptionRef.get());
        }
    }
}
