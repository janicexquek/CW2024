// File: com/example/demo/style/ButtonFactory.java

package com.example.demo.styles;

import com.example.demo.styles.FontManager;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;

public class ButtonFactory {

    private final FontManager fontManager;

    public ButtonFactory() {
        this.fontManager = FontManager.getInstance();
    }

    /**
     * Creates a custom button with specified parameters.
     *
     * @param text         the text to display on the button
     * @param fontName     the name of the font to use
     * @param fontSize     the size of the font
     * @param width        the width of the button background image
     * @param height       the height of the button background image
     * @param imagePath    the path to the button background image
     * @return a StackPane representing the styled button
     */
    public StackPane createCustomButton(String text, String fontName, double fontSize, double width, double height, String imagePath) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm()));
        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        label.setFont(fontManager.getFont(fontName, fontSize));
        label.getStyleClass().add("button-label"); // Ensure your CSS has styles for this class

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover"); // Ensure your CSS has styles for this class

        // Ensure the StackPane size matches the image size
        stackPane.setMinSize(width, height);
        stackPane.setMaxSize(width, height);

        // Prevent mouse events on transparent areas
        stackPane.setPickOnBounds(false);

        // Create a ScaleTransition for hover effects
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
     * Creates a sticker button with an image.
     *
     * @param imagePath the path to the sticker image
     * @param fitWidth  the width to fit the sticker image
     * @param fitHeight the height to fit the sticker image
     * @return a StackPane representing the sticker button
     */
    public StackPane createStickerButton(String imagePath, double fitWidth, double fitHeight) {
        // Load the button background image
        Image buttonImage = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());

        ImageView buttonImageView = new ImageView(buttonImage);
        buttonImageView.setFitWidth(fitWidth);
        buttonImageView.setFitHeight(fitHeight);
        buttonImageView.setPreserveRatio(false);

        // Create a Circle for hover effect
        Circle hoverCircle = new Circle();
        hoverCircle.setStroke(Color.web("#5b6980"));
        hoverCircle.setStrokeWidth(3.5);
        hoverCircle.setFill(Color.web("#5b6980"));
        hoverCircle.setOpacity(0);

        // Bind the Circle's radius to the ImageView's size for responsiveness
        hoverCircle.radiusProperty().bind(
                buttonImageView.fitWidthProperty().divide(2).add(5) // Adjust the "+5" for padding
        );
        // Center the Circle relative to the ImageView
        hoverCircle.centerXProperty().bind(buttonImageView.fitWidthProperty().divide(2));
        hoverCircle.centerYProperty().bind(buttonImageView.fitHeightProperty().divide(2));

        // Create a StackPane to layer the Circle and the ImageView
        StackPane stackPane = new StackPane(hoverCircle, buttonImageView);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");
        stackPane.setPickOnBounds(false); // Prevent mouse events on transparent areas

        // Create ScaleTransition for hover
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        // Create FadeTransitions for the Circle
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), hoverCircle);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), hoverCircle);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // Add hover event handlers
        stackPane.setOnMouseEntered(e -> {
            scaleTransition.stop();
            fadeOut.stop();

            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();

            fadeIn.playFromStart();
        });

        stackPane.setOnMouseExited(e -> {
            scaleTransition.stop();
            fadeIn.stop();

            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();

            fadeOut.playFromStart();
        });

        return stackPane;
    }
}
