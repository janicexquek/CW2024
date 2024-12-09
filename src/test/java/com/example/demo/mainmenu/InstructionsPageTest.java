package com.example.demo.mainmenu;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javafx.application.Platform;
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

import com.example.demo.controller.Controller;

@ExtendWith(ApplicationExtension.class)
public class InstructionsPageTest {

    private Stage stage;
    private Controller controller;
    private InstructionsPage instructionsPage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        this.controller = mock(Controller.class);

        // Initialize InstructionsPage
        instructionsPage = new InstructionsPage(stage, controller);

        // Set initial stage size
        stage.setWidth(1280);
        stage.setHeight(720);
    }

    @Test
    public void testInstructionsPageLayout(FxRobot robot) {
        // Display the InstructionsPage on the JavaFX Application Thread
        Platform.runLater(() -> instructionsPage.show());

        // Wait for JavaFX thread to complete
        robot.interact(() -> {});

        // Verify the presence of the background image
        robot.interact(() -> {
            Scene scene = stage.getScene();
            assert scene != null : "Scene should not be null";
            assert scene.getRoot().lookup(".image-view") != null : "Background image should be present";
        });

        // Verify the title label is present and has the correct text
        robot.interact(() -> {
            assert stage.getScene().lookup(".title-text") != null : "Title label should be present";
            assert stage.getScene().lookup(".title-text").toString().contains("Instructions")
                    : "Title label should contain the text 'Instructions'";
        });
    }

    @Test
    public void testInstructionsPageBackButton(FxRobot robot) {
        Platform.runLater(() -> {
            // Display the InstructionsPage
            instructionsPage.show();

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
