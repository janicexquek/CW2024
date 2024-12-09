package com.example.demo.projectile;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnemyProjectileTest {

    private EnemyProjectile enemyProjectile;

    @BeforeAll
    public static void initializeJavaFX() throws InterruptedException {
        // Initialize JavaFX runtime
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    public void setUp() {
        // Initialize the EnemyProjectile with specific initial positions
        enemyProjectile = new EnemyProjectile(200.0, 300.0);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(200.0, enemyProjectile.getLayoutX(), "Initial X position should match the input");
        assertEquals(300.0, enemyProjectile.getLayoutY(), "Initial Y position should match the input");
    }

    @Test
    public void testUpdatePosition() throws InterruptedException {
        // Call updatePosition on the JavaFX Application Thread
        Platform.runLater(() -> enemyProjectile.updatePosition());
        Thread.sleep(100); // Allow time for JavaFX thread to process

        // Verify the updated X position
        assertEquals(190.0, enemyProjectile.getLayoutX(), "X position should decrease by the horizontal velocity (-10)");
    }

    @Test
    public void testUpdateActor() throws InterruptedException {
        // Call updateActor (which internally calls updatePosition) on the JavaFX Application Thread
        Platform.runLater(() -> enemyProjectile.updateActor());
        Thread.sleep(100); // Allow time for JavaFX thread to process

        // Verify the updated X position
        assertEquals(190.0, enemyProjectile.getLayoutX(), "X position should decrease by the horizontal velocity (-10) when updateActor is called");
    }

    @Test
    public void testTakeDamage() {
        // Call takeDamage to mark the projectile as destroyed
        enemyProjectile.takeDamage();

        // Verify the projectile is marked as destroyed
        assertTrue(enemyProjectile.isDestroyed(), "Projectile should be destroyed after taking damage");
    }
}
