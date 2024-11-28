package com.example.demo.shield;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UserShieldImage extends ImageView {

    private static final int SHIELD_SIZE = 100;

    public UserShieldImage(double xPosition, double yPosition) {
        this.setLayoutX(xPosition);
        this.setLayoutY(yPosition);
        Image shieldImg = new Image(getClass().getResource("/com/example/demo/images/usershield.png").toExternalForm());
        this.setImage(shieldImg);
        this.setVisible(false); // Initially hidden
        this.setFitHeight(SHIELD_SIZE);
        this.setFitWidth(SHIELD_SIZE);
        this.setPreserveRatio(true);
        this.setSmooth(true);
    }

    public void showShield() {
        this.setVisible(true);
    }

    public void hideShield() {
        this.setVisible(false);
    }
}

