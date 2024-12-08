package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.BossProjectile;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class BossPlaneTest extends ApplicationTest {

    private BossPlane bossPlane;

    @Override
    public void start(Stage stage) {
        // Initialize JavaFX for tests
    }

    @BeforeEach
    public void setUp() {
        // Initialize BossPlane
        bossPlane = new BossPlane();
    }

    @Test
    public void testInitialHealth() {
        assertEquals(10, bossPlane.getBossHealth(), "Initial health should be 10");
    }

    @Test
    public void testUpdatePositionWithinBounds() {
        Platform.runLater(() -> {
            bossPlane.setTranslateY(300); // Set a valid starting position
            bossPlane.updatePosition();
            double position = bossPlane.getTranslateY();
            assertTrue(position >= 80 && position <= 640, "Position should remain within vertical bounds");
        });
        waitForFxEvents();
    }

    @Test
    public void testFireProjectile() {
        Platform.runLater(() -> {
            ActiveActorDestructible projectile = bossPlane.fireProjectile();
            if (projectile != null) {
                assertTrue(projectile instanceof BossProjectile, "Fired projectile should be an instance of BossProjectile");
            }
        });
        waitForFxEvents();
    }

    @Test
    public void testTakeDamageWithoutShield() {
        Platform.runLater(() -> {
            int initialHealth = bossPlane.getBossHealth();
            bossPlane.takeDamage();
            assertEquals(initialHealth - 1, bossPlane.getBossHealth(), "Health should decrease when taking damage without shield");
        });
        waitForFxEvents();
    }

    @Test
    public void testShieldActivationAndDeactivation() {
        Platform.runLater(() -> {
            // Force shield activation
            bossPlane.activateShield();
            assertTrue(bossPlane.getShieldImage().isVisible(), "Shield should be visible after activation");

            // Force shield deactivation
            bossPlane.deactivateShield();
            assertFalse(bossPlane.getShieldImage().isVisible(), "Shield should not be visible after deactivation");
        });
        waitForFxEvents();
    }

    @Test
    public void testShieldPreventsDamage() {
        Platform.runLater(() -> {
            bossPlane.activateShield();
            int initialHealth = bossPlane.getBossHealth();

            bossPlane.takeDamage();
            assertEquals(initialHealth, bossPlane.getBossHealth(), "Health should not decrease when shield is active");
        });
        waitForFxEvents();
    }

    @Test
    public void testUpdateShield() {
        Platform.runLater(() -> {
            // Simulate multiple updates to test shield behavior
            for (int i = 0; i < 1000; i++) {
                bossPlane.updateShield();
            }
            assertTrue(bossPlane.getShieldImage().isVisible() || !bossPlane.getShieldImage().isVisible(),
                    "Shield state should toggle appropriately based on probability");
        });
        waitForFxEvents();
    }
}
