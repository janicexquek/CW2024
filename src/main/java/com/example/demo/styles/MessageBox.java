// File: com/example/demo/overlay/MessageBox.java

package com.example.demo.styles;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.Objects;

/**
 * Class representing a message box within an overlay.
 * Encapsulates the layout and styling of the message display area.
 */
public class MessageBox extends StackPane {

    private final FontManager fontManager;

    private Label titleLabel;
    private Label subtitleLabel;
    private Label achievementLabel;
    private Label timeLabel;
    private Label fastestTimeLabel;

    /**
     * Constructor for MessageBox.
     *
     * @param boxWidth        the width of the message box
     * @param boxHeight       the height of the message box
     * @param titleText       the main title text (e.g., "VICTORY!" or "GAME OVER")
     * @param subtitleText    the subtitle text (e.g., "LEVEL X COMPLETED")
     * @param achievementText optional achievement message (can be null)
     * @param timeText        the current time text
     * @param fastestTimeText the fastest time text
     */
    public MessageBox(double boxWidth, double boxHeight, String titleText, String subtitleText,
                      String achievementText, String timeText, String fastestTimeText) {
        this.fontManager = FontManager.getInstance();

        // Load background image
        ImageView background;
        try {
            background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/box1.png")).toExternalForm()));
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        } catch (Exception e) {
            System.err.println("Failed to load MessageBox background image.");
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Initialize labels
        titleLabel = new Label(titleText);
        titleLabel.setTextFill(Color.web("#f5f551"));
        titleLabel.setFont(fontManager.getFont("Cartoon cookies", 50));
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(boxWidth - 40); // Padding

        subtitleLabel = new Label(subtitleText);
        subtitleLabel.setTextFill(Color.WHITE);
        subtitleLabel.setFont(fontManager.getFont("Sugar Bomb", 20));
        subtitleLabel.setWrapText(true);
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setTextAlignment(TextAlignment.CENTER);
        subtitleLabel.setMaxWidth(boxWidth - 40); // Padding

        if (achievementText != null && !achievementText.trim().isEmpty()) {
            achievementLabel = new Label(achievementText);
            achievementLabel.setTextFill(Color.web("#f5f551"));
            achievementLabel.setFont(fontManager.getFont("Sugar Bomb", 20));
            achievementLabel.setWrapText(true);
            achievementLabel.setAlignment(Pos.CENTER);
            achievementLabel.setMaxWidth(boxWidth - 40); // Padding
        }

        timeLabel = new Label(timeText);
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setFont(fontManager.getFont("Pixel Digivolve", 16));
        timeLabel.setWrapText(true);
        timeLabel.setAlignment(Pos.CENTER);
        timeLabel.setMaxWidth(boxWidth - 40); // Padding

        fastestTimeLabel = new Label(fastestTimeText);
        fastestTimeLabel.setTextFill(Color.WHITE);
        fastestTimeLabel.setFont(fontManager.getFont("Pixel Digivolve", 16));
        fastestTimeLabel.setWrapText(true);
        fastestTimeLabel.setAlignment(Pos.CENTER);
        fastestTimeLabel.setMaxWidth(boxWidth - 40); // Padding

        // Arrange labels in VBox
        VBox labelsBox = new VBox(20); // Spacing between labels
        labelsBox.setAlignment(Pos.CENTER);
        labelsBox.getChildren().addAll(titleLabel, subtitleLabel);
        if (achievementLabel != null) {
            labelsBox.getChildren().add(achievementLabel);
        }
        labelsBox.getChildren().addAll(timeLabel, fastestTimeLabel);

        // Add labelsBox to the message box
        getChildren().addAll(background, labelsBox);
        setAlignment(Pos.CENTER);
    }

    /**
     * Sets the subtitle text.
     *
     * @param text the new subtitle text
     */
    public void setSubtitleText(String text) {
        subtitleLabel.setText(text);
    }

    /**
     * Sets the achievement message.
     *
     * @param text the new achievement message
     */
    public void setAchievementText(String text) {
        if (achievementLabel != null) {
            achievementLabel.setText(text);
        }
    }

    /**
     * Sets the current time text.
     *
     * @param text the new current time text
     */
    public void setTimeText(String text) {
        timeLabel.setText(text);
    }

    /**
     * Sets the fastest time text.
     *
     * @param text the new fastest time text
     */
    public void setFastestTimeText(String text) {
        fastestTimeLabel.setText(text);
    }
}
