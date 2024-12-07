// LevelView.java
package com.example.demo.levelview;

import com.example.demo.display.DisplayManager;
import com.example.demo.overlay.OverlayManager;
import javafx.scene.Group;

/**
 * Manages the UI elements and overlays for a game level.
 * Delegates specific UI tasks to DisplayManager and OverlayManager.
 */
public class LevelView {

    private final DisplayManager displayManager;
    private final OverlayManager overlayManager;

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
     */
    public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback,
                     Runnable resumeGameCallback, double screenWidth, double screenHeight) {

        this.displayManager = new DisplayManager(root, heartsToDisplay, screenWidth, screenHeight, pauseGameCallback, this::showExitOverlay);
        this.overlayManager = new OverlayManager(root, screenWidth, screenHeight, pauseGameCallback, resumeGameCallback, backToMainMenuCallback);
    }

    // Delegated Methods to DisplayManager

    /**
     * Shows the heart display by delegating to DisplayManager.
     */
    public void showHeartDisplay() {
        displayManager.showHeartDisplay();
    }

    /**
     * Shows the exit display by delegating to DisplayManager.
     */
    public void showExitDisplay() {
        displayManager.showExitDisplay();
    }

    /**
     * Shows the exit overlay by delegating to OverlayManager.
     */
    public void showExitOverlay() {
        overlayManager.showExitOverlay();
    }

    /**
     * Updates the kill count by delegating to DisplayManager.
     *
     * @param currentKills the current number of kills
     * @param killsToAdvance the number of kills required to advance
     */
    public void updateKillCount(int currentKills, int killsToAdvance) {
        displayManager.updateKillCount(currentKills, killsToAdvance);
    }

    /**
     * Updates the boss health by delegating to DisplayManager.
     *
     * @param currentHealth the current health of the boss
     */
    public void updateBossHealth(int currentHealth) {
        displayManager.updateBossHealth(currentHealth);
    }

    /**
     * Updates custom information by delegating to DisplayManager.
     *
     * @param info the custom information to display
     */
    public void updateCustomInfo(String info) {
        displayManager.updateCustomInfo(info);
    }

    /**
     * Removes hearts from the display by delegating to DisplayManager.
     *
     * @param heartsRemaining the number of hearts remaining
     */
    public void removeHearts(int heartsRemaining) {
        displayManager.removeHearts(heartsRemaining);
    }

    /**
     * Brings the information display to the front by delegating to DisplayManager.
     */
    public void bringInfoDisplayToFront() {
        displayManager.bringInfoDisplayToFront();
    }

    // Delegated Methods to OverlayManager

    /**
     * Starts the countdown by delegating to OverlayManager.
     *
     * @param startGameCallback the callback to start the game
     */
    public void startCountdown(Runnable startGameCallback) {
        overlayManager.startCountdown(startGameCallback);
    }

    /**
     * Shows the pause overlay by delegating to OverlayManager.
     */
    public void showPauseOverlay() {
        overlayManager.showPauseOverlay();
    }

    /**
     * Hides the pause overlay by delegating to OverlayManager.
     */
    public void hidePauseOverlay() {
        overlayManager.hidePauseOverlay();
    }

    /**
     * Shows the win overlay by delegating to OverlayManager.
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
        overlayManager.showWinOverlay(backToMainMenuCallback, nextLevelCallback, restartCallback, levelName, currentTimeSeconds, fastestTimeSeconds, achievementMessage);
    }

    /**
     * Hides the win overlay by delegating to OverlayManager.
     */
    public void hideWinOverlay() {
        overlayManager.hideWinOverlay();
    }

    /**
     * Shows the game over overlay by delegating to OverlayManager.
     *
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param restartCallback the callback to restart the level
     * @param levelName the name of the level
     * @param currentTimeSeconds the current time in seconds
     * @param fastestTimeDisplay the fastest time display
     */
    public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback,
                                    String levelName, long currentTimeSeconds, String fastestTimeDisplay) {
        overlayManager.showGameOverOverlay(backToMainMenuCallback, restartCallback, levelName, currentTimeSeconds, fastestTimeDisplay);
    }

    /**
     * Hides the game over overlay by delegating to OverlayManager.
     */
    public void hideGameOverOverlay() {
        overlayManager.hideGameOverOverlay();
    }

    /**
     * Gets the active overlay by delegating to OverlayManager.
     *
     * @return the active overlay
     */
    public OverlayManager.ActiveOverlay getActiveOverlay() {
        return overlayManager.getActiveOverlay();
    }
}
