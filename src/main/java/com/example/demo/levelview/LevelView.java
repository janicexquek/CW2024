// File: com/example/demo/levelview/LevelView.java

package com.example.demo.levelview;

import com.example.demo.display.DisplayManager;
import com.example.demo.overlay.*;
import com.example.demo.mainmenu.SettingsManager;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;

/**
 * Class representing the view for a game level.
 * Manages the display and overlays for the game level.
 */
public class LevelView {


    private final Group root;
    private final ExitOverlay exitOverlay; // ExitOverlay instance
    private final PauseOverlay pauseOverlay; // PauseOverlay instance
    private final WinOverlay winOverlay; // WinOverlay instance
    private final GameOverOverlay gameOverOverlay; // GameOverOverlay instance
    private final CountdownOverlay countdownOverlay; // CountdownOverlay instance
    private final DisplayManager displayManager; // DisplayManager instance

    private final Runnable resumeGameCallback;
    private final Runnable backToMainMenuCallback;

    private Runnable startGameCallback;
    private Timeline timeline; // Reference to the game loop timeline
    private ActiveOverlay activeOverlay = ActiveOverlay.NONE;

    /**
     * Constructor for LevelView.
     *
     * @param root                   the root group of the scene
     * @param heartsToDisplay        the number of hearts to display for the player's health
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param pauseGameCallback      the callback to pause the game
     * @param resumeGameCallback     the callback to resume the game
     * @param screenWidth            the width of the screen
     * @param screenHeight           the height of the screen
     * @param timeline               the timeline for animations
     */
    public LevelView(Group root, int heartsToDisplay,
                     Runnable backToMainMenuCallback, Runnable pauseGameCallback,
                     Runnable resumeGameCallback, double screenWidth, double screenHeight,
                     Timeline timeline) {
        this.root = root;
        this.timeline = timeline;

        // Assign callbacks to instance variables
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;

        // Initialize DisplayManager with necessary callbacks
        this.displayManager = new DisplayManager(root, heartsToDisplay, screenWidth, screenHeight,
                pauseGameCallback, this::handleShowExitOverlay);

        // Initialize overlays
        this.exitOverlay = new ExitOverlay(screenWidth, screenHeight, resumeGameCallback, backToMainMenuCallback, this::hideExitOverlay);
        this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, pauseGameCallback);
        this.winOverlay = new WinOverlay(screenWidth, screenHeight); // Initialize WinOverlay
        this.gameOverOverlay = new GameOverOverlay(screenWidth, screenHeight); // Initialize GameOverOverlay
        // Initialize the CountdownOverlay
        this.countdownOverlay = new CountdownOverlay(screenWidth, screenHeight, this::onCountdownFinished);

        // Add all overlays to the scene graph
        root.getChildren().addAll(exitOverlay, pauseOverlay, winOverlay, gameOverOverlay, countdownOverlay);
        displayManager.bringInfoDisplayToFront(); // Ensure infoDisplay is on top
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
     * Shows the exit overlay.
     *
     * @param resumeGameCallback     the callback to resume the game
     * @param backToMainMenuCallback the callback to go back to the main menu
     */
    public void showExitOverlay(Runnable resumeGameCallback, Runnable backToMainMenuCallback) {
        if (activeOverlay == ActiveOverlay.NONE) {
            exitOverlay.initializeButtons(resumeGameCallback, backToMainMenuCallback, this::hideExitOverlay);
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
     * Handler method to show the exit overlay.
     * This method matches the Runnable interface.
     */
    private void handleShowExitOverlay() {
        showExitOverlay(resumeGameCallback, backToMainMenuCallback);
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
            winOverlay.showWinOverlay();
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
            gameOverOverlay.showGameOverOverlay();
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
        gameOverOverlay.hideGameOverOverlay();
        activeOverlay = ActiveOverlay.NONE;
    }

    /**
     * Updates the kill count for Level One.
     *
     * @param currentKills the current number of kills
     * @param killsToAdvance the number of kills required to advance
     */
    public void updateKillCount(int currentKills, int killsToAdvance) {
        displayManager.updateKillCount(currentKills, killsToAdvance);
    }

    /**
     * Updates the boss health for Level Two.
     *
     * @param currentHealth the current health of the boss
     */
    public void updateBossHealth(int currentHealth) {
        displayManager.updateBossHealth(currentHealth);
    }

    /**
     * Updates custom information for Level Three and Level Four.
     *
     * @param info the custom information to display
     */
    public void updateCustomInfo(String info) {
        displayManager.updateCustomInfo(info);
    }

    /**
     * Removes a specified number of hearts from the display.
     *
     * @param heartsRemaining the number of hearts remaining
     */
    public void removeHearts(int heartsRemaining) {
        displayManager.removeHearts(heartsRemaining);
    }

    /**
     * Shows the heart display.
     */
    public void showHeartDisplay() {
        displayManager.showHeartDisplay();
    }

    /**
     * Shows the exit display.
     */
    public void showExitDisplay() {
        displayManager.showExitDisplay();
    }

    /**
     * Brings the info display to the front.
     */
    public void bringInfoDisplayToFront() {
        displayManager.bringInfoDisplayToFront();
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
