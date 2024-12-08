package com.example.demo.level;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.levelview.LevelView;
import com.example.demo.plane.EnemyPlane;
import com.example.demo.overlay.OverlayManager;

/**
 * Class representing the first level of the game.
 * Extends LevelParent and provides specific implementations for Level One.
 */
public class LevelOne extends LevelParent {

    public static final int KILLS_TO_ADVANCE = 10; // 10
    public static final int PLAYER_INITIAL_HEALTH = 5;

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";
    private static final String NEXT_LEVEL = "com.example.demo.level.LevelTwo";
    protected static final int TOTAL_ENEMIES = 5;
    private static final double Y_UPPER_BOUND = 80;
    private static final double Y_LOWER_BOUND = 650.0;

    /**
     * Constructor for LevelOne.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth  the width of the screen
     */
    public LevelOne(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL ONE");
    }

    /**
     * Checks if the game is over by evaluating the destruction status of the user and enemy planes.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (userHasReachedKillTarget()) {
            winGame(); // Triggers the WinOverlay
        }
    }

    /**
     * Updates the custom display information for Level One.
     */
    @Override
    protected void updateCustomDisplay() {
        levelView.updateKillCount(getUser().getNumberOfKills(), KILLS_TO_ADVANCE);
    }

    /**
     * Initializes friendly units by adding the user to the root group.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Spawns enemy units based on the current number of enemies and a spawn probability.
     */
    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            double newEnemyInitialYPosition = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND - Y_UPPER_BOUND);
            ActiveActorDestructible newEnemy = new EnemyPlane(getScreenWidth(), newEnemyInitialYPosition);
            addEnemyUnit(newEnemy);
        }
    }

    /**
     * Instantiates the level view for Level One.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     * @return the instantiated LevelView
     */
    @Override
    protected LevelView instantiateLevelView(double screenWidth, double screenHeight) {
        // Pass the pause and resume callbacks to LevelView
        return new LevelView(
                getRoot(),
                PLAYER_INITIAL_HEALTH,
                getBackToMainMenuCallback(),
                this::pauseGame, // Pause callback
                this::resumeGame, // Resume callback
                screenWidth, // Screen width
                screenHeight // Screen height
        );
    }

    /**
     * Gets the class name of the next level.
     *
     * @return the class name of the next level
     */
    @Override
    protected String getNextLevelClassName() {
        return NEXT_LEVEL;
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
        return "LEVEL ONE";
    }

    /**
     * Checks if the user has reached the kill target to advance to the next level.
     *
     * @return true if the user has reached the kill target, false otherwise
     */
    private boolean userHasReachedKillTarget() {
        return getUser().getNumberOfKills() >= KILLS_TO_ADVANCE;
    }

    /**
     * Exposes the active overlay by delegating to LevelView.
     *
     * @return the active overlay
     */
    public OverlayManager.ActiveOverlay getActiveOverlay() {
        return levelView.getActiveOverlay();
    }
}
