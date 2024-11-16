package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;


public class MainMenu {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background1.jpg";

    private final Stage stage;
    private final Controller controller;

    public MainMenu(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        // Load the background image using the static final String
        Image backgroundImage = new Image(getClass().getResource(BACKGROUND_IMAGE_NAME).toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);

        // Create buttons
        Button startButton = new Button("Start Game");
        Button settingsButton = new Button("Settings");
        Button quitButton = new Button("Quit");

        // Style buttons (optional)
        startButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        quitButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");
        settingsButton.setStyle("-fx-font-size: 18px; -fx-padding: 10px 20px;");

        // Set button actions
        startButton.setOnAction(e -> controller.startGame());
        settingsButton.setOnAction(e -> {
            // Navigate to the settings page, passing Controller
            Settings settings = new Settings(stage, controller);
            settings.show();
        });
        quitButton.setOnAction(e -> stage.close());

        // Arrange buttons in a vertical layout
        VBox buttonLayout = new VBox(20);
        buttonLayout.setStyle("-fx-alignment: center;");
        buttonLayout.getChildren().addAll(startButton,settingsButton,quitButton);

        // Create a StackPane to layer the background and buttons
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, buttonLayout);

        // Create and set the scene
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}