package com.example.demo.mainmenu;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.example.demo.controller.Controller;
import com.example.demo.mainmenumanager.FastestTimesManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(ApplicationExtension.class)
public class ScoreboardPageTest {

    private Stage stage;
    private Controller controller;
    private ScoreboardPage scoreboardPage;
    private FastestTimesManager fastestTimesManager;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        this.controller = mock(Controller.class);

        // Mock the FastestTimesManager
        fastestTimesManager = mock(FastestTimesManager.class);

        // Set initial stage size
        stage.setWidth(1280);
        stage.setHeight(720);

        // Initialize the ScoreboardPage
        scoreboardPage = new ScoreboardPage(stage, controller);
    }

    @BeforeEach
    public void setupFastestTimes() {
        // Mock fastest times
        Map<String, Long> mockFastestTimes = new HashMap<>();
        mockFastestTimes.put("LEVEL ONE", 123456L); // Mock time in milliseconds
        mockFastestTimes.put("LEVEL TWO", 234567L);
        mockFastestTimes.put("LEVEL THREE", 345678L);

        // Return mock times when queried
        when(fastestTimesManager.getAllFastestTimes()).thenReturn(mockFastestTimes);
    }


    @Test
    public void testScoreboardPageBackButton(FxRobot robot) {
        Platform.runLater(() -> scoreboardPage.show());
        robot.interact(() -> {});

        Platform.runLater(() -> {
            // Locate the back button by its ID
            StackPane backButton = (StackPane) stage.getScene().lookup("#backButton");

            // Assert the back button is found
            assertNotNull(backButton, "Back button should be present in the scene");

            // Simulate the back button click using fireEvent
            backButton.fireEvent(
                    new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                            true, true, true, true, true, true, true, true, true, true, null)
            );

            // Verify the scene transition
            assertNotNull(stage.getScene(), "Scene should not be null after clicking Back");
        });

        // Wait for JavaFX thread to complete
        robot.interact(() -> {});
    }

}
