// File: com/example/demo/overlay/OverlayManagerTest.java

package com.example.demo.overlay;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class OverlayManagerTest extends ApplicationTest {

    private OverlayManager overlayManager;
    private boolean pauseGameCalled;
    private boolean resumeGameCalled;
    private boolean backToMainMenuCalled;
    private boolean startGameCalled;

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        overlayManager = new OverlayManager(
                root,
                800, 600,
                () -> pauseGameCalled = true,
                () -> resumeGameCalled = true,
                () -> backToMainMenuCalled = true
        );
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        pauseGameCalled = false;
        resumeGameCalled = false;
        backToMainMenuCalled = false;
        startGameCalled = false;
        Platform.runLater(() -> overlayManager.hideAllOverlays());
        waitForFxEvents();
    }

    @Test
    public void testStartCountdown() {
        Platform.runLater(() -> overlayManager.startCountdown(() -> startGameCalled = true));
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.COUNTDOWN, overlayManager.getActiveOverlay(), "Active overlay should be COUNTDOWN");
        assertTrue(pauseGameCalled, "Pause game callback should be triggered when starting the countdown");

        // Simulate countdown finish
        Platform.runLater(() -> overlayManager.onCountdownFinished());
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.NONE, overlayManager.getActiveOverlay(), "Active overlay should be NONE after countdown");
        assertTrue(resumeGameCalled, "Resume game callback should be triggered after countdown");
        assertTrue(startGameCalled, "Start game callback should be triggered after countdown");
    }

    @Test
    public void testShowPauseOverlay() {
        Platform.runLater(() -> overlayManager.showPauseOverlay());
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.PAUSE, overlayManager.getActiveOverlay(), "Active overlay should be PAUSE");
    }

    @Test
    public void testHidePauseOverlay() {
        Platform.runLater(() -> overlayManager.showPauseOverlay());
        waitForFxEvents();

        Platform.runLater(() -> overlayManager.hidePauseOverlay());
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.NONE, overlayManager.getActiveOverlay(), "Active overlay should be NONE after hiding pause overlay");
    }

    @Test
    public void testShowExitOverlay() {
        Platform.runLater(() -> overlayManager.showExitOverlay());
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.EXIT, overlayManager.getActiveOverlay(), "Active overlay should be EXIT");
    }

    @Test
    public void testHideExitOverlay() {
        Platform.runLater(() -> overlayManager.showExitOverlay());
        waitForFxEvents();

        Platform.runLater(() -> overlayManager.hideExitOverlay());
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.NONE, overlayManager.getActiveOverlay(), "Active overlay should be NONE after hiding exit overlay");
    }

    @Test
    public void testShowWinOverlay() {
        Platform.runLater(() -> overlayManager.showWinOverlay(
                () -> backToMainMenuCalled = true,
                null, // No next level callback
                null, // No restart callback
                "Level 1", 300, 250, "Achievement Unlocked!"
        ));
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.WIN, overlayManager.getActiveOverlay(), "Active overlay should be WIN");
    }

    @Test
    public void testShowGameOverOverlay() {
        Platform.runLater(() -> overlayManager.showGameOverOverlay(
                () -> backToMainMenuCalled = true,
                null, // No restart callback
                "Level 1", 300, "Fastest Time: 02:30"
        ));
        waitForFxEvents();

        assertEquals(OverlayManager.ActiveOverlay.GAME_OVER, overlayManager.getActiveOverlay(), "Active overlay should be GAME_OVER");
    }
}
