package com.example.demo.projectile;

/**
 * Class representing a user projectile in the game.
 * Manages the movement and behavior of the user projectile.
 */
public class UserProjectile extends Projectile {

    private static final String IMAGE_NAME = "userfire.png";
    private static final int IMAGE_HEIGHT = 15;
    private static final int HORIZONTAL_VELOCITY = 15;

    /**
     * Constructor for UserProjectile.
     *
     * @param initialXPos the initial X position of the user projectile
     * @param initialYPos the initial Y position of the user projectile
     */
    public UserProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    /**
     * Updates the position of the user projectile.
     * Moves the projectile horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Updates the state of the user projectile.
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
        destroy(); // This will mark the projectile as destroyed
    }
}