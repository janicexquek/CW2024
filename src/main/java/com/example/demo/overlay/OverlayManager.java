package com.example.demo.overlay;

import com.example.demo.mainmenumanager.SettingsManager;
import com.example.demo.styles.TimeFormatter;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;

/**
 * Manages the different overlays in the game, such as pause, win, game over, countdown, and exit overlays.
 */
public class OverlayManager {

    private final Group root;
    private final ExitOverlay exitOverlay;
    private final PauseOverlay pauseOverlay;
    private final WinOverlay winOverlay;
    private final GameOverOverlay gameOverOverlay;
    private final CountdownOverlay countdownOverlay;
    private final Timeline timeline;

    private final Runnable resumeGameCallback;
    private final Runnable backToMainMenuCallback;

    private ActiveOverlay activeOverlay = ActiveOverlay.NONE;
    private Runnable startGameCallback;

    /**
     * Constructs an OverlayManager with the specified parameters.
     *
     * @param root the root group to which overlays are added
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param timeline the game loop timeline
     * @param resumeGameCallback the callback to resume the game
     * @param backToMainMenuCallback the callback to return to the main menu
     */
    public OverlayManager(Group root, double screenWidth, double screenHeight, Timeline timeline,
                          Runnable resumeGameCallback, Runnable backToMainMenuCallback) {
        this.root = root;
        this.timeline = timeline;

        // Assign callbacks to instance variables
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;

        this.exitOverlay = new ExitOverlay(screenWidth, screenHeight, resumeGameCallback, backToMainMenuCallback, this::hideExitOverlay);
        this.pauseOverlay = new PauseOverlay(screenWidth, screenHeight, resumeGameCallback);
        this.winOverlay = new WinOverlay(screenWidth, screenHeight);
        this.gameOverOverlay = new GameOverOverlay(screenWidth, screenHeight);
        this.countdownOverlay = new CountdownOverlay(screenWidth, screenHeight, this::onCountdownFinished);

        root.getChildren().addAll(exitOverlay, pauseOverlay, winOverlay, gameOverOverlay, countdownOverlay);
    }

    /**
     * Enum representing the active overlay.
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
     * Returns the currently active overlay.
     *
     * @return the active overlay
     */
    public ActiveOverlay getActiveOverlay() {
        return activeOverlay;
    }

    /**
     * Starts the countdown overlay and pauses the game.
     *
     * @param startGameCallback the callback to start the game after the countdown
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
     * Callback method when the countdown finishes.
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
     * Shows the exit overlay.
     */
    public void showExitOverlay() {
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
     * Shows the win overlay.
     *
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param nextLevelCallback the callback to proceed to the next level
     * @param restartCallback the callback to restart the current level
     * @param levelName the name of the current level
     * @param currentTimeSeconds the current time in seconds
     * @param fastestTimeSeconds the fastest time in seconds
     * @param achievementMessage the achievement message to display
     */
    public void showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback, Runnable restartCallback,
                               String levelName, long currentTimeSeconds, long fastestTimeSeconds, String achievementMessage) {
        if (activeOverlay == ActiveOverlay.NONE) {
            winOverlay.initializeButtons(backToMainMenuCallback, nextLevelCallback, restartCallback, levelName);
            // Format times
            String currentTime = TimeFormatter.formatTime(currentTimeSeconds);
            String fastestTime = TimeFormatter.formatTime(fastestTimeSeconds);
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
        if (activeOverlay == ActiveOverlay.WIN) {
            return;
        }
        winOverlay.hideWinOverlay();
        activeOverlay = ActiveOverlay.NONE;
    }

    /**
     * Shows the game over overlay.
     *
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param restartCallback the callback to restart the current level
     * @param levelName the name of the current level
     * @param currentTimeSeconds the current time in seconds
     * @param fastestTimeDisplay the display string for the fastest time
     */
    public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback,
                                    String levelName, long currentTimeSeconds, String fastestTimeDisplay) {
        if (activeOverlay == ActiveOverlay.NONE) {
            gameOverOverlay.initializeButtons(backToMainMenuCallback, restartCallback, levelName);
            // Format times
            String currentTime = TimeFormatter.formatTime(currentTimeSeconds);
            gameOverOverlay.setTimes(currentTime, fastestTimeDisplay);
            gameOverOverlay.showGameOverOverlay();
            activeOverlay = ActiveOverlay.GAME_OVER;
        }
    }

    /**
     * Hides the game over overlay.
     */
    public void hideGameOverOverlay() {
        if (activeOverlay == ActiveOverlay.GAME_OVER) {
            return;
        }
        gameOverOverlay.hideGameOverOverlay();
        activeOverlay = ActiveOverlay.NONE;
    }
}