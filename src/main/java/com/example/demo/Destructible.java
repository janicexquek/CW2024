package com.example.demo;

/**
 * Interface representing a destructible entity in the game.
 * Provides methods for handling damage and destruction.
 */
public interface Destructible {

    /**
     * Method to be implemented for handling damage to the entity.
     */
    void takeDamage();

    /**
     * Method to be implemented for handling the destruction of the entity.
     */
    void destroy();
}