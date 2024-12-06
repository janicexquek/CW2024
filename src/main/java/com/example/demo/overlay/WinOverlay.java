// File: com/example/demo/overlay/WinOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.MessageBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Class representing a win overlay in the game.
 * Manages the display and interactions of the win overlay.
 */
public class WinOverlay extends BaseOverlay {

    private MessageBox messageBox;

    /**
     * Constructor for WinOverlay.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public WinOverlay(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    protected StackPane createMessageBox() {
        // Define message box parameters
        double boxWidth = 500;
        double boxHeight = 500;
        String titleText = "VICTORY!";
        String subtitleText = "LEVEL X COMPLETED";
        String achievementText = "Achievement Unlocked!";
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
    public void showWinOverlay() {
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    /**
     * Hides the overlay.
     */
    public void hideWinOverlay() {
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }

    /**
     * Initializes buttons and adds them to the overlay.
     *
     * @param backCallback the callback to run when the back button is pressed
     * @param nextCallback the callback to run when the next button is pressed
     * @param restartCallback the callback to run when the restart button is pressed
     * @param levelName the name of the level to display
     */
    public void initializeButtons(Runnable backCallback, Runnable nextCallback, Runnable restartCallback, String levelName) {
        double boxWidth = 500;
        if (buttonsInitialized) {
            return;
        }

        // Create the buttons using ButtonFactory
        StackPane backButton = buttonFactory.createCustomButton("Main Menu", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        StackPane restartButton = buttonFactory.createCustomButton("Restart", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        StackPane nextButton = null;
        Label completedMessage = null; // Declare outside the if block
        if (nextCallback != null) {
            nextButton = buttonFactory.createCustomButton("Next Level", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        } else {
            completedMessage = new Label("Congratulations!\nYou have completed all levels.");
            completedMessage.setTextFill(Color.WHITE);
            completedMessage.setFont(fontManager.getFont("Pixel Digivolve", 16));
            completedMessage.setWrapText(true);
            completedMessage.setAlignment(Pos.CENTER);
            completedMessage.setTextAlignment(TextAlignment.CENTER);
            completedMessage.setMaxWidth(boxWidth - 40); // boxWidth - 20 padding
        }

        // Assign actions to the buttons
        backButton.setOnMouseClicked(e -> {
            hideWinOverlay(); // Hide the overlay
            if (backCallback != null) {
                backCallback.run(); // Go back to the main menu
            }
        });

        restartButton.setOnMouseClicked(e -> {
            hideWinOverlay(); // Hide the overlay
            if (restartCallback != null) {
                restartCallback.run(); // Restart the game
            }
        });

        if (nextButton != null) {
            nextButton.setOnMouseClicked(e -> {
                hideWinOverlay(); // Hide the overlay
                nextCallback.run(); // Proceed to the next level
            });
        }

        // Create an HBox to hold the buttons horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonBox.getChildren().addAll(backButton, restartButton);
        // Create an VBox to hold the buttons vertically
        VBox allButtonBox = new VBox(10);
        allButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        if (nextButton != null) {
            allButtonBox.getChildren().add(nextButton); // Conditionally add Next Level button
        }
        if(nextButton == null){
            allButtonBox.getChildren().add(completedMessage); // Conditionally add completedMessage
        }
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
            messageBox.setSubtitleText(levelName + " COMPLETED");
        }
    }

    /**
     * Sets the achievement message.
     *
     * @param message the achievement message to display
     */
    public void setAchievementMessage(String message) {
        if (messageBox != null) {
            messageBox.setAchievementText(message);
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
