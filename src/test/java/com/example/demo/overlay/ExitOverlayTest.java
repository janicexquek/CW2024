package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class ExitOverlayTest extends ApplicationTest {

    private ExitOverlay exitOverlay;
    private boolean resumeGameCalled;
    private boolean backToMainMenuCalled;
    private boolean hideOverlayCalled;

    @Override
    public void start(Stage stage) {
        // Initialize ExitOverlay with callbacks
        exitOverlay = new ExitOverlay(
                800, 600,
                () -> resumeGameCalled = true,
                () -> backToMainMenuCalled = true,
                () -> hideOverlayCalled = true
        );

        // Add ExitOverlay to the scene
        StackPane root = new StackPane(exitOverlay);
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        resumeGameCalled = false;
        backToMainMenuCalled = false;
        hideOverlayCalled = false;
    }

    @Test
    public void testShowExitOverlay() {
        // Show the overlay
        Platform.runLater(exitOverlay::showExitOverlay);

        // Wait for JavaFX updates
        waitForFxEvents();

        // Verify the overlay is visible
        assertTrue(exitOverlay.isVisible(), "Exit overlay should be visible after showExitOverlay() is called");
    }

    @Test
    public void testHideExitOverlay() {
        // Show the overlay first
        Platform.runLater(exitOverlay::showExitOverlay);
        waitForFxEvents();

        // Hide the overlay
        Platform.runLater(exitOverlay::hideExitOverlay);
        waitForFxEvents();

        // Verify the overlay is hidden
        assertFalse(exitOverlay.isVisible(), "Exit overlay should be hidden after hideExitOverlay() is called");
    }

    @Test
    public void testResumeGameButton() {
        Platform.runLater(() -> {
            exitOverlay.showExitOverlay();
            exitOverlay.initializeButtons(
                    () -> resumeGameCalled = true,
                    null,
                    null
            );
        });
        waitForFxEvents();

        // Verify the button is present in the scene
        StackPane continueButton = (StackPane) exitOverlay.lookup("#continueButton");
        assertNotNull(continueButton, "Continue button should be initialized");

        // Simulate clicking the button
        interact(() -> continueButton.fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1,
                true, true, true, true, true, true, true,
                true, true, true, null
        )));

        // Verify the callback is triggered
        assertTrue(resumeGameCalled, "Resume game callback should be triggered by clicking the Continue button");
    }


    @Test
    public void testBackToMainMenuButton() {
        // Show the overlay and initialize buttons
        Platform.runLater(() -> {
            exitOverlay.showExitOverlay();
            exitOverlay.initializeButtons(
                    null,
                    () -> backToMainMenuCalled = true,
                    null
            );
        });
        waitForFxEvents();

        // Find the Main Menu button by its ID
        StackPane mainMenuButton = (StackPane) exitOverlay.lookup("#mainMenuButton");
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

}
