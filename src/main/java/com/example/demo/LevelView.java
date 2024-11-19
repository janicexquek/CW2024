package com.example.demo;

import javafx.scene.Group;

public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final double EXIT_DISPLAY_X_POSITION = 1200; // Adjust based on your screen width
	private static final double EXIT_DISPLAY_Y_POSITION = 25;
	private static final int WIN_IMAGE_X_POSITION = 355;
	private static final int WIN_IMAGE_Y_POSITION = 175;
	private static final int LOSS_SCREEN_X_POSITION = -160;
	private static final int LOSS_SCREEN_Y_POSISITION = -375;

	private final Group root;
	private final WinImage winImage;
	private final GameOverImage gameOverImage;
	private final HeartDisplay heartDisplay;
	private final ExitDisplay exitDisplay;
	private final PauseOverlay pauseOverlay; // New member variable
	private final WinOverlay winOverlay; // New WinOverlay


	public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight) {
		this.root = root;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.exitDisplay = new ExitDisplay(EXIT_DISPLAY_X_POSITION, EXIT_DISPLAY_Y_POSITION, pauseGameCallback, resumeGameCallback, backToMainMenuCallback);
		this.winImage = new WinImage(WIN_IMAGE_X_POSITION, WIN_IMAGE_Y_POSITION);
		this.gameOverImage = new GameOverImage(LOSS_SCREEN_X_POSITION, LOSS_SCREEN_Y_POSISITION);
		// Initialize the PauseOverlay with screen dimensions
		this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, pauseGameCallback::run);
		this.winOverlay = new WinOverlay(screenWidth, screenHeight); // Initialize WinOverlay

		// Add the PauseOverlay to the root; it should be on top of other elements
		this.root.getChildren().add(pauseOverlay);
		this.root.getChildren().add(winOverlay);
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showExitDisplay() {
		root.getChildren().add(exitDisplay.getContainer());
	}

	public void showWinImage() {
		root.getChildren().add(winImage);
		winImage.showWinImage();
	}

	public void showGameOverImage() {
		root.getChildren().add(gameOverImage);
	}

	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}


	// Shows the pause overlay.
	public void showPauseOverlay() {
		System.out.println("Showing PauseOverlay");
		pauseOverlay.setVisible(true);
		pauseOverlay.setMouseTransparent(false); // Allow interactions with the overlay
		pauseOverlay.toFront(); // Bring to front explicitly
	}

	// Hides the pause overlay.
	public void hidePauseOverlay() {
		System.out.println("Hiding PauseOverlay");
		pauseOverlay.setVisible(false);
		pauseOverlay.setMouseTransparent(true); // Disable interactions when not visible
	}
	// Method to show the WinOverlay with custom buttons
	public void showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback) {
		winOverlay.initializeButtons(backToMainMenuCallback, nextLevelCallback);
		winOverlay.showOverlay();
	}

	// Method to hide the WinOverlay
	public void hideWinOverlay() {
		winOverlay.hideOverlay();
		// Remove buttons from the overlay to prevent duplication
		// This is handled inside the WinOverlay's hideOverlay method or you can modify as needed
	}
}
