package com.example.demo.plane;

import com.example.demo.ActiveActorDestructible;
import javafx.geometry.Bounds;

/**
 * Abstract class representing a fighter plane in the game.
 * Manages the basic properties and behaviors of a fighter plane.
 */
public abstract class FighterPlane extends ActiveActorDestructible {

    private int health;

    /**
     * Constructor for FighterPlane.
     *
     * @param imageName the name of the image representing the plane
     * @param imageHeight the height of the image
     * @param initialXPos the initial X position of the plane
     * @param initialYPos the initial Y position of the plane
     * @param health the initial health of the plane
     */
    public FighterPlane(String imageName, int imageHeight, double initialXPos, double initialYPos, int health) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        this.health = health;
    }

    /**
     * Abstract method to fire a projectile.
     *
     * @return the fired projectile
     */
    public abstract ActiveActorDestructible fireProjectile();

    /**
     * Takes damage and decreases the health of the plane.
     * Destroys the plane if health drops to zero.
     */
    @Override
    public void takeDamage() {
        health--;
        if (healthAtZero()) {
            this.destroy();
        }
    }

    /**
     * Gets the X position for the projectile.
     *
     * @return the X position for the projectile
     */
    protected double getProjectileXPosition() {
        Bounds bounds = localToScene(getBoundsInLocal());
        return bounds.getMinX();
    }

    /**
     * Gets the Y position for the projectile with an offset.
     *
     * @param yPositionOffset the offset for the Y position
     * @return the Y position for the projectile
     */
    protected double getProjectileYPosition(double yPositionOffset) {
        Bounds bounds = localToScene(getBoundsInLocal());
        return bounds.getMinY() + bounds.getHeight() / 2 + yPositionOffset;
    }

    /**
     * Checks if the health of the plane is zero.
     *
     * @return true if health is zero, false otherwise
     */
    private boolean healthAtZero() {
        return health == 0;
    }

    /**
     * Gets the current health of the plane.
     *
     * @return the current health
     */
    public int getHealth() {
        return health;
    }
}