package com.example.demo;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
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

public class WinOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png";
    private static final String BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png";
    private static final String FONT_PATH = "/com/example/demo/fonts/Cartoon cookies.ttf";
    private static final String BUTTON_FONT_PATH = "/com/example/demo/fonts/Sugar Bomb.ttf";

    // Dimensions for buttons
    private static final double BUTTON_WIDTH = 180;
    private static final double BUTTON_HEIGHT = 60;

    // Map to store loaded fonts
    private Map<String, Font> customFonts = new HashMap<>();

    // Flags to prevent duplicate button initialization
    private boolean buttonsInitialized = false;

    public WinOverlay(double screenWidth, double screenHeight) {
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

        // Add a key event handler to consume ESC key when WinOverlay is active
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                // Optionally, ignore or consume the event to prevent underlying handlers
                event.consume();
            }
        });
    }
    // New method to load all fonts
    private void loadCustomFonts() {
        String[] fontPaths = {
                FONT_PATH,
                BUTTON_FONT_PATH
        };

        for (String fontPath : fontPaths) {
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream == null) {
                    System.err.println("Font not found: " + fontPath);
                    continue;
                }
                Font font = Font.loadFont(fontStream, 10); // Initial size; actual size set when applied
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    customFonts.put(font.getName(), font);
                    System.out.println("Loaded font: " + font.getName());
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }

        // Optional: Print all loaded font names for verification
        System.out.println("Available fonts in WinOverlay:");
        for (String fontName : customFonts.keySet()) {
            System.out.println("- " + fontName);
        }
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
            System.err.println("Failed to load WinOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the win message label
        Label winMessage = new Label("You Win!");
        winMessage.setTextFill(Color.WHITE);
        winMessage.setFont(loadCustomFont(50));
        winMessage.setWrapText(true);
        winMessage.setAlignment(Pos.TOP_CENTER);
        winMessage.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create a VBox to hold the message
        VBox vbox = new VBox(20); // Spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);

        vbox.getChildren().add(winMessage); // Add the message to VBox

        // Stack the background and the message box
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

        return messageBox;
    }

    // Method to load the custom font
    private Font loadCustomFont(double size) {
        try {
            return Font.loadFont(getClass().getResourceAsStream(FONT_PATH), size);
        } catch (Exception e) {
            System.err.println("Failed to load custom font for WinOverlay.");
            e.printStackTrace();
            // Fallback to Arial
            return Font.font("Arial", size);
        }
    }

    // Method to display the overlay
    public void showOverlay() {
        System.out.println("Showing WinOverlay.");
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    // Method to hide the overlay
    public void hideOverlay() {
        System.out.println("Hiding WinOverlay.");
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }

    // Combined method to create, assign actions, and add buttons to the overlay
    public void initializeButtons(Runnable backCallback, Runnable nextCallback) {
        if (buttonsInitialized) {
            System.out.println("Buttons already initialized. Skipping initialization.");
            return;
        }

        System.out.println("Initializing WinOverlay buttons.");

        // Create the buttons with image backgrounds and assign actions
        Button backButton = createCustomButton("Main Menu", backCallback);
        Button nextButton = createCustomButton("Next Level", nextCallback);

        // Create an HBox to hold the buttons horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, nextButton);

        // Add the buttonBox to the overlay's VBox
        // Assuming the messageBox's VBox is the second child (index 1)
        if (getChildren().size() > 0 && getChildren().get(0) instanceof StackPane) {
            StackPane messageBox = (StackPane) getChildren().get(0);
            if (messageBox.getChildren().size() > 1 && messageBox.getChildren().get(1) instanceof VBox) {
                VBox vbox = (VBox) messageBox.getChildren().get(1);
                // Remove existing HBox if any to prevent duplication
                vbox.getChildren().removeIf(node -> node instanceof HBox);
                // Add the new buttonBox
                vbox.getChildren().add(buttonBox);
                buttonsInitialized = true;
            }
        }
    }

    // Factory method to create a custom button with image and assign its action
    private Button createCustomButton(String buttonText, Runnable action) {
        Button button = new Button();
        try {
            // Load the button image
            Image buttonImage = new Image(getClass().getResourceAsStream(BUTTON_IMAGE_NAME));
            ImageView buttonImageView = new ImageView(buttonImage);
            buttonImageView.setFitWidth(BUTTON_WIDTH); // Adjust width as needed
            buttonImageView.setFitHeight(BUTTON_HEIGHT); // Adjust width as needed
            buttonImageView.setPreserveRatio(true);

            // Create a StackPane to overlay text on the image
            StackPane stack = new StackPane();
            stack.getChildren().add(buttonImageView);

            // Create label for button text
            Label label = new Label(buttonText);
            label.setTextFill(Color.WHITE);
            label.setFont(loadCustomFont(20)); // Adjust font size as needed
            stack.getChildren().add(label);

            // Set the StackPane as the button's graphic
            button.setGraphic(stack);

            // Remove default button styling
            button.setStyle("-fx-background-color: transparent;");

            // Assign the callback
            button.setOnAction(e -> action.run());

            // Apply hover effects
            applyHoverEffect(stack);
        } catch (Exception e) {
            System.err.println("Failed to load button image: " + BUTTON_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a simple styled button
            button.setText(buttonText);
            button.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
            button.setOnAction(event -> action.run()); // Assign the callback
            // Apply hover effects directly to the button
            applyHoverEffect(button);
        }
        return button;
    }

    // Method to apply hover effects using ScaleTransition
    private void applyHoverEffect(StackPane stackPane) {
        // Create a single ScaleTransition for the StackPane
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        // Add hover effects
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
        // Create a single ScaleTransition for the Button
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), button);

        // Add hover effects
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
