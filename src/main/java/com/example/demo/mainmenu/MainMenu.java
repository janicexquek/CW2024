package com.example.demo.mainmenu;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing the main menu in the game.
 * Manages the display and interactions of the main menu.
 */
public class MainMenu {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";

    private final Stage stage;
    private final Controller controller;

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    /**
     * Constructor for MainMenu.
     *
     * @param stage the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public MainMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        // Load custom fonts
        loadCustomFonts();
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
        titleLabel.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 100));
        titleLabel.getStyleClass().add("title-text");

        // Create custom buttons
        StackPane statButton = createStickerButton("/com/example/demo/images/statistic.png");
        statButton.setOnMouseClicked(e -> {
            ScoreboardPage scoreboardPage = new ScoreboardPage(stage, controller);
            scoreboardPage.show();
        });

        // --- Create Store Button ---
        StackPane storeButton = createStickerButton("/com/example/demo/images/aircraft.png");
        storeButton.setOnMouseClicked(e -> {
            StorePage storePage = new StorePage(stage, controller);
            storePage.show();
        });

        HBox stickerButtonLayout = new HBox (20,statButton, storeButton);
        stickerButtonLayout.setAlignment(Pos.CENTER);

        StackPane playButton = createCustomButton("Play");
        playButton.setOnMouseClicked(e -> controller.startGame());

        StackPane settingsButton = createCustomButton("Settings");
        settingsButton.setOnMouseClicked(e -> {
            Settings settings = new Settings(stage, controller);
            settings.show();
        });

        StackPane instructionsButton = createCustomButton("Instructions");
        instructionsButton.setOnMouseClicked(e -> {
            // Navigate to the Instructions page
            InstructionsPage instructionsPage = new InstructionsPage(stage, controller);
            instructionsPage.show();
        });

        // Arrange buttons in VBox
        VBox buttonLayout = new VBox(20, stickerButtonLayout,playButton, settingsButton, instructionsButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Create a BorderPane for main layout
        BorderPane mainLayout = new BorderPane();

        // Place the title at the top
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        mainLayout.setTop(titleLabel);

        // Place the buttons at the center with margin
        mainLayout.setCenter(buttonLayout);
        BorderPane.setMargin(buttonLayout, new Insets(10, 0, 190, 0)); // Shift buttons downward by 100 pixels

        // Top padding to give space for the title
        mainLayout.setPadding(new Insets(50, 0, 0, 0));

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout);
        // --- Add the Quit (x.png) Button in Top-Left Corner ---
        // Load the x.png image
        ImageView closeImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/x.png")).toExternalForm()));
        closeImageView.setFitWidth(30); // Adjust size as needed
        closeImageView.setFitHeight(30);
        closeImageView.setPreserveRatio(true);
        closeImageView.setCursor(Cursor.HAND); // Change cursor to hand on hover

        // Set alignment to top-left and add margin
        StackPane.setAlignment(closeImageView, Pos.TOP_LEFT);
        StackPane.setMargin(closeImageView, new Insets(40, 0, 160, 40)); // 40px from top and left

        // Add click handler to close the stage
        closeImageView.setOnMouseClicked(e -> stage.close());

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
     * Creates a sticker button with an image.
     *
     * @param imagePath the path to the image
     * @return the created StackPane representing the button
     */
    private StackPane createStickerButton(String imagePath){
        // Load the button background image
        Image buttonImage;
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                throw new IllegalArgumentException("Image not found: " + imagePath);
            }
            buttonImage = new Image(imageUrl.toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load sticker button image.");
            e.printStackTrace();
            // Optionally, use a placeholder or return null
            buttonImage = null;
        }

        ImageView buttonImageView = new ImageView(buttonImage);
        if (buttonImage != null) {
            buttonImageView.setFitWidth(40); // Set desired width
            buttonImageView.setFitHeight(40); // Set desired height
            buttonImageView.setPreserveRatio(false);
        }

        // Create a Circle around the ImageView
        Circle hoverCircle = new Circle();
        hoverCircle.setStroke(Color.web ("#5b6980")); // Set desired circle color
        hoverCircle.setStrokeWidth(3.5);       // Set desired stroke width
        hoverCircle.setFill(Color.web("#5b6980")); // Set fill to specific color
        hoverCircle.setOpacity(0);           // Initially invisible

        // Bind the Circle's radius to the ImageView's size for responsiveness
        hoverCircle.radiusProperty().bind(
                buttonImageView.fitWidthProperty().divide(2).add(5) // Adjust the "+5" for padding
        );
        // Center the Circle relative to the ImageView
        hoverCircle.centerXProperty().bind(buttonImageView.fitWidthProperty().divide(2));
        hoverCircle.centerYProperty().bind(buttonImageView.fitHeightProperty().divide(2));

        // Create a StackPane to layer the Circle and the ImageView
        // Add the Circle first so it's behind the ImageView
        StackPane stackPane = new StackPane(hoverCircle, buttonImageView);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");
        stackPane.setPickOnBounds(false); // Prevent mouse events on transparent areas

        // Create a ScaleTransition for enlarging the button on hover
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        // Create FadeTransitions for the Circle
        FadeTransition fadeIn = new FadeTransition(Duration.millis(150), hoverCircle);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(150), hoverCircle);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        // Add hover event handlers
        stackPane.setOnMouseEntered(e -> {
            // Stop any ongoing transitions
            scaleTransition.stop();
            fadeOut.stop();

            // Start the scale (enlarge) transition
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();

            // Start the fade-in transition for the Circle
            fadeIn.playFromStart();
        });

        stackPane.setOnMouseExited(e -> {
            // Stop any ongoing transitions
            scaleTransition.stop();
            fadeIn.stop();

            // Start the scale (shrink) transition
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();

            // Start the fade-out transition for the Circle
            fadeOut.playFromStart();
        });

        return stackPane;
    }

    /**
     * Creates a custom button with an image and text.
     *
     * @param text the text to display on the button
     * @return the created StackPane representing the button
     */
    private StackPane createCustomButton(String text) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/demo/images/ButtonText_Large_Round.png")).toExternalForm()));
        buttonImageView.setFitWidth(180); // Set desired width
        buttonImageView.setFitHeight(60); // Set desired height
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        // Set the Sugar Bomb font
        label.setFont(Font.font(customFonts.getOrDefault("Sugar Bomb",
                Font.font("Arial", 20)).getName(), 20));

        label.getStyleClass().add("button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");

        // Ensure the StackPane size matches the image size
        stackPane.setMinSize(buttonImageView.getFitWidth(), buttonImageView.getFitHeight());
        stackPane.setMaxSize(buttonImageView.getFitWidth(), buttonImageView.getFitHeight());

        // Prevent mouse events on transparent areas
        stackPane.setPickOnBounds(false);

        // Create a single ScaleTransition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        // Add hover effects
        stackPane.setOnMouseEntered(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        stackPane.setOnMouseExited(e -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });

        return stackPane;
    }

    /**
     * Loads custom fonts from resources.
     */
    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Add the new font path here
        };

        for (String fontPath : fontPaths) {
            try {
                Font font = Font.loadFont(getClass().getResourceAsStream(fontPath), 10);
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    // Store the font with its name for later use
                    customFonts.put(font.getName(), font);
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }

}