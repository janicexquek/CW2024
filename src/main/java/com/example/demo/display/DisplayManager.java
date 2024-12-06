// File: com/example/demo/display/DisplayManager.java

package com.example.demo.display;

import com.example.demo.styles.FontManager;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class responsible for managing all display components in the game level.
 * This includes HeartDisplay, ExitDisplay, and InfoDisplay.
 */
public class DisplayManager {

    private final Group root;
    private final HeartDisplay heartDisplay;
    private final ExitDisplay exitDisplay;
    private final Text infoDisplay;
    private final FontManager fontManager;

    /**
     * Constructor for DisplayManager.
     *
     * @param root                   the root group of the scene
     * @param heartsToDisplay        the number of hearts to display for the player's health
     * @param screenWidth            the width of the screen
     * @param screenHeight           the height of the screen
     * @param pauseGameCallback      the callback to pause the game
     * @param showExitOverlayCallback the callback to show the exit overlay
     */
    public DisplayManager(Group root, int heartsToDisplay, double screenWidth, double screenHeight,
                          Runnable pauseGameCallback, Runnable showExitOverlayCallback) {
        this.root = root;
        this.fontManager = FontManager.getInstance();

        // Initialize HeartDisplay
        this.heartDisplay = new HeartDisplay(5, 25, heartsToDisplay);

        // Initialize ExitDisplay with callbacks
        this.exitDisplay = new ExitDisplay(1200, 25, pauseGameCallback, showExitOverlayCallback);

        // Initialize InfoDisplay
        this.infoDisplay = new Text();
        setCustomFont();

        // Add all display components to the root
        root.getChildren().addAll(heartDisplay.getContainer(), exitDisplay.getContainer(), infoDisplay);
        positionInfoDisplay();
    }

    /**
     * Sets a custom font for the info display using FontManager.
     */
    private void setCustomFont() {
        // Retrieve the desired font from FontManager
        Font customFont = fontManager.getFont("Pixel Digivolve", 20);
        this.infoDisplay.setFont(customFont);
    }

    /**
     * Positions the info display on the screen based on HeartDisplay's position.
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
     * Updates the kill count display.
     *
     * @param currentKills   the current number of kills
     * @param killsToAdvance the number of kills required to advance
     */
    public void updateKillCount(int currentKills, int killsToAdvance) {
        Platform.runLater(() -> infoDisplay.setText("Kills: " + currentKills + " / " + killsToAdvance));
    }

    /**
     * Updates the boss health display.
     *
     * @param currentHealth the current health of the boss
     */
    public void updateBossHealth(int currentHealth) {
        Platform.runLater(() -> infoDisplay.setText("Boss Health: " + currentHealth));
    }

    /**
     * Updates custom information display for other levels.
     *
     * @param info the custom information to display
     */
    public void updateCustomInfo(String info) {
        Platform.runLater(() -> infoDisplay.setText(info));
    }

    /**
     * Removes a specified number of hearts from the display.
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
     * Ensures the HeartDisplay is visible on the screen.
     */
    public void showHeartDisplay() {
        if (!root.getChildren().contains(heartDisplay.getContainer())) {
            root.getChildren().add(heartDisplay.getContainer());
        }
    }

    /**
     * Ensures the ExitDisplay is visible on the screen.
     */
    public void showExitDisplay() {
        if (!root.getChildren().contains(exitDisplay.getContainer())) {
            root.getChildren().add(exitDisplay.getContainer());
        }
    }

    /**
     * Brings the InfoDisplay to the front of the scene graph.
     */
    public void bringInfoDisplayToFront() {
        infoDisplay.toFront();
    }
}
