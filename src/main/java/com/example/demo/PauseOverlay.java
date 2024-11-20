package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PauseOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png";
    private static final String FONT_PATH = "/com/example/demo/fonts/Pixel Digivolve.otf";

    public PauseOverlay(double screenWidth, double screenHeight, Runnable togglePauseCallback) {
        // Set the size of the overlay to cover the entire screen
        setPrefSize(screenWidth, screenHeight);
        setMaxSize(screenWidth, screenHeight);
        setMinSize(screenWidth, screenHeight);

        // Make the overlay non-focusable and ignore mouse events when not visible
        setFocusTraversable(false);
        setMouseTransparent(true);

        // Semi-transparent background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // 50% opacity

        // Create the message box
        StackPane messageBox = createMessageBox(screenWidth, screenHeight);

        // Add the message box to the center of the overlay
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);

        // Add key event filter to handle ESC key
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePauseCallback.run();
                event.consume();
            }
        });
    }

    private StackPane createMessageBox(double screenWidth, double screenHeight) {
        // Fixed size for the message box
        double boxWidth = 500;
        double boxHeight = 500;

        // Load box1.png as the background for the message box
        ImageView background = null;
        try {
            background = new ImageView(new Image(getClass().getResource(BOX_IMAGE_NAME).toExternalForm()));
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        } catch (Exception e) {
            System.err.println("Failed to load PauseOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the message label
        Label message = new Label("Press ESC to continue the game");
        message.setTextFill(Color.WHITE);
        message.setFont(loadCustomFont(24));
        message.setWrapText(true);
        message.setAlignment(Pos.CENTER);
        message.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create a VBox to hold the message
        VBox vbox = new VBox(message);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);

        // Stack the background and the message
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

        return messageBox;
    }


    // Method to load the custom font
    private Font loadCustomFont(double size) {
        try {
            return Font.loadFont(getClass().getResourceAsStream(FONT_PATH), size);
        } catch (Exception e) {
            System.err.println("Failed to load custom font for PauseOverlay.");
            e.printStackTrace();
            // Fallback to Arial
            return Font.font("Arial", size);
        }
    }
}
