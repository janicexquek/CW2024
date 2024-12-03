package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;

/**
 * Class representing an intermediate plane in the game.
 * Manages the movement and firing of the intermediate plane.
 */
public class IntermediatePlane extends FighterPlane {

    private static final String IMAGE_NAME = "intermediateplane.png";
    private static final int IMAGE_HEIGHT = 150;
    private static final int HORIZONTAL_VELOCITY = -5;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 5.0;
    private static final int INITIAL_HEALTH = 3;
    private static final double FIRE_RATE = 0.01;
    private int health = INITIAL_HEALTH;

    /**
     * Constructor for IntermediatePlane.
     *
     * @param initialXPos the initial X position of the intermediate plane
     * @param initialYPos the initial Y position of the intermediate plane
     */
    public IntermediatePlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    /**
     * Updates the position of the intermediate plane.
     * Moves the plane horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Fires a projectile based on the intermediate plane's fire rate.
     *
     * @return the fired projectile, or null if no projectile is fired
     */
    @Override
    public ActiveActorDestructible fireProjectile() {
        if (Math.random() < FIRE_RATE) {
            double projectileXPosition = getProjectileXPosition();
            double projectileYPosition = getProjectileYPosition(PROJECTILE_Y_POSITION_OFFSET);
            return new EnemyProjectile(projectileXPosition, projectileYPosition);
        }
        return null;
    }

    /**
     * Updates the state of the intermediate plane.
     * Handles position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Takes damage and decreases the health of the intermediate plane.
     * Destroys the plane if health drops to zero.
     */
    @Override
    public void takeDamage() {
        this.health -= 1;
        if (this.health <= 0) {
            this.destroy();
        }
    }
}