package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class PauseOverlayTest extends ApplicationTest {

    private PauseOverlay pauseOverlay;
    private boolean pauseToggled;
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        pauseToggled = false;

        // Initialize the PauseOverlay with a mock togglePauseCallback
        pauseOverlay = new PauseOverlay(800, 600, () -> pauseToggled = true);

        // Add PauseOverlay to a scene for testing
        StackPane root = new StackPane(pauseOverlay);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        pauseToggled = false; // Reset the pause toggled flag
    }

    @Test
    public void testOverlayInitialization() {
        // Verify the overlay is initially hidden and non-interactive
        assertFalse(pauseOverlay.isVisible(), "Pause overlay should be initially hidden");
        assertTrue(pauseOverlay.isMouseTransparent(), "Pause overlay should be initially mouse transparent");
    }

    @Test
    public void testShowPauseOverlay() {
        // Show the pause overlay
        Platform.runLater(() -> {
            pauseOverlay.setVisible(true);
            pauseOverlay.setMouseTransparent(false);
        });

        // Wait for JavaFX updates
        waitForFxEvents();

        // Verify the overlay is visible and interactive
        assertTrue(pauseOverlay.isVisible(), "Pause overlay should be visible after showing");
        assertFalse(pauseOverlay.isMouseTransparent(), "Pause overlay should be interactive after showing");
    }

    @Test
    public void testHidePauseOverlay() {
        // Show the overlay first
        Platform.runLater(() -> {
            pauseOverlay.setVisible(true);
            pauseOverlay.setMouseTransparent(false);
        });

        // Wait for JavaFX updates
        waitForFxEvents();

        // Hide the pause overlay
        Platform.runLater(pauseOverlay::hidePauseOverlay);

        // Wait for JavaFX updates
        waitForFxEvents();

        // Verify the overlay is hidden and non-interactive
        assertFalse(pauseOverlay.isVisible(), "Pause overlay should be hidden after calling hide");
        assertTrue(pauseOverlay.isMouseTransparent(), "Pause overlay should be mouse transparent after calling hide");
    }
    
}
