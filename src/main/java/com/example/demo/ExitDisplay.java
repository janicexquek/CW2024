package com.example.demo;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class ExitDisplay {

    private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.png";
    private static final int EXIT_HEIGHT = 50;

    private HBox container;
    private double containerXPosition;
    private double containerYPosition;

    // Callbacks to handle game state
    private Runnable pauseGameCallback;
    private Runnable resumeGameCallback;
    private Runnable backToMainMenuCallback;
    private Runnable showExitOverlayCallback;

    // Reference to the ExitOverlay
    private ExitOverlay exitOverlay;

    public ExitDisplay(double xPosition, double yPosition, Runnable pauseGameCallback, Runnable resumeGameCallback, Runnable backToMainMenuCallback, Runnable showExitOverlayCallback) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.pauseGameCallback = pauseGameCallback;
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.showExitOverlayCallback = showExitOverlayCallback;

        initializeContainer();
        initializeExit();
    }

    private void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

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

    public HBox getContainer() {
        return container;
    }

    private void showExitOverlay() {
        // Pause the game
        if (pauseGameCallback != null) {
            pauseGameCallback.run();
        }

        // No need to call initializeButtons; buttons are initialized in ExitOverlay's constructor

        // Show the overlay
        exitOverlay.showExitOverlay();
    }
}
