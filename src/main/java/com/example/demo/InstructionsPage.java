package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InstructionsPage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background5.jpg";
    private static final String FONT_PATH = "/com/example/demo/fonts/Cartoon cookies.ttf";

    private final Stage stage;
    private final Controller controller;

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    public InstructionsPage(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        // Load custom fonts
        loadCustomFonts();
    }

    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // Initialize MusicManager instance
        MusicManager musicManager = MusicManager.getInstance();

        // --- Back Button ---
        StackPane backButton = createCustomButton("Back", "/com/example/demo/images/ButtonText_Small_Round.png", 80, 30);
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // 10px from top and left

        // --- Instructions Box with Blur Effect ---
        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsBox.setPadding(new Insets(20));
        instructionsBox.setMaxWidth(500);
        instructionsBox.setMaxHeight(500);
        instructionsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        // Instructions Title
        VBox titleVBox = new VBox();
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(10,0,20,0));

        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setTextFill(Color.BLACK);
        instructionsTitle.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 60));
        titleVBox.getChildren().add(instructionsTitle);

        // Instructions Text
        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.TOP_CENTER);
        textVBox.setPadding(new Insets(20,0,40,0));
        Label instructionsText = new Label(
                "Welcome to SKY BATTLE!\n\n" +
                        "Use the arrow keys to navigate your spaceship.\n" +
                        "Press SPACE to shoot enemies.\n\n" +
                        "Avoid incoming fire and destroy all enemies to win.\n\n" +
                        "Good luck and have fun!"
        );
        instructionsText.setTextFill(Color.BLACK);
        instructionsText.setFont(Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 20));
        instructionsText.setWrapText(true);
        textVBox.getChildren().add(instructionsText);

        // Add title and text to the box
        instructionsBox.getChildren().addAll(titleVBox, textVBox);

        // --- Main Layout ---
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(instructionsBox);

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout, backButton);

        // Create and set the scene
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

        // Link the CSS stylesheet
        URL cssResource = getClass().getResource("/com/example/demo/styles/styles.css");
        if (cssResource == null) {
            System.err.println("CSS file not found!");
        } else {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        stage.setScene(scene);
        stage.show();
    }

    private StackPane createCustomButton(String text, String imagePath, double width, double height) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        label.setFont(Font.font(customFonts.get("Cartoon cookies").getName()));
        label.getStyleClass().add("button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");

        // Ensure the StackPane size matches the image size
        stackPane.setMinSize(width, height);
        stackPane.setMaxSize(width, height);

        // Change cursor to hand on hover
        stackPane.setCursor(Cursor.HAND);

        // Create a single ScaleTransition
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

        return stackPane;
    }


    private void loadCustomFonts() {
        try {
            Font font = Font.loadFont(getClass().getResourceAsStream(FONT_PATH), 10);
            if (font == null) {
                System.err.println("Failed to load font: " + FONT_PATH);
            } else {
                // Store the font with its name for later use
                customFonts.put(font.getName(), font);
                System.out.println("Loaded font: " + font.getName());
            }
        } catch (Exception e) {
            System.err.println("Error loading font: " + FONT_PATH);
            e.printStackTrace();
        }
    }
}
