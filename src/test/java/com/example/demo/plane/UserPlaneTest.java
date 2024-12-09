package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;
import com.example.demo.shield.UserShieldImage;
import com.example.demo.projectile.UserProjectile;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class UserPlaneTest extends ApplicationTest {

    private UserPlane userPlane;

    @Override
    public void start(javafx.stage.Stage stage) {
        // Initialize the UserPlane with test parameters
        userPlane = new UserPlane("userplane.png", 10);
    }

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            // Add UserPlane to a Group or Parent
            Group root = new Group();
            Scene scene = new Scene(root, 800, 600);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            userPlane = new UserPlane("userplane.png", 5); // Initialize UserPlane with an image and health
            root.getChildren().add(userPlane);

            // Set a starting position for UserPlane
            userPlane.setLayoutX(50); // Arbitrary X position
            userPlane.setLayoutY(300); // Arbitrary Y position
        });
        waitForFxEvents();
    }


    @Test
    public void testMoveUp() {
        Platform.runLater(() -> {
            double initialY = userPlane.getTranslateY();
            userPlane.moveUp();
            userPlane.updatePosition();
            assertTrue(userPlane.getTranslateY() < initialY, "UserPlane should move up when moveUp is called");
        });
        waitForFxEvents();
    }

    @Test
    public void testMoveDown() {
        Platform.runLater(() -> {
            double initialY = userPlane.getTranslateY();
            userPlane.moveDown();
            userPlane.updatePosition();
            assertTrue(userPlane.getTranslateY() > initialY, "UserPlane should move down when moveDown is called");
        });
        waitForFxEvents();
    }

    @Test
    public void testStop() {
        Platform.runLater(() -> {
            userPlane.moveUp(); // Start moving
            userPlane.stop(); // Stop the movement
            userPlane.updatePosition();
            assertEquals(0, userPlane.getTranslateY(), "UserPlane should not move when stop is called");
        });
        waitForFxEvents();
    }


    @Test
    public void testFireProjectile() {
        Platform.runLater(() -> {
            System.out.println("UserPlane LayoutX: " + userPlane.getLayoutX());
            System.out.println("UserPlane LayoutY: " + userPlane.getLayoutY());
            System.out.println("UserPlane TranslateY: " + userPlane.getTranslateY());

            ActiveActorDestructible projectile = userPlane.fireProjectile();
            assertNotNull(projectile, "UserPlane should fire a projectile");

            UserProjectile userProjectile = (UserProjectile) projectile;
            System.out.println("Projectile X: " + userProjectile.getX());
            System.out.println("Projectile Y: " + userProjectile.getY());
        });

        waitForFxEvents();
    }


    @Test
    public void testTakeDamageWithoutShield() {
        Platform.runLater(() -> {
            int initialHealth = userPlane.getHealth();
            userPlane.takeDamageFromProjectile();
            assertEquals(initialHealth - 1, userPlane.getHealth(), "Health should decrease by 1 when taking damage without shield");
        });
        waitForFxEvents();
    }

    @Test
    public void testTakeDamageWithShield() {
        Platform.runLater(() -> {
            userPlane.activateShield(); // Activate shield
            int initialShieldDamage = userPlane.getShieldDamageCounter();
            userPlane.takeDamageFromProjectile();
            assertEquals(initialShieldDamage + 1, userPlane.getShieldDamageCounter(), "Shield damage counter should increase by 1 when shield is active");
            assertTrue(userPlane.isShieldActive(), "Shield should remain active until MAX_SHIELD_DAMAGE is reached");
        });
        waitForFxEvents();
    }

    @Test
    public void testDeactivateShield() {
        Platform.runLater(() -> {
            userPlane.activateShield();
            for (int i = 0; i < UserPlane.MAX_SHIELD_DAMAGE; i++) {
                userPlane.takeDamageFromProjectile();
            }
            assertFalse(userPlane.isShieldActive(), "Shield should deactivate after MAX_SHIELD_DAMAGE is reached");
        });
        waitForFxEvents();
    }

    @Test
    public void testUpdateShieldPosition() {
        Platform.runLater(() -> {
            userPlane.activateShield(); // Activate shield
            userPlane.updateActor(); // This will update shield position
            Bounds planeBounds = userPlane.getBoundsInParent();
            UserShieldImage shield = userPlane.getShieldImage();
            assertEquals(planeBounds.getMinX() + planeBounds.getWidth() / 2, shield.getLayoutX() + shield.getFitWidth() / 2, 0.1,
                    "Shield X position should align with the UserPlane's center");
            assertEquals(planeBounds.getMinY() + planeBounds.getHeight() / 2, shield.getLayoutY() + shield.getFitHeight() / 2, 0.1,
                    "Shield Y position should align with the UserPlane's center");
        });
        waitForFxEvents();
    }

    @Test
    public void testIncrementKillCount() {
        int initialKills = userPlane.getNumberOfKills();
        Platform.runLater(() -> userPlane.incrementKillCount());
        waitForFxEvents();
        assertEquals(initialKills + 1, userPlane.getNumberOfKills(), "Kill count should increment by 1 when incrementKillCount is called");
    }
}
