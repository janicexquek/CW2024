// File: com/example/demo/overlay/GameOverOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.MessageBox;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


/**
 * Class representing a game over overlay in the game.
 * Manages the display and interactions of the game over overlay.
 */
public class GameOverOverlay extends BaseOverlay {

    private MessageBox messageBox;

    /**
     * Constructor for GameOverOverlay.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public GameOverOverlay(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    protected StackPane createMessageBox() {
        // Define message box parameters
        double boxWidth = 500;
        double boxHeight = 500;
        String titleText = "GAME OVER";
        String subtitleText = "TRY AGAIN LEVEL X";
        String achievementText = null; // No achievement message in game over
        String timeText = "Time: 00:00";
        String fastestTimeText = "Fastest Time: 00:00";

        // Create MessageBox instance
        messageBox = new MessageBox(boxWidth, boxHeight, titleText, subtitleText,
                achievementText, timeText, fastestTimeText);

        return messageBox;
    }

    /**
     * Displays the overlay.
     */
    public void showGameOverOverlay() {
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    /**
     * Hides the overlay.
     */
    public void hideGameOverOverlay() {
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }

    /**
     * Initializes buttons and adds them to the overlay.
     *
     * @param backCallback    the callback to run when the back button is pressed
     * @param restartCallback the callback to run when the restart button is pressed
     * @param levelName       the name of the level to display
     */
    public void initializeButtons(Runnable backCallback, Runnable restartCallback, String levelName) {
        if (buttonsInitialized) {
            return;
        }

        // Create the buttons using ButtonFactory
        StackPane backButton = buttonFactory.createCustomButton("Main Menu", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        backButton.setId("mainMenuButton");
        StackPane restartButton = buttonFactory.createCustomButton("Restart", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        restartButton.setId("restartButton");

        // Assign actions to the buttons
        backButton.setOnMouseClicked(e -> {
            hideGameOverOverlay(); // Hide the overlay
            if (backCallback != null) {
                backCallback.run(); // Go back to the main menu
            }
        });

        restartButton.setOnMouseClicked(e -> {
            hideGameOverOverlay(); // Hide the overlay
            if (restartCallback != null) {
                restartCallback.run(); // Restart the game
            }
        });

        // Create an HBox to hold the buttons horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, restartButton);

        // Create a VBox to hold the buttons vertically
        VBox allButtonBox = new VBox(10);
        allButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        allButtonBox.getChildren().add(buttonBox);

        // Add the allButtonBox to the overlay's VBox within the MessageBox
        // Assuming the messageBox's VBox is the second child (index 1)
        VBox messageVBox = (VBox) messageBox.getChildren().get(1); // Accessing the VBox inside MessageBox
        messageVBox.getChildren().add(allButtonBox);
        buttonsInitialized = true;

        // Set the level information
        setLevelInfo(levelName);
    }

    /**
     * Sets the level information.
     *
     * @param levelName the name of the level to display
     */
    public void setLevelInfo(String levelName) {
        if (messageBox != null) {
            messageBox.setSubtitleText("TRY AGAIN " + levelName);
        }
    }

    /**
     * Sets the current time and fastest time.
     *
     * @param currentTime  the current time to display
     * @param fastestTime the fastest time to display
     */
    public void setTimes(String currentTime, String fastestTime) {
        if (messageBox != null) {
            messageBox.setTimeText("Time: " + currentTime);
            messageBox.setFastestTimeText("Fastest Time: " + fastestTime);
        }
    }
}
