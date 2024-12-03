package com.example.demo.projectile;

/**
 * Class representing an ally projectile in the game.
 * Manages the movement and behavior of the ally projectile.
 */
public class AllyProjectile extends Projectile {

    private static final String IMAGE_NAME = "allyfire.png"; // Changed to ally's fire image
    private static final int IMAGE_HEIGHT = 10;
    private static final int HORIZONTAL_VELOCITY = 15;
    private static final int INITIAL_X_POSITION = 180;

    /**
     * Constructor for AllyProjectile.
     *
     * @param initialYPos the initial Y position of the ally projectile
     */
    public AllyProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    /**
     * Updates the position of the ally projectile.
     * Moves the projectile horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Updates the state of the ally projectile.
     * Handles position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Takes damage and marks the projectile as destroyed.
     */
    @Override
    public void takeDamage() {
        destroy(); // Mark projectile as destroyed
    }
}