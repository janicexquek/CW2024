package com.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;

public class MainMenu {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";

    private final Stage stage;
    private final Controller controller;

    public MainMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // Create custom buttons
        StackPane playButton = createCustomButton("Play");
        playButton.setOnMouseClicked(e -> controller.startGame());

        StackPane settingsButton = createCustomButton("Settings");
        settingsButton.setOnMouseClicked(e -> {
            Settings settings = new Settings(stage, controller);
            settings.show();
        });

        StackPane quitButton = createCustomButton("Quit");
        quitButton.setOnMouseClicked(e -> stage.close());

        // Arrange buttons in VBox
        VBox buttonLayout = new VBox(20, playButton, settingsButton, quitButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Create a StackPane to layer the background and buttons
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, buttonLayout);

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

}
