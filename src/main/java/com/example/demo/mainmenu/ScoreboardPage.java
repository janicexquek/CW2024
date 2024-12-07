package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import com.example.demo.mainmenumanager.FastestTimesManager;
import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import com.example.demo.styles.TimeFormatter; // Import TimeFormatter
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
import java.util.*;

/**
 * Class representing the scoreboard page in the game.
 * Manages the display and interactions of the scoreboard page.
 */
public class ScoreboardPage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background6.jpeg";

    private static final List<String> ORDERED_LEVELS = Arrays.asList(
            "LEVEL ONE",
            "LEVEL TWO",
            "LEVEL THREE",
            "LEVEL FOUR"
            // Add more levels here in the desired order
    );

    private final Stage stage;
    private final Controller controller;

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;

    /**
     * Constructor for ScoreboardPage.
     *
     * @param stage      the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public ScoreboardPage(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();
    }

    /**
     * Displays the scoreboard page.
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

        // Scoreboard Title
        VBox titleVBox = new VBox();
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30, 0, 0, 0));

        Label scoreboardTitle = new Label("Scoreboard");
        scoreboardTitle.setFont(fontManager.getFont("Cartoon cookies", 100));
        scoreboardTitle.getStyleClass().add("title-text");
        titleVBox.getChildren().add(scoreboardTitle);

        // --- Headers for the Table ---
        HBox headers = new HBox();
        headers.setSpacing(200);
        headers.setAlignment(Pos.CENTER);
        headers.setPadding(new Insets(10, 0, 10, 0));

        Label levelHeader = new Label("Level");
        levelHeader.setFont(fontManager.getFont("Pixel Digivolve", 24));
        levelHeader.setTextFill(Color.BLACK);

        Label timeHeader = new Label("Fastest Time (s)");
        timeHeader.setFont(fontManager.getFont("Pixel Digivolve", 24));
        timeHeader.setTextFill(Color.BLACK);

        headers.getChildren().addAll(levelHeader, timeHeader);

        // --- Separator ---
        Label separator = new Label("--------------------------------------------------");
        separator.setTextFill(Color.BLACK);

        // --- Scores Box with Styling ---
        VBox scoresBox = new VBox(10);
        scoresBox.setAlignment(Pos.CENTER);
        scoresBox.setPadding(new Insets(20));
        scoresBox.setMaxWidth(650);
        scoresBox.setPrefHeight(400);
        scoresBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 10;");

        // Add headers and separator
        scoresBox.getChildren().addAll(headers, separator);

        // Fetch the fastest times from the controller
        FastestTimesManager ftm = FastestTimesManager.getInstance();
        Map<String, Long> fastestTimes = ftm.getAllFastestTimes();

        // Dynamically create rows for each level in the predefined order
        for (String levelName : ORDERED_LEVELS) {
            HBox row = new HBox();
            row.setSpacing(200);
            row.setAlignment(Pos.TOP_CENTER);
            row.setPadding(new Insets(5, 0, 5, 0));

            Label levelLabel = new Label(levelName);
            levelLabel.setFont(fontManager.getFont("Pixel Digivolve", 20));
            levelLabel.setTextFill(Color.BLACK);

            long timeValue = fastestTimes.getOrDefault(levelName, Long.MAX_VALUE);
            String timeStr = timeValue < Long.MAX_VALUE ? TimeFormatter.formatTime(timeValue) : "N/A";
            Label timeLabel = new Label(timeStr);
            timeLabel.setFont(fontManager.getFont("Pixel Digivolve", 20));
            timeLabel.setTextFill(Color.BLACK);

            row.getChildren().addAll(levelLabel, timeLabel);
            scoresBox.getChildren().add(row);
        }

        VBox mainBox = new VBox(20);
        mainBox.setAlignment(Pos.TOP_CENTER);
        mainBox.getChildren().addAll(titleVBox, scoresBox);

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
