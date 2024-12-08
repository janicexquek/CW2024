package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class GameOverOverlayTest extends ApplicationTest {

    private GameOverOverlay gameOverOverlay;
    private boolean backToMainMenuCalled;
    private boolean restartGameCalled;

    @Override
    public void start(javafx.stage.Stage stage) {
        gameOverOverlay = new GameOverOverlay(800, 600);
        stage.setScene(new javafx.scene.Scene(gameOverOverlay));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        backToMainMenuCalled = false;
        restartGameCalled = false;
    }

    @Test
    public void testBackToMainMenuButton() {
        // Show the overlay and initialize buttons
        Platform.runLater(() -> {
            gameOverOverlay.showGameOverOverlay();
            gameOverOverlay.initializeButtons(
                    () -> backToMainMenuCalled = true,  // Back to Main Menu callback
                    null,  // No Restart callback
                    "Level 1"
            );
        });
        waitForFxEvents();

        // Find the Main Menu button by its ID
        StackPane mainMenuButton = (StackPane) gameOverOverlay.lookup("#mainMenuButton");
        assertNotNull(mainMenuButton, "Main Menu button should be initialized");

        // Simulate clicking the Main Menu button
        interact(() -> mainMenuButton.fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true,
                true, true, true, null
        )));

        // Verify the callback is triggered
        assertTrue(backToMainMenuCalled, "Back to main menu callback should be triggered by clicking the Main Menu button");
    }

    @Test
    public void testRestartButton() {
        // Show the overlay and initialize buttons
        Platform.runLater(() -> {
            gameOverOverlay.showGameOverOverlay();
            gameOverOverlay.initializeButtons(
                    null,  // No Back to Main Menu callback
                    () -> restartGameCalled = true,  // Restart callback
                    "Level 1"
            );
        });
        waitForFxEvents();

        // Find the Restart button by its ID
        StackPane restartButton = (StackPane) gameOverOverlay.lookup("#restartButton");
        assertNotNull(restartButton, "Restart button should be initialized");

        // Simulate clicking the Restart button
        interact(() -> restartButton.fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true,
                true, true, true, null
        )));

        // Verify the callback is triggered
        assertTrue(restartGameCalled, "Restart game callback should be triggered by clicking the Restart button");
    }

    @Test
    public void testOverlayVisibility() {
        // Show the overlay
        Platform.runLater(gameOverOverlay::showGameOverOverlay);
        waitForFxEvents();

        // Verify the overlay is visible and interactable
        assertTrue(gameOverOverlay.isVisible(), "Overlay should be visible after calling showGameOverOverlay");
        assertFalse(gameOverOverlay.isMouseTransparent(), "Overlay should not be mouse transparent after calling showGameOverOverlay");

        // Hide the overlay
        Platform.runLater(gameOverOverlay::hideGameOverOverlay);
        waitForFxEvents();

        // Verify the overlay is hidden and non-interactable
        assertFalse(gameOverOverlay.isVisible(), "Overlay should be hidden after calling hideGameOverOverlay");
        assertTrue(gameOverOverlay.isMouseTransparent(), "Overlay should be mouse transparent after calling hideGameOverOverlay");
    }

}
