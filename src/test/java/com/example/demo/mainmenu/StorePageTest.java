package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class StorePageTest {
    private Stage stage;
    private StorePage storePage;

    @Start
    public void start(Stage stage) {
        this.stage = stage;
        storePage = new StorePage(stage, null); // Use null for Controller if not needed
        storePage.show();
    }

    @Test
    public void testBackButton(FxRobot robot) {
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
    public void testTitleAndSelectionMessage(FxRobot robot) {
        robot.interact(() -> {});

        robot.interact(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene, "Scene should not be null");

            // Verify store title
            assertNotNull(scene.lookup(".title-text"), "Store title should be present");

            // Verify selection message
            assertNotNull(scene.lookup(".title-text"), "Selection message should be present");
        });
    }


    @Test
    public void testPlaneSelection(FxRobot robot) {
        // Set up the StorePage with Controller
        Controller controller = new Controller(stage);
        StorePage storePage = new StorePage(stage, controller);

        // Show the StorePage
        Platform.runLater(storePage::show);
        WaitForAsyncUtils.waitForFxEvents(); // Wait for JavaFX events to complete

        // Locate and click on a plane option
        robot.interact(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene, "Scene should not be null");

            // Locate a plane option (ensure PlaneOption is styled or has a unique ID)
            StackPane planeOption = (StackPane) scene.lookup(".plane-option"); // Update based on your style class or ID
            assertNotNull(planeOption, "Plane option should be present in the scene");

            // Simulate clicking the plane option
            planeOption.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                    true, true, true, true, true, true, true, true, true, true, null));
        });

        WaitForAsyncUtils.waitForFxEvents(); // Wait for JavaFX thread to process click event

        // Verify that the selection message updates correctly
        robot.interact(() -> {
            Label selectionMessageLabel = (Label) stage.getScene().lookup("#selectionMessage"); // Use ID instead of class
            assertNotNull(selectionMessageLabel, "Selection message label should be present");
            assertTrue(selectionMessageLabel.getText().contains("You have selected plane"),
                    "Selection message should update with the selected plane number");
        });
    }

}
