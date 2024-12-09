package com.example.demo.mainmenu;

import com.example.demo.mainmenumanager.SettingsManager;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class SettingsTest {
    private Stage stage;
    private Settings settingsPage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        settingsPage = new Settings(stage, null); // Use null for Controller if not needed
        settingsPage.show();
    }

    @Test
    public void testBackButton(FxRobot robot) {
        robot.interact(() -> {});

        // Ensure settings page is displayed
        assertNotNull(stage.getScene(), "Stage scene should not be null");

        robot.interact(() -> {
            // Locate the back button by its ID
            StackPane backButton = (StackPane) stage.getScene().lookup("#backButton");
            assertNotNull(backButton, "Back button should be present in the scene");

            // Simulate clicking the back button
            backButton.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });

        // Wait for JavaFX thread
        robot.interact(() -> {});

        // Verify scene transition
        assertNotNull(stage.getScene(), "Scene should not be null after clicking back button");
    }

    @Test
    public void testVolumeSliders(FxRobot robot) {
        robot.interact(() -> {});

        robot.interact(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene, "Scene should not be null");

            // Locate sliders
            assertNotNull(scene.lookup(".slider"), "Volume sliders should be present");
        });

        // Simulate moving a slider
        robot.moveTo(".slider").drag().dropBy(50, 0);

        robot.interact(() -> {
            assertNotNull(stage.getScene(), "Scene should still be active after slider adjustment");
        });
    }

    @Test
    public void testMuteToggle(FxRobot robot) {
        robot.interact(() -> {});

        // Locate mute toggle button
        robot.interact(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene, "Scene should not be null");
            StackPane muteToggleButton = (StackPane) scene.lookup(".custom-button-hover");
            assertNotNull(muteToggleButton, "Mute toggle button should be present");

            // Simulate clicking mute button
            muteToggleButton.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });

        // Wait for JavaFX thread
        robot.interact(() -> {});

        // Verify mute state
        assertTrue(SettingsManager.getInstance().isAllMuted(), "All sounds should be muted after toggling mute");
    }
}
