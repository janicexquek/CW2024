package com.example.demo;

import com.example.demo.shield.BossShieldImage;
import javafx.animation.Timeline;
import javafx.scene.Group;

public class LevelViewLevelTwo extends LevelView {

	private static final int SHIELD_X_POSITION = 1150;
	private static final int SHIELD_Y_POSITION = 500;
	private final Group root;
	private final BossShieldImage bossShieldImage;

	public LevelViewLevelTwo(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight, Timeline timeline) {
		super(root, heartsToDisplay, backToMainMenuCallback, pauseGameCallback, resumeGameCallback, screenWidth, screenHeight, timeline);
		this.root = root;
		this.bossShieldImage = new BossShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
		addImagesToRoot();
	}
	
	private void addImagesToRoot() {
		root.getChildren().addAll(bossShieldImage);
	}
	
	public void showShield() {
		bossShieldImage.showShield();
	}

	public void hideShield() {
		bossShieldImage.hideShield();
	}

}
