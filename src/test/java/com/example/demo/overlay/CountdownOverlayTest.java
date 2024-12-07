package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class CountdownOverlayTest extends ApplicationTest {

    private CountdownOverlay countdownOverlay;
    private boolean countdownFinished;

    @Override
    public void start(Stage stage) {
        countdownFinished = false;

        // Create a CountdownOverlay instance
        countdownOverlay = new CountdownOverlay(800, 600, () -> countdownFinished = true);

        // Set up the scene with the overlay
        Scene scene = new Scene(countdownOverlay);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        countdownFinished = false; // Reset the countdown finish flag
    }

    @Test
    public void testOverlayInitialization() {
        // Verify initial state of the overlay
        assertFalse(countdownOverlay.isVisible(), "Overlay should initially be hidden");
        assertTrue(countdownOverlay.isMouseTransparent(), "Overlay should initially be mouse transparent");
    }

    @Test
    public void testCountdownStart() {
        // Start the countdown
        Platform.runLater(countdownOverlay::startCountdown);

        // Wait for UI updates
        waitForFxEvents();

        // Verify the overlay is visible after starting the countdown
        assertTrue(countdownOverlay.isVisible(), "Overlay should be visible after starting the countdown");
        assertFalse(countdownOverlay.isMouseTransparent(), "Overlay should not be mouse transparent during the countdown");

        // Verify the label text
        Platform.runLater(() -> {
            String labelText = countdownOverlay.lookup(".label").toString();
            assertTrue(labelText.contains("Game starts in 3"), "Countdown label should start with 'Game starts in 3'");
        });
    }

    @Test
    public void testCountdownEnd() throws InterruptedException {
        // Start the countdown
        Platform.runLater(countdownOverlay::startCountdown);

        // Wait for the duration of the countdown (4 seconds: 3 seconds + 1 for "Game start")
        Thread.sleep(4000);

        // Verify the overlay is hidden after the countdown ends
        Platform.runLater(() -> {
            assertFalse(countdownOverlay.isVisible(), "Overlay should be hidden after the countdown ends");
            assertTrue(countdownOverlay.isMouseTransparent(), "Overlay should be mouse transparent after the countdown ends");
        });

        // Verify the callback was triggered
        assertTrue(countdownFinished, "Countdown finished callback should have been triggered");
    }

    @Test
    public void testHideCountdown() {
        // Show the overlay
        Platform.runLater(countdownOverlay::startCountdown);

        // Hide the countdown manually
        Platform.runLater(countdownOverlay::hideCountdown);

        // Verify the overlay is hidden
        Platform.runLater(() -> {
            assertFalse(countdownOverlay.isVisible(), "Overlay should be hidden after hideCountdown is called");
            assertTrue(countdownOverlay.isMouseTransparent(), "Overlay should be mouse transparent after hideCountdown is called");
        });
    }
}
