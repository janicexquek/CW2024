package com.example.demo;

import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class Settings {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";

    private final Stage stage; // Reference to the primary stage
    private final Controller controller; // Reference to the Controller

    // Updated constructor to accept Controller
    public Settings(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
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

        // Create a StackPane as the root layout to layer background and controls
        StackPane root = new StackPane();
        root.getChildren().add(backgroundImageView);

        // --- Back Button ---
        StackPane backButton = createCustomSettingsButton("Back", 80, 30, "/com/example/demo/images/ButtonText_Small_Round.png");
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // --- Settings Title ---
        Label titleLabel = new Label("SETTINGS");
        titleLabel.getStyleClass().add("settings-title");

        // --- Top HBox containing Back Button and Title ---
        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.TOP_CENTER);
        topHBox.setPadding(new Insets(10, 20, 10, 20));
        topHBox.getChildren().addAll(backButton, titleLabel);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // --- Labels VBox ---
        VBox labelsBox = new VBox(30);
        labelsBox.setAlignment(Pos.CENTER_LEFT);
        labelsBox.getChildren().addAll(
                new Label("Background Effects:"),
                new Label("Sound Effects:")
        );

        // Style labels
        for (javafx.scene.Node node : labelsBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().add("settings-label");
            }
        }

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

        controlsBox.getChildren().addAll(musicVolumeSlider, sfxVolumeSlider);

        // --- Content HBox containing Labels and Controls ---
        HBox contentHBox = new HBox(50);
        contentHBox.setAlignment(Pos.CENTER);
        contentHBox.getChildren().addAll(labelsBox, controlsBox);


        // --- Save and Defaults Buttons ---
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

        // --- Main VBox containing everything ---
        VBox mainVBox = new VBox(80);
        mainVBox.setAlignment(Pos.TOP_CENTER);
        mainVBox.setPadding(new Insets(20));
        mainVBox.getChildren().addAll(topHBox, contentHBox, bottomHBox);

        // Add mainVBox to the root StackPane
        root.getChildren().add(mainVBox);
        StackPane.setAlignment(mainVBox, Pos.CENTER);

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
        label.getStyleClass().add("settings-button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("settings-button");

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
}