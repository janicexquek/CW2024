package com.example.demo.projectile;

import com.example.demo.ActiveActorDestructible;

/**
 * Abstract class representing a projectile in the game.
 * Provides the basic structure and behavior for all projectiles.
 */
public abstract class Projectile extends ActiveActorDestructible {

    /**
     * Constructor for Projectile.
     *
     * @param imageName the name of the image representing the projectile
     * @param imageHeight the height of the image representing the projectile
     * @param initialXPos the initial X position of the projectile
     * @param initialYPos the initial Y position of the projectile
     */
    public Projectile(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
    }

    /**
     * Takes damage and marks the projectile as destroyed.
     */
    @Override
    public void takeDamage() {
        this.destroy();
    }

    /**
     * Updates the position of the projectile.
     * This method must be implemented by subclasses.
     */
    @Override
    public abstract void updatePosition();
}