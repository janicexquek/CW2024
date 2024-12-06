package com.example.demo.gamemanager;

import com.example.demo.LevelParent;
import javafx.scene.Group;
import javafx.scene.Scene;

/**
 * Manages the initialization and setup of the game scene, including the root node and background.
 */
public class SceneManager {

    private final Group root;
    private final Scene scene;
    private final SceneInitializer sceneInitializer;

    /**
     * Constructs a new SceneManager with the specified parameters.
     *
     * @param backgroundImageName The filename of the background image.
     * @param screenWidth         The width of the game screen.
     * @param screenHeight        The height of the game screen.
     * @param inputHandler        The input handler for managing user input.
     */
    public SceneManager(String backgroundImageName, double screenWidth, double screenHeight, InputHandler inputHandler) {
        this.root = new Group();
        this.scene = new Scene(root, screenWidth, screenHeight);
        this.sceneInitializer = new SceneInitializer(root, backgroundImageName, screenWidth, screenHeight, inputHandler);
    }

    /**
     * Initializes the scene by setting up the background and any other scene-level configurations.
     */
    public void initializeScene() {
        sceneInitializer.initializeBackground();
        // Additional scene-level initialization can be added here if needed
    }

    /**
     * Returns the root node of the scene.
     *
     * @return The root Group.
     */
    public Group getRoot() {
        return root;
    }

    /**
     * Returns the initialized Scene.
     *
     * @return The Scene instance.
     */
    public Scene getScene() {
        return scene;
    }
}
