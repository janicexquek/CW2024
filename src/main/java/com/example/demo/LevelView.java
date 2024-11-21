package com.example.demo;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;

public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final double EXIT_DISPLAY_X_POSITION = 1200; // Adjust based on your screen width
	private static final double EXIT_DISPLAY_Y_POSITION = 25;

	private final Group root;
	private final HeartDisplay heartDisplay;
	private final ExitDisplay exitDisplay;
	private final PauseOverlay pauseOverlay; // New member variable
	private final WinOverlay winOverlay; // New WinOverlay
	private final GameOverOverlay gameOverOverlay;
	private CountdownOverlay countdownOverlay;

	private Runnable startGameCallback;
	private Timeline timeline; // Reference to the game loop timeline
	private ActiveOverlay activeOverlay = ActiveOverlay.NONE;


	public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight, Timeline timeline) {
		this.root = root;
		this.timeline = timeline;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.exitDisplay = new ExitDisplay(EXIT_DISPLAY_X_POSITION, EXIT_DISPLAY_Y_POSITION, pauseGameCallback, resumeGameCallback, backToMainMenuCallback);

		// Initialize the PauseOverlay with screen dimensions
		this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, pauseGameCallback::run);
		this.winOverlay = new WinOverlay(screenWidth, screenHeight); // Initialize WinOverlay
		this.gameOverOverlay = new GameOverOverlay(screenWidth, screenHeight); // Initialize WinOverlay
		// Initialize the CountdownOverlay
		this.countdownOverlay = new CountdownOverlay(screenWidth, screenHeight, this::onCountdownFinished);

		root.getChildren().addAll(pauseOverlay, winOverlay, gameOverOverlay, countdownOverlay);
	}

	// New state variable to track active overlay
	public static enum ActiveOverlay {
		NONE,
		PAUSE,
		WIN,
		GAME_OVER,
		COUNTDOWN
	}

	// Method to start the countdown
	public void startCountdown(Runnable startGameCallback) {
		if (activeOverlay != ActiveOverlay.NONE) {
			return;
		}
		this.startGameCallback = startGameCallback;

		// Pause the game loop
		if (timeline != null) {
			timeline.pause();
		}

		// Apply blur effect to all nodes except the countdownOverlay
		root.getChildren().forEach(node -> {
			if (node != countdownOverlay) {
				node.setEffect(new GaussianBlur(10));
			}
		});

		// Start the countdown
		countdownOverlay.startCountdown();
		activeOverlay = ActiveOverlay.COUNTDOWN;
		SettingsManager.getInstance().muteAllSoundEffects();

	}

	// Callback when countdown finishes
	private void onCountdownFinished() {
			if (activeOverlay != ActiveOverlay.COUNTDOWN) {
			return;
		}
		// Remove blur effect
		root.getChildren().forEach(node -> {
			if (node != countdownOverlay) {
				node.setEffect(null);
			}
		});

		// Resume the game loop
		if (timeline != null) {
			timeline.play();
		}

		// Execute the game start callback
		if (startGameCallback != null) {
			startGameCallback.run();
		}
		SettingsManager.getInstance().unmuteAllSoundEffects();
		activeOverlay = ActiveOverlay.NONE;
	}

	public void showHeartDisplay() {
		root.getChildren().add(heartDisplay.getContainer());
	}

	public void showExitDisplay() {
		root.getChildren().add(exitDisplay.getContainer());
	}


	public void removeHearts(int heartsRemaining) {
		int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
		for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
			heartDisplay.removeHeart();
		}
	}

	// Shows the pause overlay.
	public void showPauseOverlay() {
		if (activeOverlay == ActiveOverlay.NONE) {
			pauseOverlay.setVisible(true);
			pauseOverlay.setMouseTransparent(false); // Allow interactions with the overlay
			pauseOverlay.toFront(); // Bring to front
			activeOverlay = ActiveOverlay.PAUSE;
		}
	}

	// Hides the pause overlay.
	public void hidePauseOverlay() {
		if (activeOverlay != ActiveOverlay.PAUSE) {
			pauseOverlay.setVisible(false);
			pauseOverlay.setMouseTransparent(true); // Disable interactions when not visible
			activeOverlay = ActiveOverlay.NONE;
		}
	}

	// Method to show the WinOverlay with custom buttons
	public void showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback, String levelName) {
		if (activeOverlay != ActiveOverlay.NONE) {
			winOverlay.initializeButtons(backToMainMenuCallback, nextLevelCallback, levelName);
			winOverlay.showWInOverlay();
			activeOverlay = ActiveOverlay.WIN;
		}
	}

	// Method to hide the WinOverlay
	public void hideWinOverlay() {
		if (activeOverlay != ActiveOverlay.WIN) {
			winOverlay.hideWinOverlay();
			activeOverlay = ActiveOverlay.NONE;
		}
	}

	// Method to show the GameOverOverlay with custom buttons
	public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback, String levelName) {
		if (activeOverlay == ActiveOverlay.NONE) {
			gameOverOverlay.initializeButtons(backToMainMenuCallback, restartCallback, levelName);
			gameOverOverlay.showOverlay();
			activeOverlay = ActiveOverlay.GAME_OVER;
		}
	}

	// Method to hide the WinOverlay
	public void hideGameOverOverlay() {
		if (activeOverlay != ActiveOverlay.GAME_OVER) {
			gameOverOverlay.hideOverlay();
			activeOverlay = ActiveOverlay.NONE;
		}
	}

	// Inside LevelView class
	public ActiveOverlay getActiveOverlay() {
		return activeOverlay;
	}
	public PauseOverlay getPauseOverlay() {
		return pauseOverlay;
	}
	public WinOverlay getWinOverlay() {
		return winOverlay;
	}
	public GameOverOverlay getGameOverOverlay() {
		return gameOverOverlay;
	}

}
