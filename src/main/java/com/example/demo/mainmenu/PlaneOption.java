package com.example.demo.mainmenu;

import com.example.demo.styles.FontManager;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Class representing a single plane option in the store.
 * Handles UI components and selection logic for the plane.
 */
public class PlaneOption extends StackPane {
    private final FontManager fontManager;
    private final String planeImageName;
    private final int planeNumber;
    private final Consumer<Integer> onSelectCallback;

    private final Rectangle hoverOverlay;
    private final Rectangle selectionBorder;

    /**
     * Constructor for PlaneOption.
     *
     * @param planeImageName   The filename of the plane image.
     * @param onSelectCallback Callback function to notify when this plane is selected.
     */
    public PlaneOption(String planeImageName, Consumer<Integer> onSelectCallback) {
        this.fontManager = FontManager.getInstance();
        this.planeImageName = planeImageName;
        this.planeNumber = extractPlaneNumber(planeImageName);
        this.onSelectCallback = onSelectCallback;

        // Initialize UI Components
        ImageView planeImageView = createPlaneImageView();
        hoverOverlay = createHoverOverlay();
        selectionBorder = createSelectionBorder();
        Label planeNumberLabel = createPlaneNumberLabel();

        // Add components to StackPane
        this.getChildren().addAll(planeImageView, hoverOverlay, selectionBorder, planeNumberLabel);
        this.setAlignment(Pos.CENTER);
        this.setCursor(Cursor.HAND);

        // Initialize Styles and Effects
        initializeEffects();
    }

    /**
     * Creates the ImageView for the plane.
     *
     * @return Configured ImageView.
     */
    private ImageView createPlaneImageView() {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/" + planeImageName)).toExternalForm()));
        imageView.setFitWidth(150); // Adjust as needed
        imageView.setFitHeight(150); // Adjust as needed
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    /**
     * Creates the hover overlay rectangle.
     *
     * @return Configured Rectangle.
     */
    private Rectangle createHoverOverlay() {
        Rectangle rect = new Rectangle(190, 190);
        rect.setFill(Color.web("#5b6980", 0.4)); // Grey with 40% opacity
        rect.setVisible(false);
        return rect;
    }

    /**
     * Creates the selection border rectangle.
     *
     * @return Configured Rectangle.
     */
    private Rectangle createSelectionBorder() {
        Rectangle rect = new Rectangle(190, 190);
        rect.setFill(Color.web("#5b6980", 0.4)); // Grey with 40% opacity
        rect.setStroke(Color.web("#5b6980", 0.4)); // Same color as fill
        rect.setStrokeWidth(3);
        rect.setVisible(false);
        return rect;
    }

    /**
     * Creates the label displaying the plane number.
     *
     * @return Configured Label.
     */
    private Label createPlaneNumberLabel() {
        Label label = new Label(String.valueOf(planeNumber));
        label.setTextFill(Color.WHITE);
        label.setFont(fontManager.getFont("Pixel Digivolve", 20));
        label.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(label, Pos.TOP_LEFT);
        StackPane.setMargin(label, new Insets(5, 0, 0, 10)); // Adjust margins as needed
        return label;
    }

    /**
     * Initializes hover and selection effects.
     */
    private void initializeEffects() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), this);

        // Hover Effects
        this.setOnMouseEntered(e -> {
            hoverOverlay.setVisible(true);
            scaleTransition.stop();
            scaleTransition.setFromX(this.getScaleX());
            scaleTransition.setFromY(this.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        this.setOnMouseExited(e -> {
            hoverOverlay.setVisible(false);
            scaleTransition.stop();
            scaleTransition.setFromX(this.getScaleX());
            scaleTransition.setFromY(this.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });

        // Click Event for Selection
        this.setOnMouseClicked(e -> {
            // Notify the StorePage about the selection
            onSelectCallback.accept(planeNumber);
            // Optionally, add a scale transition for visual feedback
            ScaleTransition selectScale = new ScaleTransition(Duration.millis(200), this);
            selectScale.setToX(1.1);
            selectScale.setToY(1.1);
            selectScale.play();

            // Reset scale after the transition
            selectScale.setOnFinished(event -> {
                this.setScaleX(1.0);
                this.setScaleY(1.0);
            });
        });
    }

    /**
     * Extracts the plane number from the image filename.
     *
     * @param planeImageName The plane image filename.
     * @return The extracted plane number.
     */
    private int extractPlaneNumber(String planeImageName) {
        if (planeImageName.equalsIgnoreCase("userplane.png")) {
            return 1;
        }
        // Extract number from filename, e.g., "userplane3.png" -> 4
        String numberPart = planeImageName.replaceAll("[^0-9]", "");
        if (!numberPart.isEmpty()) {
            try {
                return Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                // If parsing fails, default to 1
                return 1;
            }
        }
        return 1; // Default plane number
    }

    /**
     * Selects this plane option, showing the selection border.
     */
    public void select() {
        selectionBorder.setVisible(true);
    }

    /**
     * Deselects this plane option, hiding the selection border.
     */
    public void deselect() {
        selectionBorder.setVisible(false);
    }

    /**
     * Returns the plane number.
     *
     * @return Plane number.
     */
    public int getPlaneNumber() {
        return planeNumber;
    }
}
