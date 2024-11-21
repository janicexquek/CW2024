package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.InputStream; // Ensure this import is present

public class CountdownOverlay extends StackPane {

    private Label countdownLabel;
    private Timeline timeline;
    private Runnable onCountdownFinished;

    public CountdownOverlay(double screenWidth, double screenHeight, Runnable onCountdownFinished) {
        this.onCountdownFinished = onCountdownFinished;

        // Set size to cover the entire screen
        setPrefSize(screenWidth, screenHeight);
        setMaxSize(screenWidth, screenHeight);
        setMinSize(screenWidth, screenHeight);

        // Semi-transparent dark background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        // Initialize countdown label
        countdownLabel = new Label("Game starts in 3");
        countdownLabel.setTextFill(Color.WHITE);
        countdownLabel.setAlignment(Pos.CENTER);

        // Load and set custom font
        loadCustomFont();

        // Add a background to the label to ensure visibility
        countdownLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-padding: 20px;");

        getChildren().add(countdownLabel);
        setAlignment(Pos.CENTER);

        // Initially hidden
        setVisible(false);
        setMouseTransparent(true);
    }

    private void loadCustomFont() {
        try {
            // Ensure the font file is in the correct path: src/main/resources/com/example/demo/fonts/Pixel Digivolve.otf
            InputStream fontStream = getClass().getResourceAsStream("/com/example/demo/fonts/Pixel Digivolve.otf");
            if (fontStream == null) {
                System.err.println("Font resource not found: /com/example/demo/fonts/Pixel Digivolve.otf");
                countdownLabel.setFont(new Font("Arial", 50)); // Fallback font
                return;
            }
            Font customFont = Font.loadFont(fontStream, 50);
            if (customFont != null) {
                countdownLabel.setFont(customFont);
            } else {
                // Fallback font if custom font fails to load
                countdownLabel.setFont(new Font("Arial", 50));
                System.err.println("Failed to load custom font. Using default font.");
            }
        } catch (Exception e) {
            countdownLabel.setFont(new Font("Arial", 50));
            System.err.println("Exception while loading custom font: " + e.getMessage());
        }
    }

    public void startCountdown() {
        setVisible(true);
        setMouseTransparent(false);
        toFront(); // Ensure the overlay is on top

        SettingsManager.getInstance().playCountdownSound();

        // Define countdown messages
        String[] messages = {
                "Game starts in 3",
                "Game starts in 2",
                "Game starts in 1",
                "Game start"
        };

        // Initialize timeline
        timeline = new Timeline();
        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(i),
                    event -> {
                        countdownLabel.setText(messages[index]);
//                        MusicManager.getInstance().playCountdownSound();
//                        System.out.println("Countdown message: " + messages[index]);
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // After countdown, hide overlay and trigger callback
        timeline.setOnFinished(event -> {
            hideCountdown();
            if (onCountdownFinished != null) {
                onCountdownFinished.run();
            }
        });

        timeline.play();
    }

    public void hideCountdown() {
        setVisible(false);
        setMouseTransparent(true);
    }
}
