// File: com/example/demo/mainmenu/InstructionsPage.java

package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.net.URL;
import java.util.Objects;

/**
 * Class representing the instructions page in the game.
 * Manages the display and interactions of the instructions page.
 */
public class InstructionsPage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background5.jpg";

    private final Stage stage;
    private final Controller controller;

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;

    /**
     * Constructor for InstructionsPage.
     *
     * @param stage      the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public InstructionsPage(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();
    }

    /**
     * Displays the instructions page.
     */
    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_NAME)).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // --- Back Button ---
        StackPane backButton = buttonFactory.createCustomButton("Back", "Sugar Bomb", 16, 80, 30, "/com/example/demo/images/ButtonText_Small_Round.png");
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // 30px from top and left

        // Instructions Title
        VBox titleVBox = new VBox();
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30, 0, 0, 0));

        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setFont(fontManager.getFont("Cartoon cookies", 100));
        instructionsTitle.getStyleClass().add("title-text");

        titleVBox.getChildren().add(instructionsTitle);

        // --- Instructions Box with Blur Effect ---
        VBox instructionsBox = new VBox(20);
        instructionsBox.setAlignment(Pos.CENTER);
        instructionsBox.setPadding(new Insets(20));
        instructionsBox.setMaxWidth(650);
        instructionsBox.setPrefHeight(400);
        instructionsBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        // Instructions Text
        VBox textVBox = new VBox();
        textVBox.setAlignment(Pos.TOP_CENTER);
        textVBox.setPadding(new Insets(20, 0, 40, 0));
        Label instructionsText = new Label(
                """
                        Welcome to SKY BATTLE!

                        Use the arrow keys to navigate your spaceship.
                        Press SPACE to shoot enemies.
                        Press ESC to pause your game.

                        Avoid incoming fire and destroy all enemies to win.

                        Good luck and have fun!"""
        );
        instructionsText.setTextFill(Color.BLACK);
        instructionsText.setFont(fontManager.getFont("Pixel Digivolve", 20));
        instructionsText.setWrapText(true);
        textVBox.getChildren().add(instructionsText);

        // Add title and text to the box
        instructionsBox.getChildren().add(textVBox);

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(titleVBox, instructionsBox);

        // --- Main Layout ---
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(mainBox);

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainLayout, backButton);

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
}