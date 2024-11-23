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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ScoreboardPage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background6.jpeg";

    private final Stage stage;
    private final Controller controller;

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    public ScoreboardPage(Stage stage, Controller controller) {
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
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // 30px from top and left

        // Scoreboard Title
        VBox titleVBox = new VBox();
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30, 0, 0, 0));

        Label scoreboardTitle = new Label("Scoreboard");
        scoreboardTitle.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 100));

        scoreboardTitle.getStyleClass().add("title-text");
        titleVBox.getChildren().add(scoreboardTitle);

        // --- Headers for the Table ---
        HBox headers = new HBox();
        headers.setSpacing(200);
        headers.setAlignment(Pos.CENTER);
        headers.setPadding(new Insets(10, 0, 10, 0));

        Label levelHeader = new Label("Level");
        levelHeader.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                Font.font("Arial", 24)).getName(), 24));
        levelHeader.setTextFill(Color.BLACK);

        Label timeHeader = new Label("Fastest Time (s)");
        timeHeader.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                Font.font("Arial", 24)).getName(), 24));
        timeHeader.setTextFill(Color.BLACK);

        headers.getChildren().addAll(levelHeader, timeHeader);

        // --- Separator ---
        Label separator = new Label("--------------------------------------------------");
        separator.setTextFill(Color.BLACK);

        // --- Scores Box with Styling ---
        VBox scoresBox = new VBox(10);
        scoresBox.setAlignment(Pos.CENTER);
        scoresBox.setPadding(new Insets(20));
        scoresBox.setMaxWidth(650);
        scoresBox.setPrefHeight(400);
        scoresBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        // Add headers and separator
        scoresBox.getChildren().addAll(headers, separator);

        // Fetch fastest times from the controller
        Map<String, Long> fastestTimes = controller.getFastestTimes();

        // Debugging: Print the fastest times to verify
        System.out.println("Fastest Times Retrieved:");
        for (Map.Entry<String, Long> entry : fastestTimes.entrySet()) {
            System.out.println("Level: " + entry.getKey() + ", Time: " + entry.getValue() + " seconds");
        }

        // Dynamically create rows for each level
        for (Map.Entry<String, Long> entry : fastestTimes.entrySet()) {
            HBox row = new HBox();
            row.setSpacing(200);
            row.setAlignment(Pos.CENTER);
            row.setPadding(new Insets(5, 0, 5, 0));

            Label levelName = new Label(entry.getKey());
            levelName.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                    Font.font("Arial", 20)).getName(), 20));
            levelName.setTextFill(Color.BLACK);

            String timeStr = entry.getValue() > 0 ? formatTime(entry.getValue()) : "N/A";
            Label time = new Label(timeStr);
            time.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                    Font.font("Arial", 20)).getName(), 20));
            time.setTextFill(Color.BLACK);

            row.getChildren().addAll(levelName, time);
            scoresBox.getChildren().add(row);
        }

        // Optionally, handle levels with no recorded times yet
        // For example, list all available levels and indicate if no time is recorded
        /*
        String[] predefinedLevels = {"LEVEL ONE", "LEVEL TWO", "LEVEL THREE", "LEVEL FOUR"};
        for (String level : predefinedLevels) {
            HBox row = new HBox();
            row.setSpacing(200);
            row.setAlignment(Pos.CENTER);
            row.setPadding(new Insets(5, 0, 5, 0));

            Label levelName = new Label(level);
            levelName.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                    Font.font("Arial", 20)).getName(), 20));
            levelName.setTextFill(Color.BLACK);

            Label timeLabel;
            if (fastestTimes.containsKey(level)) {
                timeLabel = new Label(formatTime(fastestTimes.get(level)));
            } else {
                timeLabel = new Label("N/A");
            }
            timeLabel.setFont(Font.font(customFonts.getOrDefault("Pixel Digivolve",
                    Font.font("Arial", 20)).getName(), 20));
            timeLabel.setTextFill(Color.BLACK);

            row.getChildren().addAll(levelName, timeLabel);
            scoresBox.getChildren().add(row);
        }
        */

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(titleVBox, scoresBox);

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

    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Ensure this font is available
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
