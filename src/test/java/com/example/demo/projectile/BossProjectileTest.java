package com.example.demo.projectile;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class BossProjectileTest {

    private BossProjectile bossProjectile;

    @BeforeAll
    public static void initializeJavaFX() throws InterruptedException {
        // Ensure JavaFX runtime is initialized
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    public void setUp() {
        // Initialize the boss projectile before each test
        bossProjectile = new BossProjectile(200.0); // Example Y position
    }

    @Test
    public void testInitialPosition() {
        // Test the initial position of the projectile
        assertEquals(950.0, bossProjectile.getLayoutX(), "Initial X position should be 950");
        assertEquals(200.0, bossProjectile.getLayoutY(), "Initial Y position should match the input");
    }

    @Test
    public void testUpdatePosition() throws InterruptedException {
        // Test position updates
        Platform.runLater(() -> {
            System.out.println("Initial X: " + bossProjectile.getLayoutX());
            bossProjectile.updatePosition();
            System.out.println("Updated X: " + bossProjectile.getLayoutX());
        });

        Thread.sleep(100); // Allow time for the JavaFX Application Thread
        assertEquals(935.0, bossProjectile.getLayoutX(), "X position should decrease by the horizontal velocity (-15)");
    }

    @Test
    public void testUpdateActor() throws InterruptedException {
        // Test the updateActor method (should call updatePosition)
        Platform.runLater(() -> bossProjectile.updateActor());
        Thread.sleep(100); // Allow time for the JavaFX Application Thread
        assertEquals(935.0, bossProjectile.getLayoutX(), "X position should decrease by the horizontal velocity (-15) when updateActor is called");
    }

}
