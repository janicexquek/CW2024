// File: com/example/demo/overlay/WinOverlayTest.java

package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class WinOverlayTest extends ApplicationTest {

    private WinOverlay winOverlay;
    private boolean backToMainMenuCalled;
    private boolean nextLevelCalled;
    private boolean restartCalled;

    @Override
    public void start(javafx.stage.Stage stage) {
        winOverlay = new WinOverlay(800, 600);
        stage.setScene(new javafx.scene.Scene(winOverlay));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        backToMainMenuCalled = false;
        nextLevelCalled = false;
        restartCalled = false;
    }

    @Test
    public void testBackToMainMenuButton() {
        // Show the overlay and initialize buttons
        Platform.runLater(() -> {
            winOverlay.showWinOverlay();
            winOverlay.initializeButtons(
                    () -> backToMainMenuCalled = true,  // Back to Main Menu callback
                    null,  // No Next Level callback
                    null,  // No Restart callback
                    "Level 1"
            );
        });
        waitForFxEvents();

        // Find the Main Menu button by its ID
        StackPane mainMenuButton = (StackPane) winOverlay.lookup("#mainMenuButton");
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
    public void testNextLevelButton() {
        Platform.runLater(() -> {
            winOverlay.showWinOverlay();
            winOverlay.initializeButtons(
                    null,  // No Back to Main Menu callback
                    () -> nextLevelCalled = true,  // Next Level callback
                    null,  // No Restart callback
                    "Level 1"
            );
        });
        waitForFxEvents();

        StackPane nextButton = (StackPane) winOverlay.lookup("#nextButton");
        assertNotNull(nextButton, "Next Level button should be initialized");
        interact(() -> nextButton.fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true,
                true, true, true, null
        )));

        assertTrue(nextLevelCalled, "Next Level callback should be triggered");
    }



    @Test
    public void testRestartButton() {
        // Show the overlay and initialize buttons
        Platform.runLater(() -> {
            winOverlay.showWinOverlay();
            winOverlay.initializeButtons(
                    null,  // No Back to Main Menu callback
                    null,  // No Next Level callback
                    () -> restartCalled = true,  // Restart callback
                    "Level 1"
            );
        });
        waitForFxEvents();

        // Simulate clicking the "Restart" button
        StackPane restartButton = (StackPane) winOverlay.lookup("#restartButton");
        assertNotNull(restartButton, "Restart button should be initialized");
        interact(() -> restartButton.fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true,
                true, true, true, null
        )));

        // Verify the callback is triggered
        assertTrue(restartCalled, "Restart callback should be triggered");
    }
}
