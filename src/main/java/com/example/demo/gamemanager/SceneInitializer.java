package com.example.demo.gamemanager;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.demo.gamemanager.InputHandler;
import com.example.demo.levelview.LevelView;

import java.util.Objects;

/**
 * Handles the initialization of the game scene, including setting up the background
 * and attaching input handlers.
 */
public class SceneInitializer {

    private final Group root;
    private final ImageView background;
    private final double screenWidth;
    private final double screenHeight;
    private final InputHandler inputHandler;
    protected LevelView levelView;

    /**
     * Constructs a new SceneInitializer with the specified parameters.
     *
     * @param root             The root group of the scene.
     * @param backgroundImage  The filename of the background image.
     * @param screenWidth      The width of the game screen.
     * @param screenHeight     The height of the game screen.
     * @param inputHandler     The input handler for managing user input.
     */
    public SceneInitializer(Group root, String backgroundImage, double screenWidth, double screenHeight, InputHandler inputHandler) {
        this.root = root;
        this.background = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource(backgroundImage)).toExternalForm()));
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.inputHandler = inputHandler;
    }

    /**
     * Initializes the background image and sets up key event handlers.
     * Sets the background image to be focus traversable and adjusts its size to fit the screen dimensions.
     * Adds key event handlers for user movement, firing projectiles, and toggling pause.
     * Ensures the background is added to the root group if not already present.
     */
    public void initializeBackground() {
        background.setFocusTraversable(true);
        background.setFitHeight(screenHeight);
        background.setFitWidth(screenWidth);
        inputHandler.attachInputHandlers(background);

        // Add background to root if not already added
        if (!root.getChildren().contains(background)) {
            root.getChildren().add(background);
        }
    }

    /**
     * Retrieves the background ImageView.
     *
     * @return The background ImageView.
     */
    public ImageView getBackground() {
        return background;
    }
}
