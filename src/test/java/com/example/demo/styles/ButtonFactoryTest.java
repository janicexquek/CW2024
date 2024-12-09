package com.example.demo.styles;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ButtonFactoryTest extends ApplicationTest {

    private ButtonFactory buttonFactory;
    private StackPane root;

    @Override
    public void start(Stage stage) {
        buttonFactory = new ButtonFactory();
        root = new StackPane();
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        waitForFxEvents(); // Wait for any pending JavaFX events
    }

    @Test
    public void testCreateCustomButton() {
        // Create a custom button
        StackPane customButton = buttonFactory.createCustomButton(
                "Play", "Pixel Digivolve", 20, 200, 50,
                "/com/example/demo/images/ButtonText_Small_Blue_Round.png"
        );

        // Verify button creation
        assertNotNull(customButton, "Custom button should be created");

        // Add button to the scene and test hover effects
        Platform.runLater(() -> root.getChildren().add(customButton));
        waitForFxEvents();

        // Simulate hover effect
        interact(() -> customButton.fireEvent(new javafx.scene.input.MouseEvent(
                javafx.scene.input.MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                javafx.scene.input.MouseButton.NONE, 0, true, true, true,
                true, true, true, true, true, true, true, null
        )));
        waitForFxEvents();

        // Verify hover effects (Scale)
        assertTrue(customButton.getScaleX() > 1.0 && customButton.getScaleY() > 1.0,
                "Custom button should scale up on hover");
    }

    @Test
    public void testCreateStickerButton() {
        // Create a sticker button
        StackPane stickerButton = buttonFactory.createStickerButton(
                "/com/example/demo/images/aircraft.png", 100, 100
        );

        // Verify button creation
        assertNotNull(stickerButton, "Sticker button should be created");

        // Add button to the scene and test hover effects
        Platform.runLater(() -> root.getChildren().add(stickerButton));
        waitForFxEvents();

        // Simulate hover effect
        interact(() -> stickerButton.fireEvent(new javafx.scene.input.MouseEvent(
                javafx.scene.input.MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                javafx.scene.input.MouseButton.NONE, 0, true, true, true,
                true, true, true, true, true, true, true, null
        )));
        waitForFxEvents();

        // Verify hover effects (Fade and Scale)
        assertTrue(stickerButton.getScaleX() > 1.0 && stickerButton.getScaleY() > 1.0,
                "Sticker button should scale up on hover");
    }
}
