package com.example.demo.plane;

import com.example.demo.projectile.AllyProjectile;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class AllyPlaneTest extends ApplicationTest {

    private AllyPlane allyPlane;
    private AtomicBoolean projectileAdded;
    private AtomicBoolean planeDeactivated;

    @Override
    public void start(Stage stage) {
        // This initializes the JavaFX runtime for tests
    }

    @BeforeEach
    public void setUp() {
        projectileAdded = new AtomicBoolean(false);
        planeDeactivated = new AtomicBoolean(false);

        // Initialize AllyPlane with mocked callbacks
        allyPlane = new AllyPlane(
                projectile -> {
                    if (projectile instanceof AllyProjectile) {
                        projectileAdded.set(true);
                    }
                },
                () -> planeDeactivated.set(true)
        );
    }

    @Test
    public void testInitialHealth() {
        assertEquals(10, allyPlane.getHealth(), "Initial health should be 10");
    }

    @Test
    public void testUpdatePositionWithinBounds() {
        Platform.runLater(() -> {
            allyPlane.setTranslateY(300); // Set a valid starting position
            allyPlane.updatePosition();  // Trigger position update
            double position = allyPlane.getTranslateY();

            assertTrue(position >= 80 && position <= 580, "Position should remain within vertical bounds");
        });
        waitForFxEvents();
    }

    @Test
    public void testFiringProjectile() {
        Platform.runLater(() -> {
            // Simulate multiple updates to increase firing chances
            for (int i = 0; i < 100; i++) {
                allyPlane.fireProjectile();
            }
            assertTrue(projectileAdded.get(), "Projectile should be added when firing");
        });
        waitForFxEvents();
    }

    @Test
    public void testTakeDamage() {
        Platform.runLater(() -> {
            // Simulate taking damage
            allyPlane.takeDamage();
            assertEquals(9, allyPlane.getHealth(), "Health should decrease after taking damage");
        });
        waitForFxEvents();
    }

    @Test
    public void testPlaneDeactivation() {
        Platform.runLater(() -> {
            // Simulate taking damage until health is zero
            for (int i = 0; i < 10; i++) {
                allyPlane.takeDamage();
            }
            assertTrue(planeDeactivated.get(), "Plane should be deactivated when health reaches zero");
            assertTrue(allyPlane.isDestroyed(), "Plane should be marked as destroyed");
        });
        waitForFxEvents();
    }

    @Test
    public void testRandomMovementPattern() {
        Platform.runLater(() -> {
            // Collect movement samples to verify randomness
            int[] moves = new int[10];
            for (int i = 0; i < 10; i++) {
                moves[i] = (int) allyPlane.getTranslateY();
                allyPlane.updatePosition();
            }

            // Check that movement samples are not all the same
            boolean isRandom = false;
            for (int i = 1; i < moves.length; i++) {
                if (moves[i] != moves[i - 1]) {
                    isRandom = true;
                    break;
                }
            }
            assertTrue(isRandom, "Movement pattern should be randomized");
        });
        waitForFxEvents();
    }
}
