package com.example.demo.projectile;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserProjectileTest {

    private UserProjectile userProjectile;

    @BeforeAll
    public static void initializeJavaFX() throws InterruptedException {
        // Initialize JavaFX runtime
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    public void setUp() {
        // Initialize the UserProjectile with specific initial positions
        userProjectile = new UserProjectile(100.0, 200.0);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(100.0, userProjectile.getLayoutX(), "Initial X position should match the input");
        assertEquals(200.0, userProjectile.getLayoutY(), "Initial Y position should match the input");
    }

    @Test
    public void testUpdatePosition() throws InterruptedException {
        // Call updatePosition on the JavaFX Application Thread
        Platform.runLater(() -> userProjectile.updatePosition());
        Thread.sleep(100); // Allow time for JavaFX thread to process

        // Verify the updated X position
        assertEquals(115.0, userProjectile.getLayoutX(), "X position should increase by the horizontal velocity (15)");
    }

    @Test
    public void testUpdateActor() throws InterruptedException {
        // Call updateActor (which internally calls updatePosition) on the JavaFX Application Thread
        Platform.runLater(() -> userProjectile.updateActor());
        Thread.sleep(100); // Allow time for JavaFX thread to process

        // Verify the updated X position
        assertEquals(115.0, userProjectile.getLayoutX(), "X position should increase by the horizontal velocity (15) when updateActor is called");
    }

    @Test
    public void testTakeDamage() {
        // Call takeDamage to mark the projectile as destroyed
        userProjectile.takeDamage();

        // Verify the projectile is marked as destroyed
        assertEquals(true, userProjectile.isDestroyed(), "Projectile should be destroyed after taking damage");
    }
}
