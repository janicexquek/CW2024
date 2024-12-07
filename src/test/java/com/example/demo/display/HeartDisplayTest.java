package com.example.demo.display;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class HeartDisplayTest {

    private HeartDisplay heartDisplay;

    /**
     * Sets up the JavaFX environment before each test.
     *
     * @param stage the primary stage for this test
     */
    @Start
    private void start(Stage stage) {
        // Initialize HeartDisplay with test parameters
        heartDisplay = new HeartDisplay(100, 100, 3);

        // Set up the scene with the HeartDisplay container
        Scene scene = new Scene(heartDisplay.getContainer(), 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Test that the HeartDisplay initializes with the correct number of hearts.
     */
    @Test
    public void testInitialHearts() {
        // Ensure the container is not null
        assertNotNull(heartDisplay.getContainer(), "Container should not be null");

        // Verify the number of hearts displayed matches the initial count
        int expectedHearts = 3;
        int actualHearts = heartDisplay.getContainer().getChildren().size();
        assertEquals(expectedHearts, actualHearts, "Initial number of hearts should be " + expectedHearts);

        // Verify each child is an ImageView with the correct properties
        for (int i = 0; i < actualHearts; i++) {
            assertTrue(heartDisplay.getContainer().getChildren().get(i) instanceof ImageView,
                    "Child at index " + i + " should be an instance of ImageView");
            ImageView heart = (ImageView) heartDisplay.getContainer().getChildren().get(i);
            assertEquals(HeartDisplay.HEART_HEIGHT, heart.getFitHeight(),
                    "Heart height should be " + HeartDisplay.HEART_HEIGHT);
            assertTrue(heart.isPreserveRatio(), "Heart should preserve aspect ratio");
        }
    }

    /**
     * Test the removeHeart method to ensure hearts are removed correctly.
     */
    @Test
    public void testRemoveHeart(FxRobot robot) {
        // Initial number of hearts
        int initialHearts = heartDisplay.getContainer().getChildren().size();
        assertEquals(3, initialHearts, "Should start with 3 hearts");

        // Remove one heart on the JavaFX Application Thread
        robot.interact(() -> heartDisplay.removeHeart());
        int afterFirstRemoval = heartDisplay.getContainer().getChildren().size();
        assertEquals(2, afterFirstRemoval, "Should have 2 hearts after first removal");

        // Remove second heart
        robot.interact(() -> heartDisplay.removeHeart());
        int afterSecondRemoval = heartDisplay.getContainer().getChildren().size();
        assertEquals(1, afterSecondRemoval, "Should have 1 heart after second removal");

        // Remove third heart
        robot.interact(() -> heartDisplay.removeHeart());
        int afterThirdRemoval = heartDisplay.getContainer().getChildren().size();
        assertEquals(0, afterThirdRemoval, "Should have 0 hearts after third removal");

        // Attempt to remove a heart when none are left
        robot.interact(() -> heartDisplay.removeHeart());
        int afterFourthRemoval = heartDisplay.getContainer().getChildren().size();
        assertEquals(0, afterFourthRemoval, "Should still have 0 hearts after attempting to remove from empty");
    }

    /**
     * Test the positioning of the heart container.
     */
    @Test
    public void testContainerPosition(FxRobot robot) {
        double expectedX = 100;
        double expectedY = 100;
        HBox container = heartDisplay.getContainer();

        // Since layoutX and layoutY are set in initializeContainer
        robot.interact(() -> {
            assertEquals(expectedX, container.getLayoutX(), "Container X position should be " + expectedX);
            assertEquals(expectedY, container.getLayoutY(), "Container Y position should be " + expectedY);
        });
    }

    /**
     * Test that the heart image is loaded correctly.
     */
    @Test
    public void testHeartImageLoading(FxRobot robot) {
        HBox container = heartDisplay.getContainer();
        robot.interact(() -> {
            if (!container.getChildren().isEmpty()) {
                ImageView heart = (ImageView) container.getChildren().get(0);
                assertNotNull(heart.getImage(), "Heart image should be loaded");
                assertFalse(heart.getImage().isError(), "Heart image should load without errors");
            } else {
                fail("No hearts found in the container to test image loading");
            }
        });
    }
}
