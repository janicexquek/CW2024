package com.example.demo.display;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * Class representing the heart display in the game.
 * Manages the display of heart images to indicate lives or health.
 */
public class HeartDisplay {

    private static final String HEART_IMAGE_NAME = "/com/example/demo/images/heart1.png";
    protected static final int HEART_HEIGHT = 50;
    private static final int INDEX_OF_FIRST_ITEM = 0;
    protected HBox container;
    private double containerXPosition;
    private double containerYPosition;
    protected int numberOfHeartsToDisplay;

    /**
     * Constructor for HeartDisplay.
     *
     * @param xPosition the x position of the container
     * @param yPosition the y position of the container
     * @param heartsToDisplay the number of hearts to display
     */
    public HeartDisplay(double xPosition, double yPosition, int heartsToDisplay) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.numberOfHeartsToDisplay = heartsToDisplay;
        initializeContainer();
        initializeHearts();
    }

    /**
     * Initializes the container for the heart display.
     */
    protected void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

    /**
     * Initializes the heart images and adds them to the container.
     */
    protected void initializeHearts() {
        Image heartImage;
        try {
            // Ensure the resource is not null
            heartImage = new Image(Objects.requireNonNull(getClass().getResource(HEART_IMAGE_NAME),
                    "Heart image resource not found: " + HEART_IMAGE_NAME
            ).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            // Handle the missing resource, e.g., use a fallback image or skip adding hearts
            return;
        } catch (Exception e) {
            System.err.println("Failed to load heart image: " + HEART_IMAGE_NAME);
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < numberOfHeartsToDisplay; i++) {
            ImageView heart = new ImageView(heartImage);
            heart.setFitHeight(HEART_HEIGHT);
            heart.setPreserveRatio(true);
            container.getChildren().add(heart);
        }
    }

    /**
     * Removes one heart from the display.
     */
    public void removeHeart() {
        if (!container.getChildren().isEmpty())
            container.getChildren().remove(INDEX_OF_FIRST_ITEM);
    }

    /**
     * Returns the container for the heart display.
     *
     * @return the HBox container
     */
    public HBox getContainer() {
        return container;
    }
}