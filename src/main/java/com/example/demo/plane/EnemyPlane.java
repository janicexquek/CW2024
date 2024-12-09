package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;

/**
 * Class representing an enemy plane in the game.
 * Manages the movement and firing of the enemy plane.
 */
public class EnemyPlane extends FighterPlane {

    private static final String IMAGE_NAME = "enemyplane.png";
    private static final int IMAGE_HEIGHT = 50;
    private static final int HORIZONTAL_VELOCITY = -6;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 5.0;
    private static final int INITIAL_HEALTH = 1;
    private static final double FIRE_RATE = .01;

    /**
     * Constructor for EnemyPlane.
     *
     * @param initialXPos the initial X position of the enemy plane
     * @param initialYPos the initial Y position of the enemy plane
     */
    public EnemyPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    /**
     * Updates the position of the enemy plane.
     * Moves the plane horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Fires a projectile based on the enemy plane's fire rate.
     *
     * @return the fired projectile, or null if no projectile is fired
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        // Determine if the enemy fires a projectile based on FIRE_RATE
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition();
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    /**
     * Updates the state of the enemy plane.
     * Handles position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }
}