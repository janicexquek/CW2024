package com.example.demo.projectile;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class AllyProjectileTest {

    private AllyProjectile allyProjectile;

    @BeforeAll
    public static void initializeJavaFX() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    public void setUp() {
        allyProjectile = new AllyProjectile(100.0); // Use mock or real implementation
    }

    @Test
    public void testInitialPosition() {
        assertEquals(180.0, allyProjectile.getLayoutX(), "Initial X position should be 180");
        assertEquals(100.0, allyProjectile.getLayoutY(), "Initial Y position should match the input");
    }

    @Test
    public void testUpdatePosition() throws InterruptedException {
        Platform.runLater(() -> {
            System.out.println("Initial X: " + allyProjectile.getLayoutX());
            allyProjectile.updatePosition();
            System.out.println("Updated X: " + allyProjectile.getLayoutX());
        });
        Thread.sleep(100);
        assertEquals(195.0, allyProjectile.getLayoutX(), "X position should increase by the horizontal velocity (15)");
    }

    @Test
    public void testTakeDamage() {
        assertFalse(allyProjectile.isDestroyed(), "Projectile should not be destroyed initially");
        allyProjectile.takeDamage();
        assertTrue(allyProjectile.isDestroyed(), "Projectile should be marked as destroyed after taking damage");
    }
}
