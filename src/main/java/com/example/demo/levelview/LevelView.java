// LevelView.java
package com.example.demo.levelview;

import com.example.demo.display.DisplayManager;
import com.example.demo.overlay.OverlayManager;
import javafx.animation.Timeline;
import javafx.scene.Group;

public class LevelView {

    private final DisplayManager displayManager;
    private final OverlayManager overlayManager;

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
    public LevelView(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback,
                     Runnable resumeGameCallback, double screenWidth, double screenHeight, Timeline timeline) {

        this.displayManager = new DisplayManager(root, heartsToDisplay, screenWidth, screenHeight, pauseGameCallback, this::showExitOverlay);
        this.overlayManager = new OverlayManager(root, screenWidth, screenHeight, timeline, resumeGameCallback, backToMainMenuCallback);
    }

    // Delegated Methods to DisplayManager
    public void showHeartDisplay() {
        displayManager.showHeartDisplay();
    }

    public void showExitDisplay() {
        displayManager.showExitDisplay();
    }

    /**
     * Shows the exit overlay by delegating to OverlayManager.
     */
    public void showExitOverlay() {
        overlayManager.showExitOverlay();
    }

    public void updateKillCount(int currentKills, int killsToAdvance) {
        displayManager.updateKillCount(currentKills, killsToAdvance);
    }

    public void updateBossHealth(int currentHealth) {
        displayManager.updateBossHealth(currentHealth);
    }

    public void updateCustomInfo(String info) {
        displayManager.updateCustomInfo(info);
    }

    public void removeHearts(int heartsRemaining) {
        displayManager.removeHearts(heartsRemaining);
    }

    public void bringInfoDisplayToFront() {
        displayManager.bringInfoDisplayToFront();
    }

    // Delegated Methods to OverlayManager
    public void startCountdown(Runnable startGameCallback) {
        overlayManager.startCountdown(startGameCallback);
    }

    public void showPauseOverlay() {
        overlayManager.showPauseOverlay();
    }

    public void hidePauseOverlay() {
        overlayManager.hidePauseOverlay();
    }

    public void showWinOverlay(Runnable backToMainMenuCallback, Runnable nextLevelCallback, Runnable restartCallback,
                               String levelName, long currentTimeSeconds, long fastestTimeSeconds, String achievementMessage) {
        overlayManager.showWinOverlay(backToMainMenuCallback, nextLevelCallback, restartCallback, levelName, currentTimeSeconds, fastestTimeSeconds, achievementMessage);
    }

    public void hideWinOverlay() {
        overlayManager.hideWinOverlay();
    }

    public void showGameOverOverlay(Runnable backToMainMenuCallback, Runnable restartCallback,
                                    String levelName, long currentTimeSeconds, String fastestTimeDisplay) {
        overlayManager.showGameOverOverlay(backToMainMenuCallback, restartCallback, levelName, currentTimeSeconds, fastestTimeDisplay);
    }

    public void hideGameOverOverlay() {
        overlayManager.hideGameOverOverlay();
    }

    public OverlayManager.ActiveOverlay getActiveOverlay() {
        return overlayManager.getActiveOverlay();
    }
}
