package com.example.demo.mainmenumanager;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class PlaneOptionTest extends ApplicationTest {

    private PlaneOption planeOption;
    private int selectedPlaneNumber;
    private boolean selectionCallbackInvoked; // Add this variable
    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage; // Assign the primaryStage to stage

        // Initialize the PlaneOption with a sample image name and a callback
        planeOption = new PlaneOption("userplane1.png", planeNumber -> {
            selectedPlaneNumber = planeNumber;
            selectionCallbackInvoked = true; // Set to true when the callback is invoked
        });

        // Add PlaneOption to a scene for testing
        StackPane root = new StackPane(planeOption);
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        selectedPlaneNumber = -1; // Reset the selected plane number
        selectionCallbackInvoked = false; // Reset the callback invocation status
    }

    @Test
    public void testPlaneOptionInitialization() {
        // Verify the plane option is correctly initialized
        assertEquals(2, planeOption.getPlaneNumber(), "Plane number should be correctly extracted from the filename");
        assertNotNull(planeOption.lookup(".plane-option"), "Plane option should have the correct style class");
    }

    @Test
    public void testHoverEffect() {
        // Simulate mouse hover
        Platform.runLater(() -> planeOption.fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, 0, 0, 0, 0,
                MouseButton.NONE, 0, true, true, true, true, true, true, true, true, true, true, null)));

        // Wait for UI updates
        waitForFxEvents();

        // Verify hover overlay visibility
        Rectangle hoverOverlay = (Rectangle) planeOption.lookup("#hoverOverlay");
        assertNotNull(hoverOverlay, "Hover overlay should be present");
        assertTrue(hoverOverlay.isVisible(), "Hover overlay should be visible after mouse hover.");
    }

    @Test
    public void testClickSelection() {
        // Simulate mouse click on the plane option
        interact(() -> planeOption.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null)));

        // Verify the callback is triggered with the correct plane number
        assertEquals(2, selectedPlaneNumber, "Plane number callback should be triggered with the correct number");
    }

    @Test
    public void testSelectionEffect() {
        // Simulate the selection by directly calling the select method
        Platform.runLater(() -> planeOption.select());

        // Wait for the JavaFX thread to complete updates
        waitForFxEvents();

        // Verify the selection border is visible
        Rectangle selectionBorder = (Rectangle) planeOption.lookup("#selectionBorder");
        assertNotNull(selectionBorder, "Selection border should exist");
        assertTrue(selectionBorder.isVisible(), "Selection border should be visible after calling select()");

        // Verify the callback was invoked by simulating a click
        Platform.runLater(() -> planeOption.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
                MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null)));

        waitForFxEvents();

        // Verify the callback updated the selectedPlaneNumber
        assertEquals(2, selectedPlaneNumber, "The callback should have updated the selectedPlaneNumber correctly.");
    }


}
