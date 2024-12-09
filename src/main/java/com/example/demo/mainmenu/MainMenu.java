// File: com/example/demo/mainmenu/MainMenu.java

package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

/**
 * Class representing the main menu in the game.
 * Manages the display and interactions of the main menu.
 */
public class MainMenu {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";
    private static final String CLOSE_IMAGE_NAME = "/com/example/demo/images/x.png";

    private final Stage stage;
    private final Controller controller;

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;

    // Class-level UI components for testing
    private StackPane statButton;
    private StackPane storeButton;
    private StackPane playButton;
    private StackPane settingsButton;
    private StackPane instructionsButton;
    private ImageView closeImageView;

    /**
     * Constructor for MainMenu.
     *
     * @param stage      the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public MainMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();
    }

    /**
     * Displays the main menu.
     */
    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_NAME)).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // --- Main Menu Title ---
        Label titleLabel = new Label("SKY BATTLE");
        titleLabel.setFont(fontManager.getFont("Cartoon cookies", 100));
        titleLabel.getStyleClass().add("title-text");

        // Create custom buttons using ButtonFactory
        statButton = buttonFactory.createStickerButton("/com/example/demo/images/statistic.png", 40, 40);
        statButton.setOnMouseClicked(this::handleStatButtonClick);

        storeButton = buttonFactory.createStickerButton("/com/example/demo/images/aircraft.png", 40, 40);
        storeButton.setOnMouseClicked(this::handleStoreButtonClick);

        HBox stickerButtonLayout = new HBox(20, statButton, storeButton);
        stickerButtonLayout.setAlignment(Pos.CENTER);

        playButton = buttonFactory.createCustomButton("Play", "Sugar Bomb", 20, 180, 60, "/com/example/demo/images/ButtonText_Large_Round.png");
        playButton.setOnMouseClicked(this::handlePlayButtonClick);

        settingsButton = buttonFactory.createCustomButton("Settings", "Sugar Bomb", 20, 180, 60, "/com/example/demo/images/ButtonText_Large_Round.png");
        settingsButton.setOnMouseClicked(this::handleSettingsButtonClick);

        instructionsButton = buttonFactory.createCustomButton("Instructions", "Sugar Bomb", 20, 180, 60, "/com/example/demo/images/ButtonText_Large_Round.png");
        instructionsButton.setOnMouseClicked(this::handleInstructionsButtonClick);

        // Arrange buttons in VBox
        VBox buttonLayout = new VBox(20, stickerButtonLayout, playButton, settingsButton, instructionsButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Create a BorderPane for main layout
        BorderPane mainLayout = new BorderPane();

        // Place the title at the top
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        mainLayout.setTop(titleLabel);

        // Place the buttons at the center with margin
        mainLayout.setCenter(buttonLayout);
        BorderPane.setMargin(buttonLayout, new Insets(10, 0, 190, 0)); // Shift buttons downward by 190 pixels

        // Top padding to give space for the title
        mainLayout.setPadding(new Insets(50, 0, 0, 0));

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout);

        // --- Add the Quit (x.png) Button in Top-Left Corner ---
        // Load the x.png image
        closeImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(CLOSE_IMAGE_NAME)).toExternalForm()));
        closeImageView.setFitWidth(30); // Adjust size as needed
        closeImageView.setFitHeight(30);
        closeImageView.setPreserveRatio(true);
        closeImageView.setCursor(Cursor.HAND); // Change cursor to hand on hover

        // Set alignment to top-left and add margin
        StackPane.setAlignment(closeImageView, Pos.TOP_LEFT);
        StackPane.setMargin(closeImageView, new Insets(40, 0, 160, 40)); // 40px from top and left

        // Add click handler to close the stage
        closeImageView.setOnMouseClicked(this::handleCloseButtonClick);

        // Add the closeImageView to the root StackPane
        root.getChildren().add(closeImageView);
        // --- End of Quit Button Addition ---

        // Create and set the scene
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

        // Link the CSS stylesheet
        URL cssResource = getClass().getResource("/com/example/demo/styles/styles.css");
        if (cssResource == null) {
            System.err.println("CSS file not found!");
        } else {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the Play button click event.
     *
     * @param event the mouse event
     */
    private void handlePlayButtonClick(MouseEvent event) {
        controller.startGame();
    }

    /**
     * Handles the Settings button click event.
     *
     * @param event the mouse event
     */
    private void handleSettingsButtonClick(MouseEvent event) {
        Settings settings = new Settings(stage, controller);
        settings.show();
    }

    /**
     * Handles the Instructions button click event.
     *
     * @param event the mouse event
     */
    private void handleInstructionsButtonClick(MouseEvent event) {
        InstructionsPage instructionsPage = new InstructionsPage(stage, controller);
        instructionsPage.show();
    }

    /**
     * Handles the Statistics button click event.
     *
     * @param event the mouse event
     */
    private void handleStatButtonClick(MouseEvent event) {
        ScoreboardPage scoreboardPage = new ScoreboardPage(stage, controller);
        scoreboardPage.show();
    }

    /**
     * Handles the Store button click event.
     *
     * @param event the mouse event
     */
    private void handleStoreButtonClick(MouseEvent event) {
        StorePage storePage = new StorePage(stage, controller);
        storePage.show();
    }

    /**
     * Handles the Close button click event.
     *
     * @param event the mouse event
     */
    private void handleCloseButtonClick(MouseEvent event) {
        stage.close();
    }

    // --- Getter Methods for Testing ---

    /**
     * Gets the Play button.
     *
     * @return the Play button as a StackPane
     */
    public StackPane getPlayButton() {
        return playButton;
    }

    /**
     * Gets the Settings button.
     *
     * @return the Settings button as a StackPane
     */
    public StackPane getSettingsButton() {
        return settingsButton;
    }

    /**
     * Gets the Instructions button.
     *
     * @return the Instructions button as a StackPane
     */
    public StackPane getInstructionsButton() {
        return instructionsButton;
    }

    /**
     * Gets the Statistics button.
     *
     * @return the Statistics button as a StackPane
     */
    public StackPane getStatButton() {
        return statButton;
    }

    /**
     * Gets the Close button ImageView.
     *
     * @return the Close button as an ImageView
     */
    public ImageView getCloseImageView() {
        return closeImageView;
    }
}
