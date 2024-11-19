package com.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class ExitDisplay {

    private static final String EXIT_IMAGE_NAME = "/com/example/demo/images/exit.png";
    private static final String BOX_IMAGE_NAME = "/com/example/demo/images/box1.png"; // Updated if renamed
    private static final String CONTINUE_BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png"; // Continue button image
    private static final String BACK_BUTTON_IMAGE_NAME = "/com/example/demo/images/ButtonText_Small_Blue_Round.png"; // Back to Main Menu button image

    private static final int EXIT_HEIGHT = 50;
    private static final double ALERT_WIDTH = 500;
    private static final double ALERT_HEIGHT = 500;
    private HBox container;
    private double containerXPosition;
    private double containerYPosition;

    // Callbacks to handle game state
    private Runnable pauseGameCallback;
    private Runnable resumeGameCallback;
    private Runnable backToMainMenuCallback;

    public ExitDisplay(double xPosition, double yPosition, Runnable pauseGameCallback, Runnable resumeGameCallback, Runnable backToMainMenuCallback) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.pauseGameCallback = pauseGameCallback;
        this.resumeGameCallback = resumeGameCallback;
        initializeContainer();
        initializeExit();
    }

    private void initializeContainer() {
        container = new HBox();
        container.setLayoutX(containerXPosition);
        container.setLayoutY(containerYPosition);
    }

    private void initializeExit() {
        ImageView exit = new ImageView(new Image(getClass().getResource(EXIT_IMAGE_NAME).toExternalForm()));
        exit.setFitHeight(EXIT_HEIGHT);
        exit.setPreserveRatio(true);
        container.getChildren().add(exit);
        // Add click event handler
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> showExitAlert());
        // Change cursor to hand on hover
        exit.setCursor(Cursor.HAND);
    }

    public HBox getContainer() {
        return container;
    }

    private Font loadCustomFont(double size) {
        try {
            return Font.loadFont(getClass().getResourceAsStream("/com/example/demo/fonts/Cartoon cookies.ttf"), size);
        } catch (Exception e) {
            System.err.println("Failed to load custom font.");
            e.printStackTrace();
            // Fallback to a default font
            return Font.font("Arial", size);
        }
    }

    private void showExitAlert() {
        // Pause the game
        if (pauseGameCallback != null) {
            pauseGameCallback.run();
        }

        // Create a new stage for the alert
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
//        alertStage.setTitle("Exit Game");

        // Create the background image
        ImageView background = new ImageView(new Image(getClass().getResource(BOX_IMAGE_NAME).toExternalForm()));
        background.setFitWidth(ALERT_WIDTH);
        background.setFitHeight(ALERT_HEIGHT);

        // Load custom fonts
        Font titleFont = loadCustomFont(50); // Larger size for title
        Font messageFont = loadCustomFont(20); // Regular size for message

        // Create the title label
        Label titleLabel = new Label("Exit Game");
        titleLabel.setTextFill(Color.BLACK); // Ensure text is visible against the background
        titleLabel.setFont(titleFont);
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setWrapText(true);

        // Create the message label
        Label message = new Label("Do you want to CONTINUE your game or \n BACK to Main Menu?");
        message.setTextFill(Color.WHITE); // Assuming the box.png allows white text
        message.setFont(messageFont);
        message.setWrapText(true);
        message.setAlignment(Pos.CENTER);

        // Create the "Back to Main Menu" button
        Button backButton = createImageButton("Back to Main Menu", BACK_BUTTON_IMAGE_NAME);
        backButton.setPrefWidth(150);
        backButton.setOnAction(event -> {
            // Implement the action to return to the main menu
            // For example, you might want to close the current level and open the main menu
            // Here, we'll just exit the application for demonstration
//            alertStage.close();
//            backToMainMenuCallback.run(); // Execute the callback to navigate to the main menu
            alertStage.close();
            if (backToMainMenuCallback != null) {
                backToMainMenuCallback.run();
            }

        });

        // Create the "Continue" button
        Button continueButton = createImageButton("Continue", CONTINUE_BUTTON_IMAGE_NAME);
        continueButton.setPrefWidth(150);
        continueButton.setOnAction(event -> {
            alertStage.close();
            if (resumeGameCallback != null) {
                resumeGameCallback.run();
            }
        });

        // Arrange buttons in an HBox
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(continueButton, backButton);

        //Second main box
        VBox secondmainVBox = new VBox(80);
        secondmainVBox.setAlignment(Pos.CENTER);
        secondmainVBox.setPadding(new Insets(40));
        secondmainVBox.getChildren().addAll(message, buttonBox);

        // Create a VBox for the message and buttons
        VBox mainVBox = new VBox(20);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(titleLabel, secondmainVBox);

        // Create a StackPane to hold the background and VBox
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, mainVBox);

        // Create the scene and set it on the alert stage
        Scene scene = new Scene(stackPane, ALERT_WIDTH, ALERT_HEIGHT);
        alertStage.setScene(scene);
        alertStage.setResizable(false);
        alertStage.showAndWait();
    }

    private Button createImageButton(String text, String imagePath) {
        Button button = new Button();

        try {
            // Load the button image
            Image buttonImage = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView buttonImageView = new ImageView(buttonImage);
            buttonImageView.setFitWidth(180); // Adjust width as needed
            buttonImageView.setPreserveRatio(true);

            // Create a StackPane to overlay text on the image
            StackPane stack = new StackPane();
            stack.getChildren().add(buttonImageView);

            Label label = new Label(text);
            label.setTextFill(Color.WHITE);
            label.setFont(loadCustomFont(20)); // Adjust font size as needed
            stack.getChildren().add(label);

            // Set the StackPane as the button's graphic
            button.setGraphic(stack);

            // Remove the default background and border
            button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        } catch (Exception e) {
            System.err.println("Failed to load button image: " + imagePath);
            e.printStackTrace();
            // Fallback: Style the button without an image
            button.setText(text);
            button.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 14px;");
        }

        // Change cursor to hand on hover
        button.setCursor(Cursor.HAND);

        return button;
    }

}
