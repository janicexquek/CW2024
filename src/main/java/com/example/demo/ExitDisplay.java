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

import java.net.URL;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    public ExitDisplay(double xPosition, double yPosition, Runnable pauseGameCallback, Runnable resumeGameCallback, Runnable backToMainMenuCallback) {
        this.containerXPosition = xPosition;
        this.containerYPosition = yPosition;
        this.backToMainMenuCallback = backToMainMenuCallback;
        this.pauseGameCallback = pauseGameCallback;
        this.resumeGameCallback = resumeGameCallback;
        loadCustomFonts(); // Load all custom fonts
        initializeContainer();
        initializeExit();
    }

    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Pixel Digivolve.otf",
                "/com/example/demo/fonts/SKULL BONES Bold22.otf",
                "/com/example/demo/fonts/Sugar Bomb.ttf" // Newly added
        };

        for (String fontPath : fontPaths) {
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                if (fontStream == null) {
                    System.err.println("Font not found: " + fontPath);
                    continue;
                }
                Font font = Font.loadFont(fontStream, 10);
                if (font == null) {
                    System.err.println("Failed to load font: " + fontPath);
                } else {
                    customFonts.put(font.getName(), font);
                    System.out.println("Loaded font: " + font.getName());
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
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

    private void showExitAlert() {
        // Pause the game
        if (pauseGameCallback != null) {
            pauseGameCallback.run();
        }

        // Create a new stage for the alert
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        // alertStage.setTitle("Exit Game"); // Optional: Set title

        // Create the background image
        ImageView background = new ImageView(new Image(getClass().getResource(BOX_IMAGE_NAME).toExternalForm()));
        background.setFitWidth(ALERT_WIDTH);
        background.setFitHeight(ALERT_HEIGHT);

        // Create the title label
        Label titleLabel = new Label("Exit Game");
        titleLabel.setTextFill(Color.BLACK); // Ensure text is visible against the background
        Font titleFont = customFonts.get("Cartoon cookies");
        if (titleFont != null) {
            titleLabel.setFont(Font.font(titleFont.getName(), 50)); // Larger size for title
        } else {
            System.err.println("Title font not loaded. Using default font.");
            titleLabel.setFont(Font.font("Arial", 50)); // Fallback font
        }
        titleLabel.setAlignment(Pos.TOP_CENTER);
        titleLabel.setWrapText(true);

        // Create the message label
        Label message = new Label("Do you want to CONTINUE your game or \nBACK to Main Menu?");
        message.setTextFill(Color.WHITE); // Assuming the box.png allows white text
        Font messageFont = customFonts.get("Pixel Digivolve");
        if (messageFont != null) {
            message.setFont(Font.font(messageFont.getName(), 20)); // Regular size for message
        } else {
            System.err.println("Message font not loaded. Using default font.");
            message.setFont(Font.font("Arial", 20)); // Fallback font
        }
        message.setWrapText(true);
        message.setAlignment(Pos.CENTER);

        // Create the "Main Menu" button
        Button backButton = createImageButton("Main Menu", BACK_BUTTON_IMAGE_NAME);
        backButton.setPrefWidth(150);
        backButton.setOnAction(event -> {
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

        // Second main box with message and buttons
        VBox secondMainVBox = new VBox(80); // Spacing between message and buttonBox
        secondMainVBox.setAlignment(Pos.CENTER);
        secondMainVBox.setPadding(new Insets(40));
        secondMainVBox.getChildren().addAll(message, buttonBox);

        // Create a VBox for the title and secondMainVBox
        VBox mainVBox = new VBox(20);
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().addAll(titleLabel, secondMainVBox);

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
            // Load the button background image
            Image buttonImage = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView buttonImageView = new ImageView(buttonImage);
            buttonImageView.setFitWidth(180); // Adjust width as needed
            buttonImageView.setPreserveRatio(true);

            // Create a StackPane to overlay text on the image
            StackPane stack = new StackPane();
            stack.getChildren().add(buttonImageView);

            Label label = new Label(text);
            Font buttonFont = customFonts.get("Sugar Bomb"); // Use the exact name of the font
            if (buttonFont != null) {
                label.setFont(Font.font(buttonFont.getName(), 22)); // Adjust size as needed
            } else {
                System.err.println("Button font not loaded. Using default font.");
                label.setFont(Font.font("Arial", 22)); // Fallback font
            }
            label.setTextFill(Color.WHITE); // Ensure text visibility
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
            Font fallbackFont = customFonts.get("Sugar Bomb");
            if (fallbackFont != null) {
                button.setFont(Font.font(fallbackFont.getName(), 14)); // Adjust size as needed
            } else {
                button.setFont(Font.font("Arial", 14)); // Fallback font
            }
            button.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 14px;");
        }

        // Change cursor to hand on hover
        button.setCursor(Cursor.HAND);

        return button;
    }
}
