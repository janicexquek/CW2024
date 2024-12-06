package com.example.demo.display;

import com.example.demo.overlay.ExitOverlay;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * Class representing the exit display in the game.
 * Manages the exit button and its interactions.
 */
public class ExitDisplay {

    private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.png";
    private static final int EXIT_HEIGHT = 50;

    private HBox container;
    private double containerXPosition;
    private double containerYPosition;

    // Callbacks to handle game state
    private Runnable pauseGameCallback;
    private Runnable showExitOverlayCallback;

    // Reference to the ExitOverlay
    private ExitOverlay exitOverlay;

    /**
     * Constructor for ExitDisplay.
     *
     * @param xPosition the x position of the container
     * @param yPosition the y position of the container
     * @param pauseGameCallback the callback to pause the game
     * @param showExitOverlayCallback the callback to show the exit overlay
     */
    public ExitDisplay(double xPosition, double yPosition, Runnable pauseGameCallback, Runnable showExitOverlayCallback) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.pauseGameCallback = pauseGameCallback;
        this.showExitOverlayCallback = showExitOverlayCallback;

        initializeContainer();
        initializeExit();
    }

    /**
     * Initializes the container for the exit display.
     */
    private void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

    /**
     * Initializes the exit button and its event handlers.
     */
    private void initializeExit() {
        ImageView exit = new ImageView(new Image(getClass().getResource(EXIT_IMAGE_NAME).toExternalForm()));
        exit.setFitHeight(EXIT_HEIGHT);
        exit.setPreserveRatio(true);
        container.getChildren().add(exit);
        // Add click event handler
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // Pause the game
            if (pauseGameCallback != null) {
                pauseGameCallback.run();
            }
            // Show the exit overlay via LevelView
            if (showExitOverlayCallback != null) {
                showExitOverlayCallback.run();
            }
        });
        // Change cursor to hand on hover
        exit.setCursor(Cursor.HAND);
    }

    /**
     * Returns the container for the exit display.
     *
     * @return the HBox container
     */
    public HBox getContainer() {
        return container;
    }

    /**
     * Shows the exit overlay and pauses the game.
     */
    private void showExitOverlay() {
        // Pause the game
        if (pauseGameCallback != null) {
            pauseGameCallback.run();
        }
        // Show the overlay
        exitOverlay.showExitOverlay();
    }
}