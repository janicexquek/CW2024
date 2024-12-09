package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import com.example.demo.projectile.EnemyProjectile;

/**
 * Class representing a master plane in the game.
 * Manages the movement and firing of the master plane.
 */
public class MasterPlane extends FighterPlane {

    private static final String IMAGE_NAME = "masterplane.png";
    private static final int IMAGE_HEIGHT = 70;
    private static final int HORIZONTAL_VELOCITY = -6;
    private static final double PROJECTILE_Y_POSITION_OFFSET = 5.0;
    private static final int INITIAL_HEALTH = 5;
    private static final double FIRE_RATE = 0.02;
    private int health = INITIAL_HEALTH;

    /**
     * Constructor for MasterPlane.
     *
     * @param initialXPos the initial X position of the master plane
     * @param initialYPos the initial Y position of the master plane
     */
    public MasterPlane(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos, INITIAL_HEALTH);
    }

    /**
     * Updates the position of the master plane.
     * Moves the plane horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Fires a projectile based on the master plane's fire rate.
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
     * Updates the state of the master plane.
     * Handles position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Takes damage and decreases the health of the master plane.
     * Destroys the plane if health drops to zero.
     */
    @Override
    public void takeDamage() {
        this.health -= 1;
        if (this.health <= 0) {
            setDestroyedBy(DestroyedBy.USER_PROJECTILE); // Explicitly set destruction cause
            super.destroy(); // Call the parent class destroy
        }
    }

    /**
     * Moves the actor horizontally by the given velocity.
     *
     * @param velocity the amount to move the actor horizontally
     */
    protected void moveHorizontally(double velocity) {
        setLayoutX(getLayoutX() + velocity); // Adjust the layoutX position
    }

    /**
     * Returns the health of the master plane.
     *
     * @return the current health of the master plane
     */
    public int getHealth() {
        return this.health;
    }

}