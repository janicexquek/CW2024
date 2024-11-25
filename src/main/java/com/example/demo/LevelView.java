package com.example.demo;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.InputStream;

public class LevelView {

	private static final double HEART_DISPLAY_X_POSITION = 5;
	private static final double HEART_DISPLAY_Y_POSITION = 25;
	private static final double EXIT_DISPLAY_X_POSITION = 1200; // Adjust based on your screen width
	private static final double EXIT_DISPLAY_Y_POSITION = 25;

	private final Group root;
	private final HeartDisplay heartDisplay;
	private final ExitDisplay exitDisplay;
	private final ExitOverlay exitOverlay; // New WinOverlay
	private final PauseOverlay pauseOverlay; // New member variable
	private final WinOverlay winOverlay; // New WinOverlay
	private final GameOverOverlay gameOverOverlay;
	private CountdownOverlay countdownOverlay;

	private Runnable startGameCallback;
	private Timeline timeline; // Reference to the game loop timeline
	private ActiveOverlay activeOverlay = ActiveOverlay.NONE;
	private Text infoDisplay; // New Text node for displaying information


	public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight, Timeline timeline) {
		this.root = root;
		this.timeline = timeline;
		this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
		this.exitOverlay = new ExitOverlay(screenWidth, screenHeight, resumeGameCallback, backToMainMenuCallback, this::hideExitOverlay);
		this.exitDisplay = new ExitDisplay(EXIT_DISPLAY_X_POSITION, EXIT_DISPLAY_Y_POSITION,
				pauseGameCallback, resumeGameCallback, backToMainMenuCallback, this::showExitOverlay);

		// Initialize the PauseOverlay with screen dimensions
		this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, pauseGameCallback::run);
		this.winOverlay = new WinOverlay(screenWidth, screenHeight); // Initialize WinOverlay
		this.gameOverOverlay = new GameOverOverlay(screenWidth, screenHeight); // Initialize WinOverlay
		// Initialize the CountdownOverlay
		this.countdownOverlay = new CountdownOverlay(screenWidth, screenHeight, this::onCountdownFinished);
		// Initialize the infoDisplay Text node
		this.infoDisplay = new Text();
		// Load the custom font
		loadCustomFont();
		root.getChildren().addAll(exitOverlay,pauseOverlay, winOverlay, gameOverOverlay, countdownOverlay);
		root.getChildren().add(infoDisplay);
		infoDisplay.toFront(); // Bring infoDisplay to the front
		positionInfoDisplay();
	}
	private void loadCustomFont() {
		// Adjusted font loading code to handle spaces in file name
		String fontPath = "/com/example/demo/fonts/Pixel Digivolve.otf";
		InputStream fontStream = getClass().getResourceAsStream(fontPath);

		if (fontStream == null) {
			System.out.println("Font file not found at: " + fontPath);
		} else {
			Font font = Font.loadFont(fontStream, 20);
			if (font == null) {
				System.out.println("Failed to load font from: " + fontPath);
			} else {
				System.out.println("Loaded font: " + font.getName());
				this.infoDisplay.setFont(font);
			}
		}

		// Set default font if custom font fails to load
		if (this.infoDisplay.getFont() == null) {
			this.infoDisplay.setFont(Font.font("Verdana", 20));
		}
	}

	private void positionInfoDisplay() {
		Platform.runLater(() -> {
			Bounds heartBounds = heartDisplay.getContainer().getBoundsInParent();
			double heartRightX = heartBounds.getMaxX();
			double heartY = heartBounds.getMinY();

			infoDisplay.setX(heartRightX + 10); // Adjust as needed
			infoDisplay.setY(heartY + 40); // Align vertically with the heart display
		});
	}


	// New state variable to track active overlay
	public static enum ActiveOverlay {
		NONE,
		PAUSE,
		WIN,
		GAME_OVER,
		COUNTDOWN,
		EXIT
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

	public void showExitOverlay() {
		if (activeOverlay == ActiveOverlay.NONE) {
			exitOverlay.showExitOverlay();
			activeOverlay = ActiveOverlay.EXIT;
		}
	}

	public void hideExitOverlay() {
		if (activeOverlay == ActiveOverlay.EXIT) {
			exitOverlay.hideExitOverlay();
			activeOverlay = ActiveOverlay.NONE;
		}
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
			return;
		}
			pauseOverlay.setVisible(false);
			pauseOverlay.setMouseTransparent(true); // Disable interactions when not visible
			activeOverlay = ActiveOverlay.NONE;
	}

	// Method to show the WinOverlay with custom buttons
	public void showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback, Runnable restartCallback,
							   String levelName, long currentTimeSeconds, long fastestTimeSeconds, String achievementMessage) {
		if (activeOverlay == ActiveOverlay.NONE) {
			winOverlay.initializeButtons(backToMainMenuCallback, nextLevelCallback, restartCallback, levelName);
			// Format times
			String currentTime = formatTime(currentTimeSeconds);
			String fastestTime = formatTime(fastestTimeSeconds);
			winOverlay.setTimes(currentTime, fastestTime);
			winOverlay.setAchievementMessage(achievementMessage); // Set the achievement message
			winOverlay.showWInOverlay();
			activeOverlay = ActiveOverlay.WIN;
		}
	}

	// Method to hide the WinOverlay
	public void hideWinOverlay() {
		if (activeOverlay != ActiveOverlay.WIN) {
			return;
		}
			winOverlay.hideWinOverlay();
			activeOverlay = ActiveOverlay.NONE;
	}

	// Method to show the GameOverOverlay with custom buttons
	public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback,
									String levelName, long currentTimeSeconds, long fastestTimeSeconds) {
		if (activeOverlay == ActiveOverlay.NONE) {
			gameOverOverlay.initializeButtons(backToMainMenuCallback, restartCallback, levelName);
			// Format times
			String currentTime = formatTime(currentTimeSeconds);
			String fastestTime = formatTime(fastestTimeSeconds);
			gameOverOverlay.setTimes(currentTime,fastestTime);
			gameOverOverlay.showOverlay();
			activeOverlay = ActiveOverlay.GAME_OVER;
		}
	}

	// Method to hide the WinOverlay
	public void hideGameOverOverlay() {
		if (activeOverlay != ActiveOverlay.GAME_OVER) {
			return;
		}
			gameOverOverlay.hideOverlay();
			activeOverlay = ActiveOverlay.NONE;
	}

	// Method to update kill count for Level One
	public void updateKillCount(int currentKills, int killsToAdvance) {
		Platform.runLater(() -> {
			this.infoDisplay.setText("Kills: " + currentKills + " / " + killsToAdvance);
		});
	}

	// Method to update boss health for Level Two
	public void updateBossHealth(int currentHealth) {
		Platform.runLater(() -> {
			this.infoDisplay.setText("Boss Health: " + currentHealth);
		});
	}
	// Method to update wave for Level Three
	public void updateCustomInfo(String info) {
		Platform.runLater(() -> {
			this.infoDisplay.setText(info);
		});
	}

	public void bringInfoDisplayToFront() {
		infoDisplay.toFront();
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
	// Helper method to format time from seconds to MM:SS
	private String formatTime(long totalSeconds) {
		long minutes = totalSeconds / 60;
		long seconds = totalSeconds % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

}
