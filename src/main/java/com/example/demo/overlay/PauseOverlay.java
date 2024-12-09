// File: com/example/demo/overlay/PauseOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.MessageBox;
import javafx.scene.layout.StackPane;

/**
 * Class representing a pause overlay in the game.
 * Manages the display and interactions of the pause overlay.
 */
public class PauseOverlay extends BaseOverlay {

    /**
     * Constructor for PauseOverlay.
     *
     * @param screenWidth         the width of the screen
     * @param screenHeight        the height of the screen
     * @param togglePauseCallback the callback to run when the pause is toggled
     */
    public PauseOverlay(double screenWidth, double screenHeight, Runnable togglePauseCallback) {
        super(screenWidth, screenHeight);
        // Callback for toggling pause (e.g., resuming the game)
    }

    @Override
    protected StackPane createMessageBox() {
        // Define message box parameters
        double boxWidth = 500;
        double boxHeight = 200;
        String titleText = "";
        String subtitleText = "Press ESC to continue the game";
        String achievementText = null; // No achievement message for pause
        String timeText = ""; // Not applicable
        String fastestTimeText = ""; // Not applicable

        // Create MessageBox instance without buttons

        return new MessageBox(boxWidth, boxHeight, titleText, subtitleText,
                achievementText, timeText, fastestTimeText);
    }
    /**
     * Hides the overlay.
     */
    public void hidePauseOverlay() {
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }
}
