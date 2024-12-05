package com.example.demo;

import com.example.demo.plane.UserPlane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * InputHandler is responsible for handling user input events.
 * It attaches key event handlers to the background and updates
 * the user's plane based on the input.
 */
public class InputHandler {

    private UserPlane user;
    private LevelParent levelParent;

    /**
     * Constructs a new InputHandler.
     *
     * @param user        The user's plane.
     * @param levelParent The LevelParent instance.
     */
    public InputHandler(UserPlane user, LevelParent levelParent) {
        this.user = user;
        this.levelParent = levelParent;
    }

    /**
     * Attaches input handlers to the background image.
     *
     * @param background The background ImageView to attach handlers to.
     */
    public void attachInputHandlers(ImageView background) {
        background.setOnKeyPressed(this::handleKeyPressed);
        background.setOnKeyReleased(this::handleKeyReleased);
    }

    /**
     * Handles key pressed events.
     *
     * @param event The KeyEvent object.
     */
    private void handleKeyPressed(KeyEvent event) {
        KeyCode kc = event.getCode();
        if (kc == KeyCode.UP) user.moveUp();
        if (kc == KeyCode.DOWN) user.moveDown();
        if (kc == KeyCode.SPACE) levelParent.fireProjectile();
        if (kc == KeyCode.ESCAPE) levelParent.togglePause();
    }

    /**
     * Handles key released events.
     *
     * @param event The KeyEvent object.
     */
    private void handleKeyReleased(KeyEvent event) {
        KeyCode kc = event.getCode();
        if (kc == KeyCode.UP || kc == KeyCode.DOWN) user.stop();
    }
}
