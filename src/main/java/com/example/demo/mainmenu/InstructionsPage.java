package com.example.demo.mainmenu;

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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InstructionsPage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background5.jpg";

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
        SettingsManager settingsManager = SettingsManager.getInstance();

        // --- Back Button ---
        StackPane backButton = createCustomButton("Back",
                "/com/example/demo/images/ButtonText_Small_Round.png", 80, 30);
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // 10px from top and left

        // Instructions Title
        VBox titleVBox = new VBox();
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30,0,0,0));

        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 100));

        instructionsTitle.getStyleClass().add("title-text");
        titleVBox.getChildren().add(instructionsTitle);

        // --- Instructions Box with Blur Effect ---
        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsBox.setPadding(new Insets(20));
        instructionsBox.setMaxWidth(650);
        instructionsBox.setPrefHeight(400);
        instructionsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        // Instructions Text
        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.TOP_CENTER);
        textVBox.setPadding(new Insets(20,0,40,0));
        Label instructionsText = new Label(
                "Welcome to SKY BATTLE!\n\n" +
                        "Use the arrow keys to navigate your spaceship.\n" +
                        "Press SPACE to shoot enemies.\n" +
                        "Press ESC to pause your game.\n\n" +
                        "Avoid incoming fire and destroy all enemies to win.\n\n" +
                        "Good luck and have fun!"
        );
        instructionsText.setTextFill(Color.BLACK);
        instructionsText.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                Font.font("Arial", 20)).getName(), 20));
        instructionsText.setWrapText(true);
        textVBox.getChildren().add(instructionsText);

        // Add title and text to the box
        instructionsBox.getChildren().add(textVBox);

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(titleVBox, instructionsBox);

        // --- Main Layout ---
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(mainBox);

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

        // Set the Sugar Bomb font
        label.setFont(Font.font(customFonts.getOrDefault("Sugar Bomb",
                Font.font("Arial", 16)).getName(), 16));

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
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Pixel Digivolve.otf",
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Add the new font path here
        };

        for (String fontPath : fontPaths) {
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream == null) {
                    System.err.println("Font not found: " + fontPath);
                    continue;
                }
                Font font = Font.loadFont(fontStream, 10);
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    customFonts.put(font.getName(), font);
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }


}
