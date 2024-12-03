package com.example.demo;

/**
 * Abstract class representing a destructible active actor in the game.
 * Extends ActiveActor and implements Destructible interface.
 * Provides additional functionality for handling destruction.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

    private boolean isDestroyed;

    /**
     * Constructor for ActiveActorDestructible.
     *
     * @param imageName the name of the image representing the actor
     * @param imageHeight the height of the image representing the actor
     * @param initialXPos the initial X position of the actor
     * @param initialYPos the initial Y position of the actor
     */
    public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        super(imageName, imageHeight, initialXPos, initialYPos);
        isDestroyed = false;
    }

    /**
     * Enum representing the cause of destruction.
     */
    public enum DestroyedBy {
        NONE, USER_PROJECTILE, COLLISION_WITH_USER, PENETRATION
    }

    private DestroyedBy destroyedBy = DestroyedBy.NONE;

    @Override
    public abstract void updatePosition();

    public abstract void updateActor();

    @Override
    public abstract void takeDamage();

    /**
     * Destroys the actor and sets the default cause if not already set.
     */
    @Override
    public void destroy() {
        if (destroyedBy == DestroyedBy.NONE) {
            destroyedBy = DestroyedBy.PENETRATION; // Default cause if not set
        }
        setDestroyed();
    }

    /**
     * Gets the cause of destruction.
     *
     * @return the cause of destruction
     */
    public DestroyedBy getDestroyedBy() {
        return destroyedBy;
    }

    /**
     * Sets the cause of destruction.
     *
     * @param destroyedBy the cause of destruction
     */
    public void setDestroyedBy(DestroyedBy destroyedBy) {
        this.destroyedBy = destroyedBy;
    }

    /**
     * Marks the actor as destroyed.
     */
    protected void setDestroyed() {
        this.isDestroyed = true;
    }

    /**
     * Checks if the actor is destroyed.
     *
     * @return true if the actor is destroyed, false otherwise
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }
}