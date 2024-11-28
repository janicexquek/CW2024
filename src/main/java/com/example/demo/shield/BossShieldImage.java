package com.example.demo.shield;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossShieldImage extends ImageView {

	private static final int SHIELD_SIZE = 200;

	public BossShieldImage(double xPosition, double yPosition) {
		this.setLayoutX(xPosition);
		this.setLayoutY(yPosition);
		Image shieldImg = new Image(getClass().getResource("/com/example/demo/images/shield.png").toExternalForm());
		this.setImage(shieldImg);
		this.setVisible(false);
		this.setFitHeight(SHIELD_SIZE);
		this.setFitWidth(SHIELD_SIZE);
	}

	public void showShield() {
		this.setVisible(true);
	}

	public void hideShield() {
		this.setVisible(false);
	}

}
