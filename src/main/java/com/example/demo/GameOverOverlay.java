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

public class GameOverOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png";
    private static final String BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png";

    // Dimensions for buttons
    private static final double BUTTON_WIDTH = 200;
    private static final double BUTTON_HEIGHT = 80;

    // Map to store loaded fonts
    private Map<String, Font> customFonts = new HashMap<>();

    // Flags to prevent duplicate button initialization
    private boolean buttonsInitialized = false;
    // New Label to display level information
    private Label levelInfoLabel;

    public GameOverOverlay(double screenWidth, double screenHeight) {
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
        StackPane messageBox = createMessageBox(screenWidth, screenHeight);

        // Add the message box to the center of the overlay
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);

        // Add a key event handler to consume ESC key when GameOverOverlay is active
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                // Optionally, ignore or consume the event to prevent underlying handlers
                event.consume();
            }
        });
    }

    // Method to load all fonts
    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Add the new font path here
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
            System.err.println("Failed to load GameOverOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the game over message label
        Label gameOverMessage = new Label("DEFEAT");
        gameOverMessage.setTextFill(Color.WHITE);
        Font messageFont = customFonts.get("Cartoon cookies");
        if (messageFont != null) {
            gameOverMessage.setFont(Font.font(messageFont.getName(), 50)); // Adjust size as needed
        } else {
            System.err.println("Message font 'Cartoon cookies' not loaded. Using default font.");
            gameOverMessage.setFont(Font.font("Arial", 50)); // Fallback font
        }
        gameOverMessage.setWrapText(true);
        gameOverMessage.setAlignment(Pos.TOP_CENTER);
        gameOverMessage.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create the level info label
        levelInfoLabel = new Label("TRY AGAIN LEVEL X"); // Placeholder text
        levelInfoLabel.setTextFill(Color.WHITE);
        Font levelFont = customFonts.get("Sugar Bomb");
        if (levelFont != null) {
            levelInfoLabel.setFont(Font.font(levelFont.getName(), 20)); // Adjust size as needed
        } else {
            System.err.println("Level font 'Sugar Bomb' not loaded. Using default font.");
            levelInfoLabel.setFont(Font.font("Arial", 30)); // Fallback font
        }
        levelInfoLabel.setWrapText(true);
        levelInfoLabel.setAlignment(Pos.CENTER);
        levelInfoLabel.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create a VBox to hold the message and button
        VBox vbox = new VBox(30); // Spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);

        vbox.getChildren().addAll(gameOverMessage, levelInfoLabel); // Add messages to VBox

        // Stack the background and the message box
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

        return messageBox;
    }

    // Method to display the overlay
    public void showOverlay() {
        System.out.println("Showing GameOverOverlay.");
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    // Method to hide the overlay
    public void hideOverlay() {
        System.out.println("Hiding GameOverOverlay.");
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }

    // Combined method to create, assign actions, and add buttons to the overlay
    public void initializeButtons(Runnable backCallback, Runnable restartCallback, String levelName) {
        if (buttonsInitialized) {
            System.out.println("Buttons already initialized. Skipping initialization.");
            return;
        }

        System.out.println("Initializing GameOverOverlay buttons.");

        // Create the back to main menu button with image background and assign action
        Button backButton = createCustomButton("Main Menu", backCallback);
        // Create the "Restart" button
        Button restartButton = createCustomButton("Restart", restartCallback);

        // Create an HBox to hold the button horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons if more are added
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, restartButton);

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
        // Set the level information
        setLevelInfo(levelName);
    }

    public void setLevelInfo(String levelName) {
        if (levelInfoLabel != null) {
            levelInfoLabel.setText("TRY AGAIN " + levelName);
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
            buttonImageView.setFitHeight(BUTTON_HEIGHT); // Adjust height as needed
            buttonImageView.setPreserveRatio(true);

            // Create a StackPane to overlay text on the image
            StackPane stack = new StackPane();
            stack.getChildren().add(buttonImageView);

            // Create label for button text
            Label label = new Label(buttonText);
            label.setTextFill(Color.WHITE);
            Font buttonFont = customFonts.get("Sugar Bomb"); // Use the exact name of the font
            if (buttonFont != null) {
                label.setFont(Font.font(buttonFont.getName(), 20)); // Adjust size as needed
            } else {
                System.err.println("Button font 'Sugar Bomb' not loaded. Using default font.");
                label.setFont(Font.font("Arial", 20)); // Fallback font
            }
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