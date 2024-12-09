package com.example.demo.level;

import com.example.demo.plane.BossPlane;
import com.example.demo.plane.UserPlane;
import com.example.demo.levelview.LevelViewLevelTwo;
import com.example.demo.overlay.OverlayManager;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for LevelTwo.
 */
public class LevelTwoTest {

    private LevelTwo levelTwo;
    private Group root;
    private final double screenWidth = 800;
    private final double screenHeight = 600;

    @BeforeAll
    public static void setupJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit
        final Object lock = new Object();
        Platform.startup(() -> {
            synchronized (lock) {
                lock.notify();
            }
        });
        synchronized (lock) {
            lock.wait();
        }
    }

    @BeforeEach
    public void setUp() {
        root = new Group();
        levelTwo = new LevelTwo(screenHeight, screenWidth);

        // Initialize UserPlane
        UserPlane userPlane = new UserPlane("userplane.png", LevelTwo.PLAYER_INITIAL_HEALTH);
        levelTwo.user = userPlane;

        // Add user to root via LevelTwo's method
        levelTwo.initializeFriendlyUnits();
    }

    @Test
    public void testInitializeFriendlyUnits() {
        Platform.runLater(() -> {
            assertNotNull(levelTwo.getUser(), "User plane should be initialized");
            assertTrue(levelTwo.getRoot().getChildren().contains(levelTwo.getUser()), "User plane should be added to the root group");
        });
    }

    @Test
    public void testSpawnEnemyUnits() {
        Platform.runLater(() -> {
            int initialEnemyCount = levelTwo.getCurrentNumberOfEnemies();
            levelTwo.spawnEnemyUnits();
            int updatedEnemyCount = levelTwo.getCurrentNumberOfEnemies();
            assertTrue(updatedEnemyCount >= initialEnemyCount, "Enemy count should increase after spawning enemy units");
        });
    }

    @Test
    public void testCheckIfGameOver_UserDestroyed() {
        Platform.runLater(() -> {
            // Simulate user destruction
            levelTwo.getUser().destroy();

            // Perform the check
            levelTwo.checkIfGameOver();

            // Verify that the game is over
            assertTrue(levelTwo.userIsDestroyed(), "User should be marked as destroyed.");
            OverlayManager.ActiveOverlay activeOverlay = levelTwo.getActiveOverlay();
            assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, activeOverlay, "Game over overlay should be active.");
        });
    }

    @Test
    public void testCheckIfGameOver_BossDestroyed() {
        Platform.runLater(() -> {
            // Simulate boss destruction
            levelTwo.bossPlane.destroy();

            // Perform the check
            levelTwo.checkIfGameOver();

            // Verify that the game is won
            assertTrue(levelTwo.bossPlane.isDestroyed(), "Boss should be marked as destroyed.");
            OverlayManager.ActiveOverlay activeOverlay = levelTwo.getActiveOverlay();
            assertEquals(OverlayManager.ActiveOverlay.WIN, activeOverlay, "Win overlay should be active.");
        });
    }

    @Test
    public void testUpdateCustomDisplay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Simulate boss taking 3 damage points
                for (int i = 0; i < 3; i++) {
                    levelTwo.bossPlane.takeDamage();
                }
                levelTwo.updateCustomDisplay();

                // Verify that boss health in LevelViewLevelTwo is updated
                LevelViewLevelTwo levelView = (LevelViewLevelTwo) levelTwo.getLevelView();
                String bossHealthText = levelView.getBossHealthText();
                assertEquals("Boss Health: 7", bossHealthText, "Boss health should be updated correctly.");
            } finally {
                latch.countDown();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS), "JavaFX operations should complete within timeout");
    }


    @Test
    public void testInstantiateLevelView() {
        Platform.runLater(() -> {
            LevelViewLevelTwo levelView = (LevelViewLevelTwo) levelTwo.instantiateLevelView(screenWidth, screenHeight);
            assertNotNull(levelView, "LevelViewLevelTwo should be instantiated.");
            assertEquals("LEVEL TWO", levelTwo.getLevelDisplayName(), "Level display name should match.");
        });
    }

    @Test
    public void testGetNextLevelClassName() {
        Platform.runLater(() -> {
            assertEquals("com.example.demo.level.LevelThree", levelTwo.getNextLevelClassName(), "Next level class name should match.");
        });
    }

    @Test
    public void testGetLevelDisplayName() {
        assertEquals("LEVEL TWO", levelTwo.getLevelDisplayName(), "The display name should be 'LEVEL TWO'.");
    }

    @Test
    public void testSpawnBossPlaneOnce() {
        Platform.runLater(() -> {
            // Initially, no enemies
            int initialEnemyCount = levelTwo.getCurrentNumberOfEnemies();
            levelTwo.spawnEnemyUnits();
            int updatedEnemyCount = levelTwo.getCurrentNumberOfEnemies();
            assertEquals(initialEnemyCount + 1, updatedEnemyCount, "Boss plane should be spawned once.");

            // Attempt to spawn again
            levelTwo.spawnEnemyUnits();
            int finalEnemyCount = levelTwo.getCurrentNumberOfEnemies();
            assertEquals(initialEnemyCount + 1, finalEnemyCount, "Boss plane should not be spawned again if already present.");
        });
    }

    @Test
    public void testBossShieldVisibilityOnSpawn() {
        Platform.runLater(() -> {
            levelTwo.spawnEnemyUnits();
            BossPlane boss = levelTwo.bossPlane;
            assertTrue(levelTwo.getRoot().getChildren().contains(boss.getShieldImage()), "Boss shield image should be added to the root group.");
            assertFalse(boss.getShieldImage().isVisible(), "Boss shield should initially be invisible.");
        });
    }
}
