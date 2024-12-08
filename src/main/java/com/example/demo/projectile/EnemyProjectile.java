package com.example.demo.projectile;

/**
 * Class representing an enemy projectile in the game.
 * Manages the movement and behavior of the enemy projectile.
 */
public class EnemyProjectile extends Projectile {

    private static final String IMAGE_NAME = "enemyFire.png";
    private static final int IMAGE_HEIGHT = 20;
    private static final int HORIZONTAL_VELOCITY = -10;

    /**
     * Constructor for EnemyProjectile.
     *
     * @param initialXPos the initial X position of the enemy projectile
     * @param initialYPos the initial Y position of the enemy projectile
     */
    public EnemyProjectile(double initialXPos, double initialYPos) {
        super(IMAGE_NAME, IMAGE_HEIGHT, initialXPos, initialYPos);
    }

    /**
     * Updates the position of the enemy projectile.
     * Moves the projectile horizontally based on the defined velocity.
     */
    @Override
    public void updatePosition() {
        moveHorizontally(HORIZONTAL_VELOCITY);
    }

    /**
     * Updates the state of the enemy projectile.
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

    /**
     * Moves the actor horizontally by the specified velocity.
     * @param velocity the amount to move the actor horizontally
     */
    protected void moveHorizontally(double velocity) {
        setLayoutX(getLayoutX() + velocity);
    }
}