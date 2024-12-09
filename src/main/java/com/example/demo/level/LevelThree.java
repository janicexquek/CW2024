package com.example.demo.level;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.levelview.LevelView;
import com.example.demo.overlay.OverlayManager;
import com.example.demo.plane.EnemyPlane;
import com.example.demo.plane.IntermediatePlane;
import com.example.demo.plane.MasterPlane;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing the third level of the game.
 * Extends LevelParent and provides specific implementations for Level Three.
 */
public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg"; // Updated to LevelThree background
    private static final String NEXT_LEVEL = "com.example.demo.level.LevelFour";
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double Y_UPPER_BOUND = 80;
    private static final double Y_LOWER_BOUND = 600.0;

    // Wave Management
    protected int normalPlanesDestroyed = 0;
    private static final int NORMAL_TO_INTERMEDIATE_DESTROYED = 10; // Destroy 10 normal planes to spawn intermediates

    protected boolean intermediateWaveActive = false;
    protected int intermediatePlanesDestroyed = 0;
    private static final int INTERMEDIATE_TO_MASTER_DESTROYED = 5; // Destroy 5 intermediate planes to spawn masters

    protected boolean masterWaveActive = false;
    private int masterPlanesDestroyed = 0;
    private static final int MASTER_TO_WIN_DESTROYED = 2; // Destroy 5 intermediate planes to spawn masters

    /**
     * Constructor for LevelThree.
     *
     * @param screenHeight the height of the screen
     * @param screenWidth the width of the screen
     */
    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL THREE");
    }

    /**
     * Checks if the game is over by evaluating the destruction status of the user and enemy planes.
     */
    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (masterWaveActive && masterPlanesDestroyed >= 2) {
            winGame();
        }
    }

    /**
     * Initializes friendly units by adding the user to the root group.
     */
    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    /**
     * Gets the current number of intermediate planes.
     *
     * @return the number of intermediate planes
     */
    private int getCurrentNumberOfIntermediatePlanes() {
        return (int) getEnemyUnits().stream()
                .filter(enemy -> enemy instanceof IntermediatePlane)
                .count();
    }

    /**
     * Gets the current number of master planes.
     *
     * @return the number of master planes
     */
    protected int getCurrentNumberOfMasterPlanes() {
        return (int) getEnemyUnits().stream()
                .filter(enemy -> enemy instanceof MasterPlane)
                .count();
    }

    /**
     * Spawns enemy units based on the current wave and spawn probability.
     */
    @Override
    protected void spawnEnemyUnits() {
        // Activate intermediate wave if conditions are met
        if (!intermediateWaveActive && normalPlanesDestroyed >= NORMAL_TO_INTERMEDIATE_DESTROYED) {
            intermediateWaveActive = true;
        }

        // Activate master wave if conditions are met
        if (intermediateWaveActive && !masterWaveActive && intermediatePlanesDestroyed >= INTERMEDIATE_TO_MASTER_DESTROYED) {
            masterWaveActive = true;
        }

        // Spawn planes based on the active wave
        if (masterWaveActive) {
            spawnMasterPlanes();
        } else if (intermediateWaveActive) {
            spawnIntermediatePlanes();
        } else {
            spawnNormalPlanes();
        }
    }

    /**
     * Instantiates the level view for Level Three.
     *
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @return the instantiated LevelView
     */
    @Override
    protected LevelView instantiateLevelView(double screenWidth, double screenHeight) {
        return new LevelView(
                getRoot(),
                PLAYER_INITIAL_HEALTH,
                getBackToMainMenuCallback(),
                this::pauseGame,
                this::resumeGame,
                screenWidth,
                screenHeight
        );
    }

    /**
     * Gets the class name of the next level.
     *
     * @return the class name of the next level
     */
    @Override
    protected String getNextLevelClassName() {
        return NEXT_LEVEL; // Could be main menu or a final screen
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
        return "LEVEL THREE";
    }

    /**
     * Spawns normal planes based on the spawn probability.
     * Ensures the total number of normal planes does not exceed the defined limit.
     */
    protected void spawnNormalPlanes() {
        int TOTAL_NORMAL_PLANES = 5;
        for (int i = 0; i < TOTAL_NORMAL_PLANES - getCurrentNumberOfEnemies(); i++) {
            double yPos = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND - Y_UPPER_BOUND);
            ActiveActorDestructible normalPlane = new EnemyPlane(getScreenWidth(), yPos);
            addEnemyUnit(normalPlane);
        }
    }

    /**
     * Spawns intermediate planes based on the spawn probability.
     * Ensures the total number of intermediate planes does not exceed the defined limit.
     */
    private void spawnIntermediatePlanes() {
        int TOTAL_INTERMEDIATE_PLANES = 3;
        int currentIntermediatePlanes = getCurrentNumberOfIntermediatePlanes();
        for (int i = 0; i < TOTAL_INTERMEDIATE_PLANES - currentIntermediatePlanes; i++) {
            double yPos = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND - Y_UPPER_BOUND);
            ActiveActorDestructible intermediatePlane = new IntermediatePlane(getScreenWidth(), yPos);
            addEnemyUnit(intermediatePlane);
        }
    }

    /**
     * Spawns master planes based on the spawn probability.
     * Ensures the total number of master planes does not exceed the defined limit.
     */
    private void spawnMasterPlanes() {
        int TOTAL_MASTER_PLANES = 2;
        int currentMasterPlanes = getCurrentNumberOfMasterPlanes();
        for (int i = 0; i < TOTAL_MASTER_PLANES - currentMasterPlanes; i++) {
            double yPos = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND - Y_UPPER_BOUND);
            ActiveActorDestructible masterPlane = new MasterPlane(getScreenWidth(), yPos);
            addEnemyUnit(masterPlane);
        }
    }

    /**
     * Removes all destroyed actors from the game and updates the destruction counts.
     */
    @Override
    protected void removeAllDestroyedActors() {
        // Step 1: Track destroyed planes before they are removed
        List<ActiveActorDestructible> destroyedEnemies = getEnemyUnits().stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        for (ActiveActorDestructible enemy : destroyedEnemies) {
            // Only increment counts if destroyed by user projectile
            if (enemy.getDestroyedBy() == ActiveActorDestructible.DestroyedBy.USER_PROJECTILE) {
                if (enemy instanceof MasterPlane) {
                    masterPlanesDestroyed++;
                } else if (enemy instanceof IntermediatePlane) {
                    intermediatePlanesDestroyed++;
                } else if (enemy instanceof EnemyPlane) {
                    normalPlanesDestroyed++;
                }
            }
        }

        // Step 2: Now remove the destroyed actors
        super.removeAllDestroyedActors();
    }

    /**
     * Updates the custom display information for Level Three.
     */
    @Override
    protected void updateCustomDisplay() {
        // Display current wave and objectives
        String status;
        if (!intermediateWaveActive) {
            int remaining = NORMAL_TO_INTERMEDIATE_DESTROYED - normalPlanesDestroyed;
            status = "Wave 1: Destroy " + Math.max(remaining, 0) + " Normal Planes";
        } else if (!masterWaveActive) {
            int remaining = INTERMEDIATE_TO_MASTER_DESTROYED - intermediatePlanesDestroyed;
            status = "Wave 2: Destroy " + Math.max(remaining, 0) + " Intermediate Planes";
        } else {
            int remaining = MASTER_TO_WIN_DESTROYED - masterPlanesDestroyed;
            status = "Wave 3: Destroy " + Math.max(remaining, 0) + " Master Planes";
        }
        levelView.updateCustomInfo(status);
    }

    /**
     * Exposes the active overlay by delegating to LevelView.
     *
     * @return the active overlay
     */
    public OverlayManager.ActiveOverlay getActiveOverlay() {
        return levelView.getActiveOverlay();
    }

    /**
     * Getter for the number of destroyed master planes.
     *
     * @return The number of master planes destroyed.
     */
    public int getMasterPlanesDestroyed() {
        return masterPlanesDestroyed;
    }
}