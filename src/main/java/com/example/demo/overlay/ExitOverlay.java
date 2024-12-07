// File: com/example/demo/overlay/ExitOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.MessageBox;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Class representing the exit overlay in the game.
 * Manages the display and interactions of the exit overlay.
 */
public class ExitOverlay extends BaseOverlay {

    private MessageBox messageBox;

    // Callbacks for button actions
    private Runnable resumeGameCallback;
    private Runnable backToMainMenuCallback;
    private Runnable hideOverlayCallback;

    /**
     * Constructor for ExitOverlay.
     *
     * @param screenWidth            the width of the screen
     * @param screenHeight           the height of the screen
     * @param resumeGameCallback     the callback to resume the game
     * @param backToMainMenuCallback the callback to go back to the main menu
     * @param hideOverlayCallback    the callback to hide the overlay
     */
    public ExitOverlay(double screenWidth, double screenHeight,
                       Runnable resumeGameCallback,
                       Runnable backToMainMenuCallback,
                       Runnable hideOverlayCallback) {
        super(screenWidth, screenHeight);
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.hideOverlayCallback = hideOverlayCallback;
    }

    @Override
    protected StackPane createMessageBox() {
        // Define message box parameters
        double boxWidth = 500;
        double boxHeight = 500;
        String titleText = "Exit Game ?";
        String subtitleText = "Do you want to CONTINUE your game \nor \nBACK to Main Menu ?";
        String achievementText = null; // No achievement message for exit
        String timeText = ""; // Not applicable
        String fastestTimeText = ""; // Not applicable

        // Create MessageBox instance
        messageBox = new MessageBox(boxWidth, boxHeight, titleText, subtitleText,
                achievementText, timeText, fastestTimeText);

        return messageBox;
    }

    /**
     * Initializes buttons and adds them to the overlay.
     *
     * @param resumeGameCallback     the callback to run when the resume button is pressed
     * @param backToMainMenuCallback the callback to run when the back to main menu button is pressed
     * @param hideOverlayCallback    the callback to run when the overlay needs to be hidden
     */
    public void initializeButtons(Runnable resumeGameCallback, Runnable backToMainMenuCallback, Runnable hideOverlayCallback) {
        if (buttonsInitialized) {
            return;
        }

        // Update callbacks if provided
        if (resumeGameCallback != null) {
            this.resumeGameCallback = resumeGameCallback;
        }
        if (backToMainMenuCallback != null) {
            this.backToMainMenuCallback = backToMainMenuCallback;
        }
        if (hideOverlayCallback != null) {
            this.hideOverlayCallback = hideOverlayCallback;
        }

        // Create the buttons using ButtonFactory
        StackPane continueButton = buttonFactory.createCustomButton("Continue", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");
        StackPane mainMenuButton = buttonFactory.createCustomButton("Main Menu", "Sugar Bomb", 16, 150, 60, "/com/example/demo/images/ButtonText_Small_Blue_Round.png");

        // Assign actions to the buttons
        continueButton.setOnMouseClicked(e -> {
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Hide the overlay via callback
            }
            if (resumeGameCallback != null) {
                resumeGameCallback.run(); // Resume the game
            }
        });
        continueButton.setId("continueButton");

        mainMenuButton.setOnMouseClicked(e -> {
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Hide the overlay via callback
            }
            if (backToMainMenuCallback != null) {
                backToMainMenuCallback.run(); // Go back to the main menu
            }
        });
        mainMenuButton.setId("mainMenuButton");

        // Create an HBox to hold the buttons horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueButton, mainMenuButton);

        // Create a VBox to hold the buttons vertically (if needed)
        VBox allButtonBox = new VBox(10);
        allButtonBox.setAlignment(Pos.CENTER);
        allButtonBox.getChildren().add(buttonBox);

        // Add the allButtonBox to the overlay's VBox within the MessageBox
        // Assuming the messageBox's VBox is the second child (index 1)
        VBox messageVBox = (VBox) messageBox.getChildren().get(1); // Accessing the VBox inside MessageBox
        messageVBox.getChildren().add(allButtonBox);
        buttonsInitialized = true;
    }

    /**
     * Displays the exit overlay.
     */
    public void showExitOverlay() {
        setVisible(true);
        setMouseTransparent(false); // Enable interactions
        toFront(); // Bring to front
    }

    /**
     * Hides the exit overlay.
     */
    public void hideExitOverlay() {
        setVisible(false);
        setMouseTransparent(true); // Disable interactions
    }
}
