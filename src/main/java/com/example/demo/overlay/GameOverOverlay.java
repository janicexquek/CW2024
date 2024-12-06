// File: com/example/demo/overlay/GameOverOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;

/**
 * Class representing a game over overlay in the game.
 * Manages the display and interactions of the game over overlay.
 */
public class GameOverOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png"; // Path to the background image for the message box
    private static final String BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png"; // Path to the button image

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;

    // Flags to prevent duplicate button initialization
    private boolean buttonsInitialized = false;

    // Labels to display information
    private Label levelInfoLabel;
    private Label currentTimeLabel;
    private Label fastestTimeLabel;

    /**
     * Constructor for GameOverOverlay.
     *
     * @param screenWidth            the width of the screen
     * @param screenHeight           the height of the screen
     */
    public GameOverOverlay(double screenWidth, double screenHeight) {
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();

        // Set the size of the overlay to cover the entire screen
        setPrefSize(screenWidth, screenHeight);
        setMaxSize(screenWidth, screenHeight);
        setMinSize(screenWidth, screenHeight);

        // Make the overlay non-focusable and ignore mouse events when not visible
        setFocusTraversable(false);
        setMouseTransparent(true);

        // Semi-transparent background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // 50% opacity

        // Link the CSS stylesheet
        linkStylesheet();

        // Create the message box
        StackPane messageBox = createMessageBox();

        // Add the message box to the center of the overlay
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);

        // Add a key event handler to consume ESC key when GameOverOverlay is active
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                // Optionally, ignore or consume the event to prevent underlying handlers
                event.consume();
            }
        });
    }

    /**
     * Links the CSS stylesheet to this GameOverOverlay.
     */
    private void linkStylesheet() {
        URL cssResource = getClass().getResource("/com/example/demo/styles/styles.css");
        if (cssResource == null) {
            System.err.println("CSS file not found!");
        } else {
            getStylesheets().add(cssResource.toExternalForm());
        }
    }

    /**
     * Creates the message box with a background image and a message label.
     *
     * @return the created StackPane representing the message box
     */
    private StackPane createMessageBox() {
        // Fixed size for the message box
        double boxWidth = 500;
        double boxHeight = 500;

        // Load box1.png as the background for the message box
        ImageView background;
        try {
            background = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(BOX_IMAGE_NAME)).toExternalForm()));
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        } catch (Exception e) {
            System.err.println("Failed to load GameOverOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the game over message label
        Label gameOverMessage = new Label("GAME OVER");
        gameOverMessage.setTextFill(Color.web("#f5f551"));
        gameOverMessage.setFont(fontManager.getFont("Cartoon cookies", 50));
        gameOverMessage.setWrapText(true);
        gameOverMessage.setAlignment(Pos.TOP_CENTER);
        gameOverMessage.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create the level info label
        levelInfoLabel = new Label("TRY AGAIN LEVEL X"); // Placeholder text
        levelInfoLabel.setTextFill(Color.WHITE);
        levelInfoLabel.setFont(fontManager.getFont("Sugar Bomb", 20)); // Use 'Sugar Bomb' or fallback
        levelInfoLabel.setWrapText(true);
        levelInfoLabel.setAlignment(Pos.CENTER);
        levelInfoLabel.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Current Time Label
        currentTimeLabel = new Label("Time: 00:00");
        currentTimeLabel.setTextFill(Color.WHITE);
        currentTimeLabel.setFont(fontManager.getFont("Pixel Digivolve", 16));
        currentTimeLabel.setWrapText(true);
        currentTimeLabel.setAlignment(Pos.CENTER);
        currentTimeLabel.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Fastest Time Label
        fastestTimeLabel = new Label("Fastest Time: 00:00");
        fastestTimeLabel.setTextFill(Color.WHITE);
        fastestTimeLabel.setFont(fontManager.getFont("Pixel Digivolve", 16));
        fastestTimeLabel.setWrapText(true);
        fastestTimeLabel.setAlignment(Pos.CENTER);
        fastestTimeLabel.setMaxWidth(boxWidth - 40); // Padding inside the box

        // Create a VBox to hold the message and button
        VBox vbox = new VBox(30); // Spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);

        vbox.getChildren().addAll(gameOverMessage, levelInfoLabel, currentTimeLabel, fastestTimeLabel); // Add messages to VBox

        // Stack the background and the message box
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

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

        // Create the back to main menu button with image background and assign action
        StackPane backButton = buttonFactory.createCustomButton("Main Menu", "Sugar Bomb", 16, 150, 60, BUTTON_IMAGE_NAME);
        backButton.setOnMouseClicked(e -> {
            hideGameOverOverlay(); // Hide the overlay
            if (backCallback != null) {
                backCallback.run(); // Go back to the main menu
            }
        });

        // Create the "Restart" button
        StackPane restartButton = buttonFactory.createCustomButton("Restart", "Sugar Bomb", 16, 150, 60, BUTTON_IMAGE_NAME);
        restartButton.setOnMouseClicked(e -> {
            hideGameOverOverlay(); // Hide the overlay
            if (restartCallback != null) {
                restartCallback.run(); // Restart the game
            }
        });

        // Create an HBox to hold the button horizontally
        HBox buttonBox = new HBox(20); // 20px spacing between buttons if more are added
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, restartButton);

        // Add the buttonBox to the overlay's VBox
        // Assuming the messageBox's VBox is the second child (index 1)
        if (!getChildren().isEmpty() && getChildren().get(0) instanceof StackPane messageBox) {
            if (messageBox.getChildren().size() > 1 && messageBox.getChildren().get(1) instanceof VBox vbox) {
                // Remove existing HBox if any to prevent duplication
                vbox.getChildren().removeIf(node -> node instanceof HBox);
                // Add the new buttonBox
                vbox.getChildren().add(buttonBox);
                buttonsInitialized = true;
            }
        }
        // Set the level information
        setLevelInfo(levelName);
    }

    /**
     * Sets the level information.
     *
     * @param levelName the name of the level to display
     */
    public void setLevelInfo(String levelName) {
        if (levelInfoLabel != null) {
            levelInfoLabel.setText("TRY AGAIN " + levelName);
        }
    }

    /**
     * Sets the current time and fastest time.
     *
     * @param currentTime the current time to display
     * @param fastestTime the fastest time to display
     */
    public void setTimes(String currentTime, String fastestTime) {
        Platform.runLater(() -> {
            currentTimeLabel.setText("Time: " + currentTime);
            fastestTimeLabel.setText("Fastest Time: " + fastestTime);
        });
    }
}
