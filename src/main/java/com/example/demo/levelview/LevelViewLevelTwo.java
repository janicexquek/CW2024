package com.example.demo.levelview;

import com.example.demo.shield.BossShieldImage;
import javafx.animation.Timeline;
import javafx.scene.Group;

/**
 * Class representing the view for Level Two of the game.
 * Extends LevelView and provides specific implementations for Level Two.
 */
public class LevelViewLevelTwo extends LevelView {

    private static final int SHIELD_X_POSITION = 1150;
    private static final int SHIELD_Y_POSITION = 500;
    private final Group root;
    private final BossShieldImage bossShieldImage;

    /**
     * Constructor for LevelViewLevelTwo.
     * @param root the root group of the scene
     * @param heartsToDisplay the number of hearts to display for the player's health
     * @param backToMainMenuCallback the callback to return to the main menu
     * @param pauseGameCallback the callback to pause the game
     * @param resumeGameCallback the callback to resume the game
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public LevelViewLevelTwo(Group root, int heartsToDisplay, Runnable backToMainMenuCallback, Runnable pauseGameCallback, Runnable resumeGameCallback, double screenWidth, double screenHeight) {
        super(root, heartsToDisplay, backToMainMenuCallback, pauseGameCallback, resumeGameCallback, screenWidth, screenHeight);
        this.root = root;
        this.bossShieldImage = new BossShieldImage(SHIELD_X_POSITION, SHIELD_Y_POSITION);
        addImagesToRoot();
    }

    /**
     * Adds images to the root group.
     */
    private void addImagesToRoot() {
        root.getChildren().addAll(bossShieldImage);
    }
}