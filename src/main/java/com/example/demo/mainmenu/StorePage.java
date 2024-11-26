package com.example.demo.mainmenu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.example.demo.controller.Controller;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StorePage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpeg"; // Background image for store

    private final Stage stage;
    private final Controller controller;

    // Map to store loaded fonts for easy access
    private Map<String, Font> customFonts = new HashMap<>();

    public StorePage(Stage stage, Controller controller) {
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

        // --- Back Button ---
        StackPane backButton = createCustomButton("Back",
                "/com/example/demo/images/ButtonText_Small_Round.png", 80, 30);
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // Adjust as needed

        // --- Store Title ---
        VBox titleVBox = new VBox(10);
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30, 0, 0, 0));

        Label storeTitle = new Label("Welcome to the Plane Store");
        storeTitle.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 60));
        storeTitle.getStyleClass().add("title-text");

        Label storeText = new Label("Please choose your own plane");
        storeText.setFont(Font.font(customFonts.getOrDefault("Cartoon cookies",
                Font.font("Arial", 100)).getName(), 40));
        storeText.getStyleClass().add("title-text");

        titleVBox.getChildren().addAll(storeTitle, storeText);

        // --- Planes Display Area ---
        TilePane planesBox = new TilePane();
        planesBox.setAlignment(Pos.CENTER);
        planesBox.setPadding(new Insets(20));
        planesBox.setHgap(20); // Horizontal gap between tiles
        planesBox.setVgap(20); // Vertical gap between tiles
        planesBox.setPrefColumns(2); // Fixed number of columns to 2
        planesBox.setMaxWidth(600); // Adjust as needed

        // Load plane images into planesBox
        String[] planeImages = { "userplane.png", "userplane1.png", "userplane2.png", "userplane3.png", "userplane4.png",
                "userplane5.png", "userplane6.png" }; // Add more plane images as needed
        for (String planeImage : planeImages) {
            StackPane planePane = createPlaneOption(planeImage);
            planesBox.getChildren().add(planePane);
        }

        // --- ScrollPane for Planes ---
        ScrollPane scrollPane = new ScrollPane();
        VBox contentBox = new VBox(planesBox);
        contentBox.setAlignment(Pos.CENTER);
        scrollPane.setContent(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Set preferred size
        scrollPane.setPrefWidth(600); // Adjust as needed
        scrollPane.setPrefHeight(400); // Adjust as needed

        // Bind ScrollPane size to 60% of window width and 50% of window height for responsiveness
        scrollPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        scrollPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.5));

        // Add a style class to the ScrollPane for CSS styling
        scrollPane.getStyleClass().add("transparent-scroll-pane");

        // --- Main Layout ---
        VBox mainBox = new VBox(40);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(titleVBox, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Create a StackPane to layer the background and main layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, mainBox, backButton);
        StackPane.setAlignment(mainBox, Pos.CENTER);
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);

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

     // Creates a plane option with hover and click effects.
    private StackPane createPlaneOption(String planeImageName) {
        // Load the plane image
        ImageView planeImageView = new ImageView(new Image(getClass().getResource("/com/example/demo/images/" + planeImageName).toExternalForm()));
        planeImageView.setFitWidth(150); // Adjust width as needed
        planeImageView.setFitHeight(150); // Adjust height as needed
        planeImageView.setPreserveRatio(true);
        planeImageView.setSmooth(true);

        // Create a semi-transparent grey overlay for hover effect
        Rectangle hoverOverlay = new Rectangle(190, 190);
        hoverOverlay.setFill(Color.web("#5b6980", 0.4)); // Grey with 40% opacity
        hoverOverlay.setVisible(false); // Initially invisible

        // Create a dark grey border for selection
        Rectangle selectionBorder = new Rectangle(190, 190);
        selectionBorder.setFill(null); // No fill
        selectionBorder.setStroke(Color.web("#5b6980", 0.4)); // Semi-transparent dark grey stroke
        selectionBorder.setFill(Color.web("#5b6980", 0.4)); // Grey with 40% opacity
        selectionBorder.setStrokeWidth(3);
        selectionBorder.setVisible(false); // Initially not selected

        // Create a StackPane to layer the plane image, hover overlay, and selection border
        StackPane stackPane = new StackPane(planeImageView, hoverOverlay, selectionBorder);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setCursor(Cursor.HAND);

        // Bind plane image size to window size for responsiveness
        planeImageView.fitWidthProperty().bind(stage.widthProperty().multiply(0.15)); // 15% of window width
        planeImageView.fitHeightProperty().bind(stage.heightProperty().multiply(0.15)); // 15% of window height
        // Change cursor to hand on hover
        stackPane.setCursor(Cursor.HAND);

        // Create a single ScaleTransition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);
        // Handle hover effects
        stackPane.setOnMouseEntered(e -> {
            hoverOverlay.setVisible(true);
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        stackPane.setOnMouseExited(e -> {
            hoverOverlay.setVisible(false);
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });

        // Handle selection on click
        stackPane.setOnMouseClicked(e -> {
            // Deselect all other planes by hiding their selection borders
            TilePane parent = (TilePane) stackPane.getParent(); // Parent is TilePane
            for (javafx.scene.Node node : parent.getChildren()) {
                if (node instanceof StackPane) {
                    StackPane otherPane = (StackPane) node;
                    for (javafx.scene.Node child : otherPane.getChildren()) {
                        if (child instanceof Rectangle) {
                            Rectangle rect = (Rectangle) child;
                            // Hide selection border if it's not the current one
                            if (rect != selectionBorder) {
                                rect.setVisible(false);
                            }
                        }
                    }
                }
            }

            // Select this plane by showing its selection border
            selectionBorder.setVisible(true);

            // Save the selected plane in StoreManager
            StoreManager.getInstance().setSelectedPlane(planeImageName);
            System.out.println("Selected Plane: " + planeImageName);

            // Optionally, add a scale transition for visual feedback
            ScaleTransition selectScale = new ScaleTransition(Duration.millis(200), stackPane);
            selectScale.setToX(1.1);
            selectScale.setToY(1.1);
            selectScale.play();

            // Reset scale after the transition
            selectScale.setOnFinished(event -> {
                stackPane.setScaleX(1.0);
                stackPane.setScaleY(1.0);
            });
        });

        return stackPane;
    }


     // Creates a custom button with hover effects.
    private StackPane createCustomButton(String text, String imagePath, double width, double height) {
        // Load the button background image
        ImageView buttonImageView = new ImageView(new Image(getClass().getResource(imagePath).toExternalForm()));
        buttonImageView.setFitWidth(width);
        buttonImageView.setFitHeight(height);
        buttonImageView.setPreserveRatio(false);

        // Create a label for the button text
        Label label = new Label(text);
        label.setFont(Font.font(customFonts.getOrDefault("Sugar Bomb",
                Font.font("Arial", 16)).getName(), 16));

        label.getStyleClass().add("button-label");

        // Create a StackPane to stack the image and the label
        StackPane stackPane = new StackPane(buttonImageView, label);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getStyleClass().add("custom-button-hover");

        // Ensure the StackPane size matches the image size
        stackPane.setMinSize(width, height);
        stackPane.setMaxSize(width, height);

        // Change cursor to hand on hover
        stackPane.setCursor(Cursor.HAND);

        // Create a single ScaleTransition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), stackPane);

        // Add hover effects
        stackPane.setOnMouseEntered(ev -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.playFromStart();
        });

        stackPane.setOnMouseExited(ev -> {
            scaleTransition.stop();
            scaleTransition.setFromX(stackPane.getScaleX());
            scaleTransition.setFromY(stackPane.getScaleY());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.playFromStart();
        });

        return stackPane;
    }


     // Loads custom fonts from the resources.
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
