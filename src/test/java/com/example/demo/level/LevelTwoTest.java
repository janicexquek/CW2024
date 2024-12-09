package com.example.demo.level;

import com.example.demo.plane.BossPlane;
import com.example.demo.plane.UserPlane;
import com.example.demo.levelview.LevelViewLevelTwo;
import com.example.demo.overlay.OverlayManager;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Refactored JUnit test class for LevelTwo.
 */
public class LevelTwoTest {

    private LevelTwo levelTwo;

    @BeforeAll
    public static void setupJavaFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {
            // No-op: Ensures JavaFX toolkit is initialized
        });
    }

    @BeforeEach
    public void setUp() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            levelTwo = new LevelTwo(800, 600);
            levelTwo.initializeFriendlyUnits();
            latch.countDown();
        });

        try {
            latch.await(); // Wait for initialization to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Initialization interrupted", e);
        }

        assertNotNull(levelTwo.levelView, "LevelView should be initialized");
    }

    @Test
    public void testInitializeFriendlyUnits() throws InterruptedException {
        runAndWait(() -> {
            assertNotNull(levelTwo.getUser(), "User plane should be initialized.");
            assertTrue(levelTwo.getRoot().getChildren().contains(levelTwo.getUser()), "User plane should be added to the root group.");
        });
    }

    @Test
    public void testSpawnEnemyUnits() throws InterruptedException {
        runAndWait(() -> {
            levelTwo.spawnEnemyUnits();
            int enemyCount = levelTwo.getEnemyUnits().size();
            assertTrue(enemyCount > 0, "At least one enemy should be spawned.");
            assertTrue(levelTwo.getEnemyUnits().stream().allMatch(enemy -> enemy instanceof BossPlane || enemy instanceof UserPlane),
                    "All spawned units should be either BossPlane or UserPlane.");
        });
    }

    @Test
    public void testGameOver_Loss() throws InterruptedException {
        runAndWait(() -> {
            // Simulate user losing all health
            for (int i = 0; i < LevelTwo.PLAYER_INITIAL_HEALTH; i++) {
                levelTwo.getUser().takeDamage();
            }

            levelTwo.checkIfGameOver();
            assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, levelTwo.getActiveOverlay(), "Game over overlay should be active when user is destroyed.");
        });
    }

    @Test
    public void testGameOver_Win() throws InterruptedException {
        runAndWait(() -> {
            // Simulate boss destruction
            BossPlane boss = levelTwo.bossPlane;
            assertNotNull(boss, "Boss plane should be initialized.");
            boss.destroy();

            levelTwo.checkIfGameOver();
            assertEquals(OverlayManager.ActiveOverlay.WIN, levelTwo.getActiveOverlay(), "Win overlay should be active when the boss is destroyed.");
        });
    }

    @Test
    public void testUpdateCustomDisplay_BossHealth() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // Simulate boss damage and update display
                for (int i = 0; i < 3; i++) {
                    levelTwo.bossPlane.takeDamage();
                    levelTwo.updateCustomDisplay();
                    System.out.println("Boss Health after damage: " + levelTwo.bossPlane.getBossHealth());
                }
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS), "UI update did not complete within the timeout.");

        Platform.runLater(() -> {
            LevelViewLevelTwo levelView = (LevelViewLevelTwo) levelTwo.getLevelView();
            String bossHealthText = levelView.getDisplayManager().getBossHealthText();
            assertEquals("Boss Health: 7", bossHealthText, "Boss health should be updated to reflect damage.");
        });
    }



    @Test
    public void testSpawnBossPlaneOnce() throws InterruptedException {
        runAndWait(() -> {
            int initialEnemyCount = levelTwo.getEnemyUnits().size();

            levelTwo.spawnEnemyUnits();
            int updatedEnemyCount = levelTwo.getEnemyUnits().size();
            assertEquals(initialEnemyCount + 1, updatedEnemyCount, "Boss plane should be spawned once.");

            levelTwo.spawnEnemyUnits();
            int finalEnemyCount = levelTwo.getEnemyUnits().size();
            assertEquals(initialEnemyCount + 1, finalEnemyCount, "Boss plane should not be spawned again if already present.");
        });
    }

    @Test
    public void testBossShieldVisibilityOnSpawn() throws InterruptedException {
        runAndWait(() -> {
            levelTwo.spawnEnemyUnits();

            BossPlane boss = levelTwo.bossPlane;
            assertNotNull(boss, "Boss plane should be initialized.");
            assertTrue(levelTwo.getRoot().getChildren().contains(boss.getShieldImage()), "Boss shield image should be added to the root group.");
            assertFalse(boss.getShieldImage().isVisible(), "Boss shield should initially be invisible.");
        });
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
