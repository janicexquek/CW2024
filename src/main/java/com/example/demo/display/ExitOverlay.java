// File: com/example/demo/display/ExitOverlay.java

package com.example.demo.display;

import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;

/**
 * Class representing the exit overlay in the game.
 * Manages the display and interactions of the exit overlay.
 */
public class ExitOverlay extends StackPane {

    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png";
    private static final String BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png";

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;

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
        this.resumeGameCallback = resumeGameCallback;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.hideOverlayCallback = hideOverlayCallback;

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

        // Create the message box
        StackPane messageBox = createMessageBox();

        // Add the message box to the center of the overlay
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);

        // Link the CSS stylesheet
        linkStylesheet();

        // Add a key event handler to consume ESC key when ExitOverlay is active
        // Prevent ESC key from triggering other actions
        addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);
    }

    /**
     * Links the CSS stylesheet to this ExitOverlay.
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
     * Creates the message box with title, message, and buttons.
     *
     * @return the created message box as a StackPane
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
            System.err.println("Failed to load ExitOverlay background image: " + BOX_IMAGE_NAME);
            e.printStackTrace();
            // Fallback to a semi-transparent dark box
            background = new ImageView();
            background.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");
            background.setFitWidth(boxWidth);
            background.setFitHeight(boxHeight);
        }

        // Create the title label
        Label titleLabel = new Label("Exit Game ?");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(fontManager.getFont("Cartoon cookies", 50));
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setMaxWidth(boxWidth - 40);

        // Create the message label
        Label messageLabel = new Label("Do you want to CONTINUE your game \nor \nBACK to Main Menu ?");
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setFont(fontManager.getFont("Pixel Digivolve", 20));
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setMaxWidth(boxWidth - 40);

        // Create buttons with actions that hide the overlay and then run the callbacks
        StackPane continueButton = buttonFactory.createCustomButton("Continue", "Sugar Bomb", 16, 180, 60, BUTTON_IMAGE_NAME);
        continueButton.setOnMouseClicked(e -> {
            hideExitOverlay(); // Hide the overlay
            if (resumeGameCallback != null) {
                resumeGameCallback.run(); // Resume the game
            }
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Notify LevelView or other components
            }
        });

        StackPane backButton = buttonFactory.createCustomButton("Main Menu", "Sugar Bomb", 16, 180, 60, BUTTON_IMAGE_NAME);
        backButton.setOnMouseClicked(e -> {
            hideExitOverlay(); // Hide the overlay
            if (backToMainMenuCallback != null) {
                backToMainMenuCallback.run(); // Go back to the main menu
            }
            if (hideOverlayCallback != null) {
                hideOverlayCallback.run(); // Notify LevelView or other components
            }
        });

        // Arrange buttons in an HBox
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueButton, backButton);

        // Create a VBox to hold the labels and buttons
        VBox vbox = new VBox(40); // Spacing between elements
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(boxWidth, boxHeight);
        vbox.setMaxSize(boxWidth, boxHeight);
        vbox.setMinSize(boxWidth, boxHeight);
        vbox.getChildren().addAll(titleLabel, messageLabel, buttonBox);

        // Stack the background and the message box
        StackPane messageBox = new StackPane(background, vbox);
        messageBox.setAlignment(Pos.CENTER);

        return messageBox;
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
