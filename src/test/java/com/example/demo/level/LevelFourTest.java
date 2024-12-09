package com.example.demo.level;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.overlay.OverlayManager;
import com.example.demo.plane.EnemyPlane;
import com.example.demo.plane.IntermediatePlane;
import com.example.demo.plane.UserPlane;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class LevelFourTest {

    private LevelFour levelFour;

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
            levelFour = new LevelFour(800, 600);
            levelFour.initializeFriendlyUnits();
            latch.countDown(); // Signal that initialization is complete
        });

        // Wait for initialization to complete
        try {
            latch.await(); // Wait until the latch is counted down
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Initialization interrupted", e);
        }
    }

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
    public void testInitialization() {
        // Test that LevelFour is initialized correctly
        Platform.runLater(() -> {
            assertNotNull(levelFour.getUser(), "User plane should be initialized");
            assertEquals(5, levelFour.getUser().getHealth(), "User plane initial health should be 5");
        });
    }

    @Test
    public void testSpawnEnemyUnits() throws InterruptedException {
        runAndWait(() -> {
            levelFour.spawnEnemyUnits();
            int totalEnemies = levelFour.getEnemyUnits().size();
            assertTrue(totalEnemies <= 7, "Total enemies should not exceed 7");
        });
    }

    @Test
    public void testShieldActivation() throws InterruptedException {
        runAndWait(() -> {
            levelFour.setRandomSupplier(() -> 0.4); // Force shield activation (probability < 0.5)
            levelFour.decideAndActivateAbility();

            assertTrue(levelFour.getUser().isShieldActive(), "Shield should be activated");
        });
    }

    @Test
    public void testAllyPlaneActivation() throws InterruptedException {
        runAndWait(() -> {
            levelFour.setRandomSupplier(() -> 0.6); // Force ally plane activation (probability >= 0.5)
            levelFour.decideAndActivateAbility();

            boolean allyPlaneExists = levelFour.getFriendlyUnits().stream()
                    .anyMatch(unit -> unit instanceof com.example.demo.plane.AllyPlane);

            assertTrue(allyPlaneExists, "Ally plane should be spawned");
        });
    }


    @Test
    public void testGameOver_Win() throws InterruptedException {
        runAndWait(() -> {
            // Simulate destroying the required planes
            for (int i = 0; i < 15; i++) {
                ActiveActorDestructible enemy = new EnemyPlane(800, 100);
                enemy.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
                enemy.destroy();
                levelFour.addEnemyUnit(enemy);
            }
            for (int i = 0; i < 8; i++) {
                ActiveActorDestructible intermediate = new IntermediatePlane(800, 150);
                intermediate.setDestroyedBy(ActiveActorDestructible.DestroyedBy.USER_PROJECTILE);
                intermediate.destroy();
                levelFour.addEnemyUnit(intermediate);
            }

            levelFour.removeAllDestroyedActors();
            levelFour.checkIfGameOver();

            assertEquals(OverlayManager.ActiveOverlay.WIN, levelFour.getActiveOverlay(), "Win overlay should be active after destroying all required planes");
        });
    }

    @Test
    public void testGameOver_Loss() throws InterruptedException {
        runAndWait(() -> {
            // Simulate user plane being destroyed
            UserPlane userPlane = levelFour.getUser();
            for (int i = 0; i < 5; i++) {
                userPlane.takeDamage();
            }
            levelFour.checkIfGameOver();

            assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, levelFour.getActiveOverlay(), "Game over overlay should be active when user is destroyed");
        });
    }

    @Test
    public void testDisplayCustomInfo() throws InterruptedException {
        runAndWait(() -> {
            levelFour.updateCustomDisplay();
        });

        Thread.sleep(100); // Allow JavaFX to process updates

        String customInfo = levelFour.levelView.getDisplayManager().getCustomDisplay();
        assertTrue(customInfo.contains("Normal Planes"), "Custom display should include info about normal planes");
        assertTrue(customInfo.contains("Intermediate Planes"), "Custom display should include info about intermediate planes");
    }
}
