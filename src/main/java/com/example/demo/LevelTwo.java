package com.example.demo;

import com.example.demo.plane.BossPlane;
import javafx.animation.Timeline;

/**
 * Class representing the second level of the game.
 * Extends LevelParent and provides specific implementations for Level Two.
 */
public class LevelTwo extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.LevelThree";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private final BossPlane bossPlane;
    private LevelViewLevelTwo levelView;

    /**
     * Constructor for LevelTwo.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth the width of the screen
     */
    public LevelTwo(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL TWO");
        bossPlane = new BossPlane();
    }

    /**
     * Checks if the game is over by evaluating the destruction status of the user and the boss plane.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (bossPlane.isDestroyed()) {
            winGame();
        }
    }

    /**
     * Updates the custom display information for Level Two.
     */
    @Override
    protected void updateCustomDisplay() {
        levelView.updateBossHealth(bossPlane.getBossHealth());
    }

    /**
     * Initializes friendly units by adding the user to the root group.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units by adding the boss plane to the game if no enemies are present.
     */
    @Override
    protected void spawnEnemyUnits() {
        if (getCurrentNumberOfEnemies() == 0) {
            addEnemyUnit(bossPlane);
            if (!getRoot().getChildren().contains(bossPlane.getShieldImage())) {
                getRoot().getChildren().add(bossPlane.getShieldImage()); // Add the shield image to the scene graph
            }
        }
    }

    /**
     * Instantiates the level view for Level Two.
     *
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param timeline the timeline for animations
     * @return the instantiated LevelView
     */
    @Override
    protected LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline) {
        levelView = new LevelViewLevelTwo(
                getRoot(),
                PLAYER_INITIAL_HEALTH,
                getBackToMainMenuCallback(),
                this::pauseGame, // Pass the pauseGame callback
                this::resumeGame,  // Pass the resumeGame callback
                screenWidth, // Screen width
                screenHeight, // Screen height
                timeline // Pass the game loop timeline
        );
        return levelView;
    }

    /**
     * Gets the class name of the next level.
     *
     * @return the class name of the next level
     */
    @Override
    protected String getNextLevelClassName() {
        return NEXT_LEVEL; // Indicates that the next screen is the Main Menu
    }

    /**
     * Gets the class name of the current level.
     *
     * @return the class name of the current level
     */
    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    /**
     * Gets the display name of the current level.
     *
     * @return the display name of the current level
     */
    @Override
    protected String getLevelDisplayName() {
        return "LEVEL TWO";
    }
}