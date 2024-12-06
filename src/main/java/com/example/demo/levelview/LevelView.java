package com.example.demo.levelview;

import com.example.demo.display.ExitDisplay;
import com.example.demo.display.ExitOverlay;
import com.example.demo.display.HeartDisplay;
import com.example.demo.mainmenu.SettingsManager;
import com.example.demo.overlay.CountdownOverlay;
import com.example.demo.overlay.GameOverOverlay;
import com.example.demo.overlay.PauseOverlay;
import com.example.demo.overlay.WinOverlay;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.InputStream;

/**
 * Class representing the view for a game level.
 * Manages the display and overlays for the game level.
 */
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

    /**
     * Constructor for LevelView.
     *
     * @param root the root group of the scene
     * @param heartsToDisplay the number of hearts to display for the player's health
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param pauseGameCallback the callback to pause the game
     * @param resumeGameCallback the callback to resume the game
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param timeline the timeline for animations
     */
    public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight, Timeline timeline) {
        this.root = root;
        this.timeline = timeline;
        this.heartDisplay = new HeartDisplay(HEART_DISPLAY_X_POSITION, HEART_DISPLAY_Y_POSITION, heartsToDisplay);
        this.exitOverlay = new ExitOverlay(screenWidth, screenHeight, resumeGameCallback, backToMainMenuCallback, this::hideExitOverlay);
        this.exitDisplay = new ExitDisplay(EXIT_DISPLAY_X_POSITION, EXIT_DISPLAY_Y_POSITION, pauseGameCallback, this::showExitOverlay);

        // Initialize the Pause, Win, GameOver Overlay with screen dimensions
        this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, pauseGameCallback);
        this.winOverlay = new WinOverlay(screenWidth, screenHeight); // Initialize WinOverlay
        this.gameOverOverlay = new GameOverOverlay(screenWidth, screenHeight); // Initialize WinOverlay
        // Initialize the CountdownOverlay
        this.countdownOverlay = new CountdownOverlay(screenWidth, screenHeight, this::onCountdownFinished);
        // Initialize the infoDisplay Text node
        this.infoDisplay = new Text();
        // Load the custom font
        loadCustomFont();
        root.getChildren().addAll(exitOverlay, pauseOverlay, winOverlay, gameOverOverlay, countdownOverlay);
        root.getChildren().add(infoDisplay);
        infoDisplay.toFront(); // Bring infoDisplay to the front
        positionInfoDisplay();
    }

    /**
     * Loads a custom font for the info display.
     */
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
                this.infoDisplay.setFont(font);
            }
        }

        // Set default font if custom font fails to load
        if (this.infoDisplay.getFont() == null) {
            this.infoDisplay.setFont(Font.font("Verdana", 20));
        }
    }

    /**
     * Positions the info display on the screen.
     */
    private void positionInfoDisplay() {
        Platform.runLater(() -> {
            Bounds heartBounds = heartDisplay.getContainer().getBoundsInParent();
            double heartRightX = heartBounds.getMaxX();
            double heartY = heartBounds.getMinY();

            infoDisplay.setX(heartRightX + 10); // Adjust as needed
            infoDisplay.setY(heartY + 40); // Align vertically with the heart display
        });
    }

    /**
     * Formats time from seconds to MM:SS.
     *
     * @param totalSeconds the total seconds to format
     * @return the formatted time string
     */
    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Enum representing the active overlay state.
     */
    public enum ActiveOverlay {
        NONE,
        PAUSE,
        WIN,
        GAME_OVER,
        COUNTDOWN,
        EXIT
    }

    /**
     * Starts the countdown overlay.
     *
     * @param startGameCallback the callback to start the game
     */
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

    /**
     * Callback when countdown finishes.
     */
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

    /**
     * Shows the heart display.
     */
    public void showHeartDisplay() {
        root.getChildren().add(heartDisplay.getContainer());
    }

    /**
     * Removes hearts from the display.
     *
     * @param heartsRemaining the number of hearts remaining
     */
    public void removeHearts(int heartsRemaining) {
        int currentNumberOfHearts = heartDisplay.getContainer().getChildren().size();
        for (int i = 0; i < currentNumberOfHearts - heartsRemaining; i++) {
            heartDisplay.removeHeart();
        }
    }

    /**
     * Shows the exit overlay.
     */
    public void showExitOverlay() {
        if (activeOverlay == ActiveOverlay.NONE) {
            exitOverlay.showExitOverlay();
            activeOverlay = ActiveOverlay.EXIT;
        }
    }

    /**
     * Hides the exit overlay.
     */
    public void hideExitOverlay() {
        if (activeOverlay == ActiveOverlay.EXIT) {
            exitOverlay.hideExitOverlay();
            activeOverlay = ActiveOverlay.NONE;
        }
    }

    /**
     * Shows the exit display.
     */
    public void showExitDisplay() {
        root.getChildren().add(exitDisplay.getContainer());
    }

    /**
     * Shows the pause overlay.
     */
    public void showPauseOverlay() {
        if (activeOverlay == ActiveOverlay.NONE) {
            pauseOverlay.setVisible(true);
            pauseOverlay.setMouseTransparent(false); // Allow interactions with the overlay
            pauseOverlay.toFront(); // Bring to front
            activeOverlay = ActiveOverlay.PAUSE;
        }
    }

    /**
     * Hides the pause overlay.
     */
    public void hidePauseOverlay() {
        if (activeOverlay != ActiveOverlay.PAUSE) {
            return;
        }
        pauseOverlay.setVisible(false);
        pauseOverlay.setMouseTransparent(true); // Disable interactions when not visible
        activeOverlay = ActiveOverlay.NONE;
    }

    /**
     * Shows the win overlay with custom buttons.
     *
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param nextLevelCallback the callback to proceed to the next level
     * @param restartCallback the callback to restart the level
     * @param levelName the name of the level
     * @param currentTimeSeconds the current time in seconds
     * @param fastestTimeSeconds the fastest time in seconds
     * @param achievementMessage the achievement message
     */
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

    /**
     * Hides the win overlay.
     */
    public void hideWinOverlay() {
        if (activeOverlay != ActiveOverlay.WIN) {
            return;
        }
        winOverlay.hideWinOverlay();
        activeOverlay = ActiveOverlay.NONE;
    }

    /**
     * Shows the game over overlay with custom buttons.
     *
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param restartCallback the callback to restart the level
     * @param levelName the name of the level
     * @param currentTimeSeconds the current time in seconds
     */
    public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback,
                                    String levelName, long currentTimeSeconds, String fastestTimeDisplay) {
        if (activeOverlay == ActiveOverlay.NONE) {
            gameOverOverlay.initializeButtons(backToMainMenuCallback, restartCallback, levelName);
            // Format times
            String currentTime = formatTime(currentTimeSeconds);
            gameOverOverlay.setTimes(currentTime, fastestTimeDisplay);
            gameOverOverlay.showOverlay();
            activeOverlay = ActiveOverlay.GAME_OVER;
        }
    }

    /**
     * Hides the game over overlay.
     */
    public void hideGameOverOverlay() {
        if (activeOverlay != ActiveOverlay.GAME_OVER) {
            return;
        }
        gameOverOverlay.hideOverlay();
        activeOverlay = ActiveOverlay.NONE;
    }

    /**
     * Updates the kill count for Level One.
     *
     * @param currentKills the current number of kills
     * @param killsToAdvance the number of kills required to advance
     */
    public void updateKillCount(int currentKills, int killsToAdvance) {
        Platform.runLater(() -> this.infoDisplay.setText("Kills: " + currentKills + " / " + killsToAdvance));
    }

    /**
     * Updates the boss health for Level Two.
     *
     * @param currentHealth the current health of the boss
     */
    public void updateBossHealth(int currentHealth) {
        Platform.runLater(() -> this.infoDisplay.setText("Boss Health: " + currentHealth));
    }

    /**
     * Updates custom information for Level Three and Level Four.
     *
     * @param info the custom information to display
     */
    public void updateCustomInfo(String info) {
        Platform.runLater(() -> this.infoDisplay.setText(info));
    }

    /**
     * Brings the info display to the front.
     */
    public void bringInfoDisplayToFront() {
        infoDisplay.toFront();
    }

    /**
     * Gets the active overlay.
     *
     * @return the active overlay
     */
    public ActiveOverlay getActiveOverlay() {
        return activeOverlay;
    }


    /**
     * Gets the pause overlay.
     *
     * @return the pause overlay
     */
    public PauseOverlay getPauseOverlay() {
        return pauseOverlay;
    }

    /**
     * Gets the win overlay.
     *
     * @return the win overlay
     */
    public WinOverlay getWinOverlay() {
        return winOverlay;
    }

    /**
     * Gets the game over overlay.
     *
     * @return the game over overlay
     */
    public GameOverOverlay getGameOverOverlay() {
        return gameOverOverlay;
    }
}