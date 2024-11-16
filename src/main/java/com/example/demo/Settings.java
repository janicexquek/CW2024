package com.example.demo;

import com.example.demo.controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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

        // Create a VBox for the settings controls
        VBox settingsBox = new VBox(20); // 20 pixels spacing
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setPadding(new Insets(50));

        // --- Background Music Volume Controls ---
        Label musicVolumeLabel = new Label("Background Music Volume:");
        Slider musicVolumeSlider = new Slider(0, 1, musicManager.getMusicVolume());
        musicVolumeSlider.setShowTickLabels(true);
        musicVolumeSlider.setShowTickMarks(true);
        musicVolumeSlider.setMajorTickUnit(0.1);
        musicVolumeSlider.setBlockIncrement(0.05);
        musicVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's music volume
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicManager.setMusicVolume(newValue.doubleValue());
        });

        // --- Sound Effects Volume Controls ---
        Label sfxVolumeLabel = new Label("Sound Effects Volume:");
        Slider sfxVolumeSlider = new Slider(0, 1, musicManager.getSoundEffectVolume());
        sfxVolumeSlider.setShowTickLabels(true);
        sfxVolumeSlider.setShowTickMarks(true);
        sfxVolumeSlider.setMajorTickUnit(0.1);
        sfxVolumeSlider.setBlockIncrement(0.05);
        sfxVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's sound effect volume
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicManager.setSoundEffectVolume(newValue.doubleValue());
        });

        // Save Button (Optional: Currently, settings are applied in real-time)
        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(e -> {
            // Currently, volume changes are real-time. You can add persistence here if needed.
            // For example, save settings to a file or preferences.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Settings Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your settings have been saved successfully!");
            alert.showAndWait();
        });

        // --- Defaults Button ---
        Button defaultsButton = new Button("Defaults");
        defaultsButton.setPrefWidth(100);
        defaultsButton.setOnAction(e -> {
            // Reset volumes to default values
            musicManager.setMusicVolume(MusicManager.DEFAULT_MUSIC_VOLUME);
            musicManager.setSoundEffectVolume(MusicManager.DEFAULT_SOUND_EFFECT_VOLUME);

            // Update sliders to reflect default values
            musicVolumeSlider.setValue(MusicManager.DEFAULT_MUSIC_VOLUME);
            sfxVolumeSlider.setValue(MusicManager.DEFAULT_SOUND_EFFECT_VOLUME);
        });

        // Back Button to return to Main Menu
        Button backButton = new Button("Back to Main Menu");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

//        // Add buttons to an HBox
//        HBox buttonsBox = new HBox(20);
//        buttonsBox.setAlignment(Pos.CENTER);
//        buttonsBox.getChildren().addAll(saveButton, defaultsButton);

        // Add all controls to the VBox
        settingsBox.getChildren().addAll(
                musicVolumeLabel,
                musicVolumeSlider,
                sfxVolumeLabel,
                sfxVolumeSlider,
                saveButton,
                defaultsButton,
                backButton
        );

        // Add the settingsBox to the StackPane
        root.getChildren().add(settingsBox);
        StackPane.setAlignment(settingsBox, Pos.CENTER); // Ensure VBox is centered

        // Create the scene with preferred dimensions
        Scene settingsScene = new Scene(root, stage.getWidth(), stage.getHeight()); // Adjust width and height as needed

        // Set the scene on the stage
        stage.setScene(settingsScene);
        stage.show();
    }
}
