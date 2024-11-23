// ExitOverlay.java
package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ExitOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png";
    private static final String BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png";

    // Map to store loaded fonts
    private Map<String, Font> customFonts = new HashMap<>();

    // Flags to prevent duplicate button initialization
    private boolean buttonsInitialized = false;

    // Callbacks for button actions
    private Runnable resumeGameCallback;
    private Runnable backToMainMenuCallback;
    private Runnable hideOverlayCallback;

    public ExitOverlay(double screenWidth, double screenHeight, Runnable resumeGameCallback, Runnable backToMainMenuCallback, Runnable hideOverlayCallback) {
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.hideOverlayCallback = hideOverlayCallback;

        // Set the size of the overlay to cover the entire screen
        setPrefSize(screenWidth, screenHeight);
        setMaxSize(screenWidth, screenHeight);
        setMinSize(screenWidth, screenHeight);

        // Make the overlay non-focusable and ignore mouse events when not visible
        setFocusTraversable(false);
        setMouseTransparent(true);

        // Semi-transparent background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // 50% opacity

        loadCustomFonts();

        // Create the message box
        StackPane messageBox = createMessageBox();

        // Add the message box to the center of the overlay
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);

        // Add a key event handler to consume ESC key when ExitOverlay is active
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            event.consume(); // Prevent ESC key from triggering other actions
        });
    }

    // Method to load all fonts
    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Pixel Digivolve.otf",
                "/com/example/demo/fonts/Sugar Bomb.ttf"
        };

        for (String fontPath : fontPaths) {
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream == null) {
                    System.err.println("Font not found: " + fontPath);
                    continue;
                }
                Font font = Font.loadFont(fontStream, 10); // Initial size; actual size set when applied
                customFonts.put(font.getName(), font);
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }

    private StackPane createMessageBox() {
        // Fixed size for the message box
        double boxWidth = 500;
        double boxHeight = 500;

        // Load box1.png as the background for the message box
        ImageView background;
        try {
            background = new ImageView(new Image(getClass().getResource(BOX_IMAGE_NAME).toExternalForm()));
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        } catch (Exception e) {
            System.err.println("Failed to load ExitOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the title label
        Label titleLabel = new Label("Exit Game ?");
        titleLabel.setTextFill(Color.WHITE);
        Font titleFont = customFonts.get("Cartoon cookies");
        if (titleFont != null) {
            titleLabel.setFont(Font.font(titleFont.getName(), 50));
        } else {
            titleLabel.setFont(Font.font("Arial", 50));
        }
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setMaxWidth(boxWidth - 40);

        // Create the message label
        Label messageLabel = new Label("Do you want to CONTINUE your game or \nBACK to Main Menu?");
        messageLabel.setTextFill(Color.WHITE);
        Font messageFont = customFonts.get("Pixel Digivolve");
        if (messageFont != null) {
            messageLabel.setFont(Font.font(messageFont.getName(), 20));
        } else {
            messageLabel.setFont(Font.font("Arial", 20));
        }
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setMaxWidth(boxWidth - 40);

        // Create buttons with actions that hide the overlay and then run the callbacks
        Button continueButton = createCustomButton("Continue", () -> {
            hideExitOverlay(); // Hide the overlay
            if (resumeGameCallback != null) {
                resumeGameCallback.run(); // Resume the game
            }
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Notify LevelView
            }
        });

        Button backButton = createCustomButton("Main Menu", () -> {
            hideExitOverlay(); // Hide the overlay
            if (backToMainMenuCallback != null) {
                backToMainMenuCallback.run(); // Go back to the main menu
            }
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Notify LevelView
            }
        });


        // Arrange buttons in an HBox
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueButton, backButton);

        // Create a VBox to hold the labels and buttons
        VBox vbox = new VBox(40); // Spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);
        vbox.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        // Stack the background and the message box
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

        return messageBox;
    }

    // Method to display the overlay
    public void showExitOverlay() {
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    // Method to hide the overlay
    public void hideExitOverlay() {
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }

    // Factory method to create a custom button with image and assign its action
    private Button createCustomButton(String buttonText, Runnable action) {
        Button button = new Button();
        try {
            // Load the button image
            Image buttonImage = new Image(getClass().getResourceAsStream(BUTTON_IMAGE_NAME));
            ImageView buttonImageView = new ImageView(buttonImage);
            buttonImageView.setFitWidth(180); // Adjust width as needed
            buttonImageView.setFitHeight(60); // Adjust height as needed
            buttonImageView.setPreserveRatio(true);

            // Create label for button text
            Label label = new Label(buttonText);
            label.setTextFill(Color.WHITE);
            Font buttonFont = customFonts.get("Sugar Bomb");
            if (buttonFont != null) {
                label.setFont(Font.font(buttonFont.getName(), 16));
            } else {
                label.setFont(Font.font("Arial", 16));
            }

            // Create a StackPane to overlay text on the image
            StackPane stack = new StackPane(buttonImageView, label);

            // Set the StackPane as the button's graphic
            button.setGraphic(stack);

            // Remove default button styling and assign the action
            button.setStyle("-fx-background-color: transparent;");
            button.setOnAction(e -> action.run());

            // Apply hover effects
            applyHoverEffect(stack);
        } catch (Exception e) {
            System.err.println("Failed to load button image: " + BUTTON_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a simple styled button
            button.setText(buttonText);
            button.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            button.setOnAction(event -> action.run());
            // Apply hover effects directly to the button
            applyHoverEffect(button);
        }
        return button;
    }

    // Method to apply hover effects using ScaleTransition
    private void applyHoverEffect(StackPane stackPane) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        stackPane.setOnMouseEntered(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        stackPane.setOnMouseExited(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });
    }

    // Overloaded method to apply hover effects to Buttons directly (for fallback)
    private void applyHoverEffect(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), button);

        button.setOnMouseEntered(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(button.getScaleX());
            scaleTransition.setFromY(button.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        button.setOnMouseExited(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(button.getScaleX());
            scaleTransition.setFromY(button.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });
    }
}
