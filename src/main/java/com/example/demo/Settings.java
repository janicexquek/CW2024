package com.example.demo;

import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpeg";

    private final Stage stage; // Reference to the primary stage
    private final Controller controller; // Reference to the Controller

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    // Updated constructor to accept Controller
    public Settings(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        // Load custom fonts
        loadCustomFonts();
    }

    public void show() {
        // Load the background image using the static final String
        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(stage.getWidth()); // Set to scene width
        backgroundImageView.setFitHeight(stage.getHeight()); // Set to scene height
        backgroundImageView.setPreserveRatio(false); // Stretch to fill

        // Initialize MusicManager instance
        MusicManager musicManager = MusicManager.getInstance();

        // --- Back Button ---
        StackPane backButton = createCustomSettingsButton("Back", 80, 30, "/com/example/demo/images/ButtonText_Small_Round.png");
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30));


        // --- Settings Title ---
        // --- Top HBox  Title ---
        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.TOP_CENTER);
        topHBox.setPadding(new Insets(30, 0, 0, 0));

        Label titleLabel = new Label("SETTINGS");
        titleLabel.setFont(Font.font(customFonts.get("Cartoon cookies").getName(), 100));
        titleLabel.getStyleClass().add("title-text");
        topHBox.getChildren().add(titleLabel);

        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // --- Labels VBox ---
        VBox labelsBox = new VBox(30);
        labelsBox.setAlignment(Pos.CENTER_LEFT);

        // Create labels
        Label backgroundEffectsLabel = new Label("Background Effects:");
        Label soundEffectsLabel = new Label("Sound Effects:");
        Label countdownSoundEffectsLabel = new Label("Countdown Sound Effects:");

        // Apply custom font to labels
        Font settingsLabelFont = customFonts.get("Cartoon cookies");
        backgroundEffectsLabel.setTextFill(Color.DARKGRAY);
        soundEffectsLabel.setTextFill(Color.DARKGRAY);
        countdownSoundEffectsLabel.setTextFill(Color.DARKGRAY);
        if (settingsLabelFont != null) {
            backgroundEffectsLabel.setFont(Font.font(settingsLabelFont.getName(), 25));
            soundEffectsLabel.setFont(Font.font(settingsLabelFont.getName(), 25));
            countdownSoundEffectsLabel.setFont(Font.font(settingsLabelFont.getName(), 25));
        }

        labelsBox.getChildren().addAll(backgroundEffectsLabel, soundEffectsLabel, countdownSoundEffectsLabel);

        // --- Controls VBox ---
        VBox controlsBox = new VBox(30);
        controlsBox.setAlignment(Pos.CENTER_RIGHT);

        // --- Background Music Volume Controls ---
        Slider musicVolumeSlider = new Slider(0, 1, musicManager.getMusicVolume());
        musicVolumeSlider.setShowTickLabels(true);
//        musicVolumeSlider.setShowTickMarks(true);
        musicVolumeSlider.setMajorTickUnit(0.2);
        musicVolumeSlider.setBlockIncrement(0.05);
        musicVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's music volume
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicManager.setMusicVolume(newValue.doubleValue());
        });

        // --- Sound Effects Volume Controls ---
        Slider sfxVolumeSlider = new Slider(0, 1, musicManager.getSoundEffectVolume());
        sfxVolumeSlider.setShowTickLabels(true);
//        sfxVolumeSlider.setShowTickMarks(true);
        sfxVolumeSlider.setMajorTickUnit(0.2);
        sfxVolumeSlider.setBlockIncrement(0.05);
        sfxVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's sound effect volume
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicManager.setSoundEffectVolume(newValue.doubleValue());
        });

        // --- Countdown Sound Effects Volume Controls ---
        Slider countdownSfxVolumeSlider = new Slider(0, 1, musicManager.getCountdownSoundVolume());
        countdownSfxVolumeSlider.setShowTickLabels(true);
//        countdownSfxVolumeSlider.setShowTickMarks(true);
        countdownSfxVolumeSlider.setMajorTickUnit(0.2);
        countdownSfxVolumeSlider.setBlockIncrement(0.05);
        countdownSfxVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's countdown sound effect volume
        countdownSfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicManager.setCountdownSoundVolume(newValue.doubleValue());
        });

        controlsBox.getChildren().addAll(musicVolumeSlider, sfxVolumeSlider, countdownSfxVolumeSlider); // Add new slider

        // --- Content HBox containing Labels and Controls ---
        HBox contentHBox = new HBox(50);
        contentHBox.setAlignment(Pos.CENTER);
        contentHBox.getChildren().addAll(labelsBox, controlsBox);


        // --- Save and Defaults Buttons ---
        // --- Save Button ---
        StackPane saveButton = createCustomSettingsButton("Save", 120, 40, "/com/example/demo/images/ButtonText_Small_Round.png");
        saveButton.setOnMouseClicked(e -> {
            // Implement save functionality if needed
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your settings have been saved successfully!");
            alert.showAndWait();
        });

        // --- Defaults Button ---
        StackPane defaultsButton = createCustomSettingsButton("Defaults", 120, 40, "/com/example/demo/images/ButtonText_Small_Round.png");
        defaultsButton.setOnMouseClicked(e -> {
            // Reset to default settings
            musicManager.setMusicVolume(MusicManager.DEFAULT_MUSIC_VOLUME);
            musicManager.setSoundEffectVolume(MusicManager.DEFAULT_SOUND_EFFECT_VOLUME);
            musicVolumeSlider.setValue(MusicManager.DEFAULT_MUSIC_VOLUME);
            sfxVolumeSlider.setValue(MusicManager.DEFAULT_SOUND_EFFECT_VOLUME);
        });


        // --- Bottom HBox containing Save and Defaults Buttons ---
        HBox bottomHBox = new HBox(20);
        bottomHBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomHBox.getChildren().addAll(saveButton, defaultsButton);

        // ---- Second Main VBox contained content box and bottom box
        VBox secondmainVBox = new VBox(80);
        secondmainVBox.setAlignment(Pos.CENTER);
        secondmainVBox.setPadding(new Insets(20));
        secondmainVBox.setMaxWidth(650);
        secondmainVBox.setPrefHeight(400); // Set to desired height
        secondmainVBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 10;");
        secondmainVBox.getChildren().addAll(contentHBox, bottomHBox);

        // --- Main VBox containing everything ---
        VBox mainVBox = new VBox(20);
        mainVBox.setAlignment(Pos.TOP_CENTER);
        mainVBox.setPadding(new Insets(10));
        mainVBox.getChildren().addAll(topHBox, secondmainVBox);

        // --- Main Layout ---
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(mainVBox);

        // Create a StackPane as the root layout to layer background and controls
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout, backButton);

        // Create the scene with preferred dimensions
        Scene settingsScene = new Scene(root, stage.getWidth(), stage.getHeight()); // Adjust width and height as needed

        // Link the CSS stylesheet
        URL cssResource = getClass().getResource("/com/example/demo/styles/styles.css");
        if (cssResource == null) {
            System.err.println("CSS file not found!");
        } else {
            settingsScene.getStylesheets().add(cssResource.toExternalForm());
        }

        // Set the scene on the stage
        stage.setScene(settingsScene);
        stage.show();
    }

    // Private method to create custom settings buttons
    private StackPane createCustomSettingsButton(String text, double width, double height, String imagePath) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        // Set the Sugar Bomb font
        Font buttonFont = customFonts.get("Sugar Bomb"); // Ensure the font name matches
        if (buttonFont != null) {
            label.setFont(Font.font(buttonFont.getName(), 16)); // Adjust size as needed
        } else {
            System.err.println("Button font not found! Using default font.");
            label.setFont(Font.font(16)); // Fallback font
        }
        label.getStyleClass().add("button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");

        // Ensure the StackPane size matches the image size
        stackPane.setMinSize(width, height);
        stackPane.setMaxSize(width, height);

        // Prevent mouse events on transparent areas
        stackPane.setPickOnBounds(false);

        // Create a single ScaleTransition for hover effects
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
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Add the new font path here
        };

        for (String fontPath : fontPaths) {
            try {
                Font font = Font.loadFont(getClass().getResourceAsStream(fontPath), 10);
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    // Store the font with its name for later use
                    customFonts.put(font.getName(), font);
                    System.out.println("Loaded font: " + font.getName());
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }
}