package com.example.demo.level;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.overlay.OverlayManager;
import com.example.demo.plane.EnemyPlane;
import com.example.demo.plane.MasterPlane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class LevelThreeTest {

    private LevelThree levelThree;

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
            levelThree = new LevelThree(800, 600);
            levelThree.initializeFriendlyUnits();
            latch.countDown(); // Signal that initialization is complete
        });

        // Wait for initialization to complete
        try {
            latch.await(); // Wait until the latch is counted down
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Initialization interrupted", e);
        }
    assertNotNull(levelThree.levelView, "LevelView should be initialized");
    assertNotNull(levelThree.levelView.getDisplayManager(), "DisplayManager should be initialized");
}

    @Test
    public void testInitialization() {
        // Run assertions or logic that depends on the initialized JavaFX components
        Platform.runLater(() -> {
            assertEquals(5, levelThree.getUser().getHealth(), "Player initial health should be 5");
        });

        // Wait for assertions to complete
        waitForRunLater();
    }

    /**
     * Helper method to wait for JavaFX operations to complete.
     */
    private static void waitForRunLater() {
        try {
            Thread.sleep(500); // Adjust based on your test environment's speed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    // Helper method to run tasks on the JavaFX Application Thread
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

    @Test
    public void testActivateIntermediateWave() throws InterruptedException {
        levelThree.normalPlanesDestroyed = 10; // Simulate destroying normal planes

        runAndWait(() -> {
            levelThree.spawnEnemyUnits();
            assertTrue(levelThree.intermediateWaveActive, "Intermediate wave should activate after destroying enough normal planes");
        });
    }

    @Test
    public void testActivateMasterWave() throws InterruptedException {
        levelThree.normalPlanesDestroyed = 10; // Simulate destroying normal planes
        levelThree.intermediatePlanesDestroyed = 5; // Simulate destroying intermediate planes

        runAndWait(() -> {
            levelThree.spawnEnemyUnits();
            assertTrue(levelThree.masterWaveActive, "Master wave should activate after destroying enough intermediate planes");
        });
    }

    @Test
    public void testSpawnNormalPlanes() throws InterruptedException {
        runAndWait(() -> {
            levelThree.spawnEnemyUnits();
            int enemyCount = levelThree.getEnemyUnits().size();
            assertTrue(enemyCount > 0 && enemyCount <= 5, "Normal wave should spawn between 1 and 5 planes");
            assertTrue(levelThree.getEnemyUnits().stream().allMatch(enemy -> enemy instanceof EnemyPlane),
                    "All spawned enemies should be normal planes in the first wave");
        });
    }


    @Test
    public void testGameOver_Loss() throws InterruptedException {
        runAndWait(() -> {
            levelThree.getUser().takeDamage(); // Simulate player losing all health
            levelThree.getUser().takeDamage();
            levelThree.getUser().takeDamage();
            levelThree.getUser().takeDamage();
            levelThree.getUser().takeDamage();

            levelThree.checkIfGameOver();
            assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, levelThree.getActiveOverlay(), "Game over overlay should be active when user is destroyed");
        });
    }

    @Test
    public void testGameOver_Win() throws InterruptedException {
        runAndWait(() -> {
            levelThree.masterWaveActive = true; // Simulate master wave activation
            levelThree.spawnEnemyUnits();
            // Forcefully destroy master planes
            for (ActiveActorDestructible enemy : levelThree.getEnemyUnits()) {
                if (enemy instanceof MasterPlane) {
                    enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
                    enemy.destroy();
                }
            }
            levelThree.removeAllDestroyedActors();
            assertEquals(2, levelThree.getMasterPlanesDestroyed(), "[DEBUG] MasterPlanesDestroyed count mismatch!");
            levelThree.checkIfGameOver();
            assertEquals(OverlayManager.ActiveOverlay.WIN, levelThree.getActiveOverlay(), "Win overlay should be active after destroying all master planes");
        });
    }

    @Test
    public void testDisplayCustomInfo() throws InterruptedException {
        runAndWait(() -> {
            levelThree.updateCustomDisplay();
        });

        Thread.sleep(100); // Allow JavaFX to process updates

        String customInfo = levelThree.levelView.getDisplayManager().getCustomDisplay();
        assertTrue(customInfo.contains("Wave 1"), "Custom display should show Wave 1 info at the start");
    }

}
