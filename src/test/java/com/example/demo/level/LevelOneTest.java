package com.example.demo.level;

import com.example.demo.plane.UserPlane;
import com.example.demo.levelview.LevelView;
import com.example.demo.overlay.OverlayManager;
import javafx.application.Platform;
import javafx.scene.Group;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for LevelOne.
 */
public class LevelOneTest {

    private LevelOne levelOne;
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
        levelOne = new LevelOne(screenHeight, screenWidth);

        // Initialize UserPlane
        UserPlane userPlane = new UserPlane("userplane.png", LevelOne.PLAYER_INITIAL_HEALTH);
        levelOne.user = userPlane;

        // Add user to root via LevelOne's method
        levelOne.initializeFriendlyUnits();
    }

    @Test
    public void testInitializeFriendlyUnits() {
        Platform.runLater(() -> {
            assertNotNull(levelOne.getUser(), "User plane should be initialized");
            assertTrue(levelOne.getRoot().getChildren().contains(levelOne.getUser()), "User plane should be added to the root group");
        });
    }

    @Test
    public void testSpawnEnemyUnits() {
        Platform.runLater(() -> {
            int initialEnemyCount = levelOne.getCurrentNumberOfEnemies();
            levelOne.spawnEnemyUnits();
            int updatedEnemyCount = levelOne.getCurrentNumberOfEnemies();
            assertTrue(updatedEnemyCount >= initialEnemyCount, "Enemy count should increase after spawning enemy units");
        });
    }

    @Test
    public void testCheckIfGameOver_UserDestroyed() {
        Platform.runLater(() -> {
            // Simulate user destruction
            levelOne.getUser().destroy();

            // Perform the check
            levelOne.checkIfGameOver();

            // Verify that the game is over
            assertTrue(levelOne.userIsDestroyed(), "User should be marked as destroyed.");
            OverlayManager.ActiveOverlay activeOverlay = levelOne.getActiveOverlay();
            assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, activeOverlay, "Game over overlay should be active.");
        });
    }

    @Test
    public void testCheckIfGameOver_UserReachedKillTarget() {
        Platform.runLater(() -> {
            UserPlane user = levelOne.getUser();
            for (int i = 0; i < LevelOne.KILLS_TO_ADVANCE; i++) {
                user.incrementKillCount();
            }
            levelOne.checkIfGameOver();
            OverlayManager.ActiveOverlay activeOverlay = levelOne.getActiveOverlay();
            assertEquals(OverlayManager.ActiveOverlay.WIN, activeOverlay, "Win overlay should be active when kill target is reached.");
        });
    }

    @Test
    public void testUpdateCustomDisplay() {
        Platform.runLater(() -> {
            UserPlane user = levelOne.getUser();
            user.incrementKillCount();
            levelOne.updateCustomDisplay();

            // Verify that kill count in LevelView's DisplayManager is updated
            String killCountText = levelOne.levelView.getDisplayManager().getKillCountText();
            assertEquals("Kills: 1 / 10", killCountText, "Kill count should be updated correctly.");
        });
    }

    @Test
    public void testInstantiateLevelView() {
        Platform.runLater(() -> {
            LevelView levelView = levelOne.instantiateLevelView(screenWidth, screenHeight);
            assertNotNull(levelView, "LevelView should be instantiated.");
            assertEquals("LEVEL ONE", levelOne.getLevelDisplayName(), "Level display name should match.");
        });
    }

    @Test
    public void testGetNextLevelClassName() {
        Platform.runLater(() -> {
            assertEquals("com.example.demo.level.LevelTwo", levelOne.getNextLevelClassName(), "Next level class name should match.");
        });
    }

    @Test
    public void testGetLevelDisplayName() {
        assertEquals("LEVEL ONE", levelOne.getLevelDisplayName(), "The display name should be 'LEVEL ONE'.");
    }
}
