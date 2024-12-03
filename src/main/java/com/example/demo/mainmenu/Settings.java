package com.example.demo.mainmenu;

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
import java.util.Objects;

/**
 * Class representing the settings page in the game.
 * Manages the display and interactions of the settings page.
 */
public class Settings {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpeg";
    private static final String AUDIO_ON_IMAGE = "/com/example/demo/images/Audio_ON.png";
    private static final String AUDIO_OFF_IMAGE = "/com/example/demo/images/Audio_Off.png";

    private final Stage stage; // Reference to the primary stage
    private final Controller controller; // Reference to the Controller

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    // Instance variables for sliders to enable/disable them based on mute state
    private Slider musicVolumeSlider;
    private Slider sfxVolumeSlider;
    private Slider countdownSfxVolumeSlider;

    /**
     * Constructor for Settings.
     *
     * @param stage the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public Settings(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        // Load custom fonts
        loadCustomFonts();
    }

    /**
     * Displays the settings page.
     */
    public void show() {
        // Load the background image using the static final String
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_NAME)).toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(stage.getWidth()); // Set to scene width
        backgroundImageView.setFitHeight(stage.getHeight()); // Set to scene height
        backgroundImageView.setPreserveRatio(false); // Stretch to fill

        // Initialize MusicManager instance
        SettingsManager settingsManager = SettingsManager.getInstance();

        // --- Back Button ---
        StackPane backButton = createCustomSettingsButton("Back", 80, 30);
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
        titleLabel.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 100));
        titleLabel.getStyleClass().add("title-text");

        topHBox.getChildren().add(titleLabel);

        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        // --- Labels VBox ---
        VBox labelsBox = new VBox(30);
        labelsBox.setAlignment(Pos.CENTER_LEFT);

        // Create labels
        Label backgroundEffectsLabel = new Label("Background Music:");
        Label soundEffectsLabel = new Label("Sound Effects:");
        Label countdownSoundEffectsLabel = new Label("Countdown Music:");

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
        musicVolumeSlider = new Slider(0, 1, settingsManager.getMusicVolume());
        musicVolumeSlider.setShowTickLabels(true);
        musicVolumeSlider.setMajorTickUnit(0.2);
        musicVolumeSlider.setBlockIncrement(0.05);
        musicVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's music volume
        musicVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> settingsManager.setMusicVolume(newValue.doubleValue()));

        // --- Sound Effects Volume Controls ---
        sfxVolumeSlider = new Slider(0, 1, settingsManager.getSoundEffectVolume());
        sfxVolumeSlider.setShowTickLabels(true);
        sfxVolumeSlider.setMajorTickUnit(0.2);
        sfxVolumeSlider.setBlockIncrement(0.05);
        sfxVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's sound effect volume
        sfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> settingsManager.setSoundEffectVolume(newValue.doubleValue()));

        // --- Countdown Sound Effects Volume Controls ---
        countdownSfxVolumeSlider = new Slider(0, 1, settingsManager.getCountdownSoundVolume());
        countdownSfxVolumeSlider.setShowTickLabels(true);
        countdownSfxVolumeSlider.setMajorTickUnit(0.2);
        countdownSfxVolumeSlider.setBlockIncrement(0.05);
        countdownSfxVolumeSlider.setPrefWidth(300);

        // Bind the slider value to MusicManager's countdown sound effect volume
        countdownSfxVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> settingsManager.setCountdownSoundVolume(newValue.doubleValue()));

        // --- Mute All Toggle Button ---
        ImageView muteToggleImageView = new ImageView();
        updateMuteToggleImage(muteToggleImageView, settingsManager.isAllMuted());

        // Set initial size for the toggle button
        muteToggleImageView.setFitWidth(50);
        muteToggleImageView.setFitHeight(50);
        muteToggleImageView.setPreserveRatio(true);

        // Create a StackPane to hold the ImageView
        StackPane muteToggleButton = new StackPane(muteToggleImageView);
        muteToggleButton.setAlignment(Pos.CENTER);
        muteToggleButton.setPrefSize(50, 50);
        muteToggleButton.setMaxSize(50, 50);
        muteToggleButton.setMinSize(50, 50);
        muteToggleButton.getStyleClass().add("custom-button-hover");

        // Add hover effects
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), muteToggleButton);

        muteToggleButton.setOnMouseEntered(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(muteToggleButton.getScaleX());
            scaleTransition.setFromY(muteToggleButton.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        muteToggleButton.setOnMouseExited(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(muteToggleButton.getScaleX());
            scaleTransition.setFromY(muteToggleButton.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });

        // Handle click event to toggle mute
        muteToggleButton.setOnMouseClicked(e -> {
            settingsManager.toggleMuteAll();
            updateMuteToggleImage(muteToggleImageView, settingsManager.isAllMuted());
            updateSlidersState();
        });

        // Create an HBox for the "Mute All" label and toggle button
        HBox muteAllHBox = new HBox(135);
        muteAllHBox.setAlignment(Pos.CENTER_LEFT);
        muteAllHBox.setPadding(new Insets(0, 0, 0, 40)); // top, right, bottom, left

        // Create the "Mute All" label
        Label muteAllLabel = new Label("Mute All:");
        muteAllLabel.setTextFill(Color.DARKGRAY);
        muteAllLabel.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 25)).getName(), 25));

        // Add the label and toggle button to the HBox
        muteAllHBox.getChildren().addAll(muteAllLabel, muteToggleButton);

        // Add the "Mute All" HBox to controlsBox
        controlsBox.getChildren().addAll(musicVolumeSlider, sfxVolumeSlider, countdownSfxVolumeSlider);

        // --- Content HBox containing Labels and Controls ---
        HBox contentHBox = new HBox(50);
        contentHBox.setAlignment(Pos.CENTER);
        contentHBox.getChildren().addAll(labelsBox, controlsBox);

        // --- Content all boxes containing contentHBox and muteAllHBox
        VBox allVBox = new VBox(10);
        allVBox.setAlignment(Pos.CENTER);
        allVBox.getChildren().addAll(contentHBox, muteAllHBox);

        // --- Defaults Button ---
        StackPane defaultsButton = createCustomSettingsButton("Defaults", 120, 40);
        defaultsButton.setOnMouseClicked(e -> {
            // Reset to default settings
            settingsManager.setMusicVolume(SettingsManager.DEFAULT_MUSIC_VOLUME);
            settingsManager.setSoundEffectVolume(SettingsManager.DEFAULT_SOUND_EFFECT_VOLUME);
            settingsManager.setCountdownSoundVolume(SettingsManager.DEFAULT_COUNTDOWN_SOUND_VOLUME);
            musicVolumeSlider.setValue(SettingsManager.DEFAULT_MUSIC_VOLUME);
            sfxVolumeSlider.setValue(SettingsManager.DEFAULT_SOUND_EFFECT_VOLUME);
            countdownSfxVolumeSlider.setValue(SettingsManager.DEFAULT_COUNTDOWN_SOUND_VOLUME);
        });

        // --- Bottom HBox containing Save and Defaults Buttons ---
        HBox bottomHBox = new HBox(20);
        bottomHBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomHBox.getChildren().addAll(defaultsButton);

        // ---- Second Main VBox contained content box and bottom box
        VBox secondmainVBox = new VBox(80);
        secondmainVBox.setAlignment(Pos.CENTER);
        secondmainVBox.setPadding(new Insets(20));
        secondmainVBox.setMaxWidth(650);
        secondmainVBox.setPrefHeight(400); // Set to desired height
        secondmainVBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 10;");
        secondmainVBox.getChildren().addAll(allVBox, bottomHBox);

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

        // Initialize sliders state based on current mute state
        updateSlidersState();
    }

    /**
     * Creates a custom settings button with an image and text.
     *
     * @param text the text to display on the button
     * @param width the width of the button
     * @param height the height of the button
     * @return the created StackPane representing the button
     */
    private StackPane createCustomSettingsButton(String text, double width, double height) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/ButtonText_Small_Round.png")).toExternalForm()));
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

    /**
     * Updates the mute toggle image based on the mute state.
     *
     * @param imageView the ImageView to update
     * @param isMuted the current mute state
     */
    private void updateMuteToggleImage(ImageView imageView, boolean isMuted) {
        String imagePath = isMuted ? AUDIO_OFF_IMAGE : AUDIO_ON_IMAGE;
        Image muteImage = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        imageView.setImage(muteImage);
    }

    /**
     * Enables or disables sliders based on the mute state.
     */
    private void updateSlidersState() {
        SettingsManager settingsManager = SettingsManager.getInstance();
        boolean isMuted = settingsManager.isAllMuted();

        // Disable sliders if muted, enable if not
        musicVolumeSlider.setDisable(isMuted);
        sfxVolumeSlider.setDisable(isMuted);
        countdownSfxVolumeSlider.setDisable(isMuted);
    }

    /**
     * Loads custom fonts from resources.
     */
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
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }
}