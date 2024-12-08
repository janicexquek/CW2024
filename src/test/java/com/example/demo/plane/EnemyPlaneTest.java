package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class EnemyPlaneTest extends ApplicationTest {

    private EnemyPlane enemyPlane;

    @Override
    public void start(javafx.stage.Stage stage) {
        // Initialize JavaFX if necessary
    }

    @BeforeEach
    public void setUp() {
        // Initialize the EnemyPlane with a starting position
        enemyPlane = new EnemyPlane(800, 400); // Starting at X=800, Y=400
    }

    @Test
    public void testInitialPosition() {
        // Verify initial position
        assertEquals(800, enemyPlane.getLayoutX(), "Initial X position should be 800");
        assertEquals(400, enemyPlane.getLayoutY(), "Initial Y position should be 400");
    }

    @Test
    public void testUpdatePosition() {
        // Store the initial translateX value
        double initialTranslateX = enemyPlane.getTranslateX();

        Platform.runLater(() -> {
            enemyPlane.updatePosition(); // Move left
        });
        waitForFxEvents();

        // Assert that the position moved left (negative X direction)
        assertTrue(enemyPlane.getTranslateX() < initialTranslateX,
                "X position should decrease after updatePosition");
    }


    @Test
    public void testFireProjectile() {
        Platform.runLater(() -> {
            // Simulate firing a projectile
            ActiveActorDestructible projectile = enemyPlane.fireProjectile();
            if (projectile != null) {
                assertTrue(projectile instanceof EnemyProjectile, "Projectile should be an instance of EnemyProjectile");
                EnemyProjectile enemyProjectile = (EnemyProjectile) projectile;

                // Verify projectile position
                assertEquals(enemyPlane.getProjectileXPosition(), enemyProjectile.getX(), "Projectile X position should match");
                assertEquals(enemyPlane.getProjectileYPosition(5.0), enemyProjectile.getY(), "Projectile Y position should match");
            }
        });
        waitForFxEvents();
    }

    @Test
    public void testFireProjectileWithLowFireRate() {
        // Test that projectile firing is probabilistic
        int projectileCount = 0;
        for (int i = 0; i < 1000; i++) {
            if (enemyPlane.fireProjectile() != null) {
                projectileCount++;
            }
        }

        // Given the FIRE_RATE of 0.01, the expected count should be around 10
        assertTrue(projectileCount > 0 && projectileCount < 30,
                "Projectile count should be within expected range based on FIRE_RATE");
    }

    @Test
    public void testTakeDamage() {
        Platform.runLater(() -> {
            assertEquals(1, enemyPlane.getHealth(), "Initial health should be 1");
            enemyPlane.takeDamage();
            assertEquals(0, enemyPlane.getHealth(), "Health should decrease to 0 after taking damage");
            assertTrue(enemyPlane.isDestroyed(), "Enemy plane should be marked as destroyed when health is 0");
        });
        waitForFxEvents();
    }

    @Test
    public void testUpdateActor() {
        double initialX = enemyPlane.getTranslateX(); // Changed from getLayoutX
        Platform.runLater(() -> {
            enemyPlane.updateActor(); // This should update the position
        });
        waitForFxEvents(); // Ensure JavaFX updates are applied
        assertTrue(enemyPlane.getTranslateX() < initialX, "X position should decrease after updateActor");
    }
}
