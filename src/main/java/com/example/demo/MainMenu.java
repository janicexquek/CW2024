package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainMenu {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background3.jpg";

    private final Stage stage;
    private final Controller controller;

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    public MainMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        // Load custom fonts
        loadCustomFonts();
    }

    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // --- Main Menu Title ---
        Label titleLabel = new Label("SKY BATTLE");
        titleLabel.setFont(Font.font(customFonts.get("Cartoon cookies").getName(), 100));
        titleLabel.getStyleClass().add("settings-title");

        // Create custom buttons
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
        VBox buttonLayout = new VBox(20, playButton, settingsButton, instructionsButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Create a BorderPane for main layout
        BorderPane mainLayout = new BorderPane();

        // Place the title at the top
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        mainLayout.setTop(titleLabel);

        // Place the buttons at the center with margin
        mainLayout.setCenter(buttonLayout);
        BorderPane.setMargin(buttonLayout, new Insets(10, 0, 120, 0)); // Shift buttons downward by 100 pixels

        // Top padding to give space for the title
        mainLayout.setPadding(new Insets(60, 0, 0, 0));

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout);
        // --- Add the Quit (x.png) Button in Top-Left Corner ---
        // Load the x.png image
        ImageView closeImageView = new ImageView(new Image(getClass().getResource("/com/example/demo/images/x.png").toExternalForm()));
        closeImageView.setFitWidth(30); // Adjust size as needed
        closeImageView.setFitHeight(30);
        closeImageView.setPreserveRatio(true);
        closeImageView.setCursor(Cursor.HAND); // Change cursor to hand on hover

        // Set alignment to top-left and add margin
        StackPane.setAlignment(closeImageView, Pos.TOP_LEFT);
        StackPane.setMargin(closeImageView, new Insets(30, 0, 0, 30)); // 10px from top and left

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



    private StackPane createCustomButton(String text) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(getClass().getResource("/com/example/demo/images/ButtonText_Large_Round.png").toExternalForm()));
        buttonImageView.setFitWidth(180); // Set desired width
        buttonImageView.setFitHeight(60); // Set desired height
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        label.getStyleClass().add("button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button");

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


    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
        };

        for (String fontPath : fontPaths) {
            try {
                Font font = Font.loadFont(getClass().getResourceAsStream(fontPath), 10);
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    // Store the font with its name for later use
                    customFonts.put(font.getName(), font);
                    System.out.println("Loaded font: " + font.getName());
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }

}
