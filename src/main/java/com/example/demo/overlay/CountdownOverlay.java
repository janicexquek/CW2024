// File: com/example/demo/overlay/CountdownOverlay.java

package com.example.demo.overlay;

import com.example.demo.mainmenumanager.SettingsManager;
import com.example.demo.styles.FontManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Class representing a countdown overlay in the game.
 * Manages the display and interactions of the countdown overlay.
 */
public class CountdownOverlay extends StackPane {

    private Label countdownLabel;
    private Timeline timeline;
    private Runnable onCountdownFinished;

    private final FontManager fontManager;

    /**
     * Constructor for CountdownOverlay.
     *
     * @param screenWidth          the width of the screen
     * @param screenHeight         the height of the screen
     * @param onCountdownFinished  the callback to run when the countdown finishes
     */
    public CountdownOverlay(double screenWidth, double screenHeight, Runnable onCountdownFinished) {
        this.onCountdownFinished = onCountdownFinished;
        this.fontManager = FontManager.getInstance();

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
        countdownLabel.setFont(fontManager.getFont("Pixel Digivolve", 50));

        // Add a background to the label to ensure visibility
        countdownLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-padding: 20px;");

        getChildren().add(countdownLabel);
        setAlignment(Pos.CENTER);

        // Initially hidden
        setVisible(false);
        setMouseTransparent(true);
    }

    /**
     * Starts the countdown and displays the overlay.
     * Plays a countdown sound and updates the label text at each second.
     */
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
                    event -> countdownLabel.setText(messages[index])
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

    /**
     * Hides the countdown overlay and makes it non-interactive.
     */
    public void hideCountdown() {
        setVisible(false);
        setMouseTransparent(true);
    }
}