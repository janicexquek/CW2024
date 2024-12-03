package com.example.demo;

import javafx.scene.image.*;

import java.util.Objects;

/**
 * Abstract class representing an active actor in the game.
 * Provides basic structure and behavior for all active actors.
 */
public abstract class ActiveActor extends ImageView {

    private static final String IMAGE_LOCATION = "/com/example/demo/images/";

    /**
     * Constructor for ActiveActor.
     *
     * @param imageName the name of the image representing the actor
     * @param imageHeight the height of the image representing the actor
     * @param initialXPos the initial X position of the actor
     * @param initialYPos the initial Y position of the actor
     */
    public ActiveActor(String imageName, int imageHeight, double initialXPos, double initialYPos) {
        // Set the image for the actor using the provided image name
        this.setImage(new Image(Objects.requireNonNull(getClass().getResource(IMAGE_LOCATION + imageName)).toExternalForm()));
        this.setLayoutX(initialXPos);
        this.setLayoutY(initialYPos);
        this.setFitHeight(imageHeight);
        this.setPreserveRatio(true);
    }

    /**
     * Updates the position of the active actor.
     * This method must be implemented by subclasses.
     */
    public abstract void updatePosition();

    /**
     * Moves the actor horizontally by a specified amount.
     *
     * @param horizontalMove the amount to move the actor horizontally
     */
    protected void moveHorizontally(double horizontalMove) {
        this.setTranslateX(getTranslateX() + horizontalMove);
    }

    /**
     * Moves the actor vertically by a specified amount.
     *
     * @param verticalMove the amount to move the actor vertically
     */
    protected void moveVertically(double verticalMove) {
        this.setTranslateY(getTranslateY() + verticalMove);
    }
}