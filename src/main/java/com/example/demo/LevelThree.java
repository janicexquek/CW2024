package com.example.demo;

import com.example.demo.plane.EnemyPlane;
import com.example.demo.plane.IntermediatePlane;
import com.example.demo.plane.MasterPlane;
import javafx.animation.Timeline;

import java.util.List;
import java.util.stream.Collectors;

public class LevelThree extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg"; // Updated to LevelThree background
    private static final String NEXT_LEVEL = "com.example.demo.LevelFour"; // No next level or set to main menu
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private int masterPlanesDestroyed = 0;
    private static final int MASTER_TO_WIN_DESTROYED = 2; // Destroy 5 intermediate planes to spawn masters

    // Wave Management
    private int normalPlanesDestroyed = 0;
    private static final int NORMAL_TO_INTERMEDIATE_DESTROYED = 10; // Destroy 10 normal planes to spawn intermediates
    private boolean intermediateWaveActive = false;

    private int intermediatePlanesDestroyed = 0;
    private static final int INTERMEDIATE_TO_MASTER_DESTROYED = 5; // Destroy 5 intermediate planes to spawn masters
    private boolean masterWaveActive = false;

    public LevelThree(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL THREE");
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (masterWaveActive && masterPlanesDestroyed >= 2) { // Assuming you want to destroy all Master Planes
            winGame();
        }
    }

    @Override
    protected void initializeFriendlyUnits() {
        getRoot().getChildren().add(getUser());
    }

    private int getCurrentNumberOfIntermediatePlanes() {
        return (int) getEnemyUnits().stream()
                .filter(enemy -> enemy instanceof IntermediatePlane)
                .count();
    }

    private int getCurrentNumberOfMasterPlanes() {
        return (int) getEnemyUnits().stream()
                .filter(enemy -> enemy instanceof MasterPlane)
                .count();
    }

    @Override
    protected void spawnEnemyUnits() {
        // Activate intermediate wave if conditions are met
        if (!intermediateWaveActive && normalPlanesDestroyed >= NORMAL_TO_INTERMEDIATE_DESTROYED) {
            intermediateWaveActive = true;
            System.out.println("Intermediate Wave Activated!");
        }

        // Activate master wave if conditions are met
        if (intermediateWaveActive && !masterWaveActive && intermediatePlanesDestroyed >= INTERMEDIATE_TO_MASTER_DESTROYED) {
            masterWaveActive = true;
            System.out.println("Master Wave Activated!");
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

    @Override
    protected LevelView instantiateLevelView(double screenWidth, double screenHeight, Timeline timeline) {
        return new LevelView(
                getRoot(),
                PLAYER_INITIAL_HEALTH,
                getBackToMainMenuCallback(),
                this::pauseGame,
                this::resumeGame,
                screenWidth,
                screenHeight,
                timeline
        );
    }

    @Override
    protected String getNextLevelClassName() {
        return NEXT_LEVEL; // Could be main menu or a final screen
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected String getLevelDisplayName() {
        return "LEVEL THREE";
    }

    private boolean userHasDestroyedAllMasters() {
        // Check if there are no MasterPlanes left
        return getEnemyUnits().stream().noneMatch(enemy -> enemy instanceof MasterPlane);
    }

    // Spawn methods for different waves
    private void spawnNormalPlanes() {
        int TOTAL_NORMAL_PLANES = 5;
        double ENEMY_SPAWN_PROBABILITY = 0.25;
        for (int i = 0; i < TOTAL_NORMAL_PLANES - getCurrentNumberOfEnemies(); i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double yPos = LevelParent.Y_UPPER_BOUND + Math.random() * (LevelParent.Y_LOWER_BOUND - LevelParent.Y_UPPER_BOUND);
                ActiveActorDestructible normalPlane = new EnemyPlane(getScreenWidth(), yPos);
                addEnemyUnit(normalPlane);
            }
        }
    }

    private void spawnIntermediatePlanes() {
        int TOTAL_INTERMEDIATE_PLANES = 3;
        double ENEMY_SPAWN_PROBABILITY = 0.15;
        int currentIntermediatePlanes = getCurrentNumberOfIntermediatePlanes();

        for (int i = 0; i < TOTAL_INTERMEDIATE_PLANES - currentIntermediatePlanes; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double yPos = LevelParent.Y_UPPER_BOUND + Math.random() * (LevelParent.Y_LOWER_BOUND - LevelParent.Y_UPPER_BOUND);
                ActiveActorDestructible intermediatePlane = new IntermediatePlane(getScreenWidth(), yPos);
                addEnemyUnit(intermediatePlane);
//                System.out.println("Spawned IntermediatePlane. Current Intermediate Planes: " + (currentIntermediatePlanes + 1));
            }
        }
    }

    private void spawnMasterPlanes() {
        int TOTAL_MASTER_PLANES = 2;
        double ENEMY_SPAWN_PROBABILITY = 0.10;
        int currentMasterPlanes = getCurrentNumberOfMasterPlanes();

        for (int i = 0; i < TOTAL_MASTER_PLANES - currentMasterPlanes; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double yPos = LevelParent.Y_UPPER_BOUND + Math.random() * (LevelParent.Y_LOWER_BOUND - LevelParent.Y_UPPER_BOUND);
                ActiveActorDestructible masterPlane = new MasterPlane(getScreenWidth(), yPos);
                addEnemyUnit(masterPlane);
//                System.out.println("Spawned MasterPlane. Current Master Planes: " + (currentMasterPlanes + 1));
            }
        }
    }

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
}
