package com.example.demo.shield;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Class representing the boss shield image in the game.
 * Manages the display and visibility of the boss shield.
 */
public class BossShieldImage extends ImageView {

    private static final int SHIELD_SIZE = 200;

    /**
     * Constructor for BossShieldImage.
     *
     * @param xPosition the initial X position of the boss shield
     * @param yPosition the initial Y position of the boss shield
     */
    public BossShieldImage(double xPosition, double yPosition) {
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
        Image shieldImg = new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/shield.png")).toExternalForm());
        this.setImage(shieldImg);
        this.setVisible(false);
        this.setFitHeight(SHIELD_SIZE);
        this.setFitWidth(SHIELD_SIZE);
    }

    /**
     * Shows the boss shield by setting its visibility to true.
     */
    public void showShield() {
        this.setVisible(true);
    }

    /**
     * Hides the boss shield by setting its visibility to false.
     */
    public void hideShield() {
        this.setVisible(false);
    }
}