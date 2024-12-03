package com.example.demo.shield;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Class representing the user shield image in the game.
 * Manages the display and visibility of the user shield.
 */
public class UserShieldImage extends ImageView {

    private static final int SHIELD_SIZE = 100;

    /**
     * Constructor for UserShieldImage.
     *
     * @param xPosition the initial X position of the user shield
     * @param yPosition the initial Y position of the user shield
     */
    public UserShieldImage(double xPosition, double yPosition) {
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
        Image shieldImg = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/usershield.png")).toExternalForm());
        this.setImage(shieldImg);
        this.setVisible(false); // Initially hidden
        this.setFitHeight(SHIELD_SIZE);
        this.setFitWidth(SHIELD_SIZE);
        this.setPreserveRatio(true);
        this.setSmooth(true);
    }

    /**
     * Shows the user shield by setting its visibility to true.
     */
    public void showShield() {
        this.setVisible(true);
    }

    /**
     * Hides the user shield by setting its visibility to false.
     */
    public void hideShield() {
        this.setVisible(false);
    }
}