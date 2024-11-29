// LevelFour.java
package com.example.demo;

import com.example.demo.plane.AllyPlane;
import com.example.demo.plane.EnemyPlane;
import com.example.demo.plane.IntermediatePlane;
import com.example.demo.plane.UserPlane;
import javafx.animation.Timeline;
import javafx.scene.Group;

import java.util.List;
import java.util.stream.Collectors;

public class LevelFour extends LevelParent {

    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background2.jpg";
    private static final String NEXT_LEVEL = ""; // Assuming Level Four is the final level
    private static final int PLAYER_INITIAL_HEALTH = 5;
    private static final double Y_UPPER_BOUND = 80;
    private static final double Y_LOWER_BOUND = 580.0;
    // Win conditions
    private static final int NORMAL_PLANES_TO_DESTROY = 15;
    private static final int INTERMEDIATE_PLANES_TO_DESTROY = 8;

    private int normalPlanesDestroyed = 0;
    private int intermediatePlanesDestroyed = 0;

    // allyPlane
    private boolean abilityActivated = false; // New flag
    private AllyPlane activeAllyPlane = null;  // Reference to active Ally Plane


    public LevelFour(double screenHeight, double screenWidth) {
        super(BACKGROUND_IMAGE_NAME, screenHeight, screenWidth, PLAYER_INITIAL_HEALTH, "LEVEL FOUR");
    }

    @Override
    protected void checkIfGameOver() {
        if (userIsDestroyed()) {
            loseGame();
        } else if (normalPlanesDestroyed >= NORMAL_PLANES_TO_DESTROY && intermediatePlanesDestroyed >= INTERMEDIATE_PLANES_TO_DESTROY) {
            winGame();
        }
    }

    @Override
    protected void initializeFriendlyUnits() {
        Group userGroup = new Group(getUser(), getUser().getShieldImage());
        getRoot().getChildren().add(userGroup);
    }

    // ------------------------ spawn enemy plane & intermediate plane -------------------------
    @Override
    protected void spawnEnemyUnits() {
        int currentNumberOfEnemies = getCurrentNumberOfEnemies();
        final int TOTAL_ENEMIES = 7; // Adjust based on desired difficulty
        final double ENEMY_SPAWN_PROBABILITY = 0.25;

        for (int i = 0; i < TOTAL_ENEMIES - currentNumberOfEnemies; i++) {
            if (Math.random() < ENEMY_SPAWN_PROBABILITY) {
                double yPos = Y_UPPER_BOUND + Math.random() * (Y_LOWER_BOUND- Y_UPPER_BOUND);
                ActiveActorDestructible enemy;

                // Randomly decide to spawn a Normal or Intermediate Plane
                if (Math.random() < 0.7) { // 70% Normal Planes
                    enemy = new EnemyPlane(getScreenWidth(), yPos);
                } else { // 30% Intermediate Planes
                    enemy = new IntermediatePlane(getScreenWidth(), yPos);
                }

                addEnemyUnit(enemy);
            }
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
        return NEXT_LEVEL;
    }

    @Override
    protected String getClassName() {
        return this.getClass().getName();
    }

    @Override
    protected String getLevelDisplayName() {
        return "LEVEL FOUR";
    }

    @Override
    protected void removeAllDestroyedActors() {
        List<ActiveActorDestructible> destroyedEnemies = getEnemyUnits().stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        for (ActiveActorDestructible enemy : destroyedEnemies) {
            if (enemy.getDestroyedBy() == ActiveActorDestructible.DestroyedBy.USER_PROJECTILE) {
                if (enemy instanceof IntermediatePlane) {
                    intermediatePlanesDestroyed++;
                    // Check for ability activation
                    if (intermediatePlanesDestroyed >= 2 && !abilityActivated) {
                        decideAndActivateAbility();
                    }
                } else if (enemy instanceof EnemyPlane) {
                    normalPlanesDestroyed++;
                }
            }
        }
        // Handle destroyed friendly units (e.g., AllyPlane)
        List<ActiveActorDestructible> destroyedFriendlies = friendlyUnits.stream()
                .filter(ActiveActorDestructible::isDestroyed)
                .collect(Collectors.toList());

        for (ActiveActorDestructible friendly : destroyedFriendlies) {
            if (friendly instanceof AllyPlane) {
            }
        }
        super.removeAllDestroyedActors();
    }

    // --------------- decide either activate Ally plane or Shield -------------------
    private void decideAndActivateAbility() {
        abilityActivated = true; // Ensure single activation

        double probability = Math.random(); // 0.0 to 1.0

        if (probability < 0.5) { // 50% chance for Shield
            activateShield();
        } else { // 50% chance for Ally Plane
            spawnAllyPlane();
        }
    }

    // ------------------------ activate shield--------------------------
    private void activateShield() {
        getUser().activateShield(); // Assuming UserPlane has this method
    }

    // ---------------------------- Ally Plane -----------------------------
    // ------------------------ activate ally plane -------------------------
    private void spawnAllyPlane() {
        // Pass 'this::addAllyProjectile' as the projectile addition callback
        activeAllyPlane = new AllyPlane(this::addAllyProjectile, this::deactivateAllyPlane);
        friendlyUnits.add(activeAllyPlane);
        getRoot().getChildren().add(activeAllyPlane);

    }

    // ------------------------ deactivate ally plane -------------------------
    private void deactivateAllyPlane() {
        if (activeAllyPlane != null) {
            activeAllyPlane.destroy(); // Remove Ally Plane from the game
            activeAllyPlane = null;
        }
        levelView.updateCustomInfo("Ally Plane Deactivated.");
    }

    // ------------------ update level view info display ---------------------
    @Override
    protected void updateCustomDisplay() {
        // Format the custom info string for Level Four
        String info = String.format("Normal Planes: %d / %d | Intermediate Planes: %d / %d",
                normalPlanesDestroyed, NORMAL_PLANES_TO_DESTROY,
                intermediatePlanesDestroyed, INTERMEDIATE_PLANES_TO_DESTROY);
        UserPlane userPlane = getUser();
        String shieldInfo = userPlane.isShieldActive() ?
                " | Shield: " + (UserPlane.MAX_SHIELD_DAMAGE - userPlane.getShieldDamageCounter()) + " hits left"
                : "";
        String allyInfo = "";
        if (activeAllyPlane != null) {
            allyInfo = String.format(" | Ally Plane Hits Left: %d", activeAllyPlane.getHealth());
        }
        levelView.updateCustomInfo(info + shieldInfo + allyInfo);
    }
}
