package com.example.demo.projectile;

/**
 * Class representing a boss projectile in the game.
 * Manages the movement and behavior of the boss projectile.
 */
public class BossProjectile extends Projectile {

    private static final String IMAGE_NAME = "fireball.png";
    private static final int IMAGE_HEIGHT = 35;
    private static final int HORIZONTAL_VELOCITY = -15;
    private static final int INITIAL_X_POSITION = 950;

    /**
     * Constructor for BossProjectile.
     *
     * @param initialYPos the initial Y position of the boss projectile
     */
    public BossProjectile(double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, INITIAL_X_POSITION, initialYPos);
    }

    /**
     * Updates the position of the boss projectile.
     * Moves the projectile horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Updates the state of the boss projectile.
     * Handles position update.
     */
    @Override
    public void updateActor() {
        updatePosition();
    }

    /**
     * Moves the actor horizontally by the specified velocity.
     * @param velocity the amount to move the actor horizontally
     */
    protected void moveHorizontally(double velocity) {
        setLayoutX(getLayoutX() + velocity);
    }

}