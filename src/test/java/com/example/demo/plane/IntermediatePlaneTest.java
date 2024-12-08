package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class IntermediatePlaneTest extends ApplicationTest {

    private IntermediatePlane intermediatePlane;

    @Override
    public void start(Stage stage) {
        // Setup the test environment
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Create MasterPlane and add it to the root
        intermediatePlane = new IntermediatePlane(700, 300);
        root.getChildren().add(intermediatePlane);
    }

    @BeforeEach
    public void setUp() {
        // Ensure proper JavaFX thread setup
        waitForFxEvents();
    }

    @Test
    public void testInitialPosition() {
        assertEquals(700, intermediatePlane.getLayoutX(), "Initial X position should be 700");
        assertEquals(300, intermediatePlane.getLayoutY(), "Initial Y position should be 300");
    }

    @Test
    public void testUpdatePosition() {
        Platform.runLater(() -> {
            // Record the initial X position
            double initialX = intermediatePlane.getLayoutX() + intermediatePlane.getTranslateX();

            // Call the method to update position
            intermediatePlane.updatePosition();

            // Record the new X position
            double updatedX = intermediatePlane.getLayoutX() + intermediatePlane.getTranslateX();

            // Assert that the X position has decreased
            assertTrue(updatedX < initialX, "X position should decrease after updatePosition");
        });

        // Wait for JavaFX thread events to complete
        waitForFxEvents();
    }

    @Test
    public void testFireProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = intermediatePlane.fireProjectile();

            // Since firing is probabilistic, we check null and valid cases
            if (projectile != null) {
                assertTrue(projectile instanceof EnemyProjectile, "Projectile should be of type EnemyProjectile");
                EnemyProjectile enemyProjectile = (EnemyProjectile) projectile;

                // Verify projectile position
                double expectedX = intermediatePlane.getLayoutX();
                double expectedY = intermediatePlane.getLayoutY() + intermediatePlane.getTranslateY() + 5;
                assertEquals(expectedX, enemyProjectile.getX(), 0.1, "Projectile X position should match expected value");
                assertEquals(expectedY, enemyProjectile.getY(), 0.1, "Projectile Y position should match expected value");
            }
        });
        waitForFxEvents();
    }

    @Test
    public void testTakeDamage() {
        Platform.runLater(() -> {
            // Initial health
            int initialHealth = intermediatePlane.getHealth();
            assertEquals(3, initialHealth, "Initial health should be 5");

            // Take damage and verify health decreases
            intermediatePlane.takeDamage();
            assertEquals(initialHealth - 1, intermediatePlane.getHealth(), "Health should decrease by 1 after taking damage");

            // Reduce health to zero
            for (int i = 1; i < initialHealth; i++) {
                intermediatePlane.takeDamage();
            }

            // Verify the plane is destroyed
            assertTrue(intermediatePlane.isDestroyed(), "MasterPlane should be destroyed when health reaches zero");
        });
        waitForFxEvents();
    }


    @Test
    public void testUpdateActor() {
        Platform.runLater(() -> {
            double initialX = intermediatePlane.getLayoutX(); // Or getTranslateX()
            System.out.println("Initial X: " + initialX);

            intermediatePlane.updateActor(); // Call updateActor

            double updatedX = intermediatePlane.getLayoutX(); // Or getTranslateX()
            System.out.println("Updated X: " + updatedX);

            assertTrue(updatedX < initialX, "X position should decrease after updateActor");
        });
        waitForFxEvents();
    }


}