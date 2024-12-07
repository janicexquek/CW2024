package com.example.demo.display;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class ExitDisplayTest {

    private Runnable pauseGameCallback;
    private Runnable showExitOverlayCallback;
    private ExitDisplay exitDisplay;

    @Start
    public void start(Stage stage) {
        // Mock the callbacks
        pauseGameCallback = mock(Runnable.class);
        showExitOverlayCallback = mock(Runnable.class);

        // Create the display at position (0,0)
        exitDisplay = new ExitDisplay(0, 0, pauseGameCallback, showExitOverlayCallback);

        // Create a scene and add the exit display container
        StackPane root = new StackPane(exitDisplay.getContainer());
        Scene scene = new Scene(root, 200, 200);

        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testContainerInitialized() {
        Assertions.assertNotNull(exitDisplay.getContainer(), "Container should be initialized");
        Assertions.assertFalse(exitDisplay.getContainer().getChildren().isEmpty(), "Container should have children");
    }

    @Test
    public void testClickExitButton(FxRobot robot) {
        // Get the ImageView (exit button)
        robot.interact(() -> {});

        // Manually fire the mouse click event
        robot.interact(() -> {
            exitDisplay.getContainer().getChildren().get(0).fireEvent(
                    new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                            true, true, true, true, true, true, true, true, true, true, null)
            );
        });

        // Verify that the callbacks were executed
        verify(pauseGameCallback).run();
        verify(showExitOverlayCallback).run();
    }

}
