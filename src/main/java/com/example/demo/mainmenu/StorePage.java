package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import com.example.demo.mainmenumanager.PlaneOption;
import com.example.demo.mainmenumanager.StoreManager;
import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * Class representing the store page in the game.
 * Manages the display and interactions of the store page.
 */
public class StorePage {
    private static final String BACKGROUND_IMAGE_NAME = "/com/example/demo/images/background4.jpeg"; // Background image for store

    private final Stage stage;
    private final Controller controller;

    private final FontManager fontManager;
    private final ButtonFactory buttonFactory;
    private Label selectionMessageLabel;

    private final List<PlaneOption> planeOptions = new ArrayList<>();

    /**
     * Constructor for StorePage.
     *
     * @param stage      the primary stage for this application
     * @param controller the controller to manage interactions
     */
    public StorePage(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();
    }

    /**
     * Displays the store page.
     */
    public void show() {
        // Load the background image
        ImageView backgroundImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_NAME)).toExternalForm()));
        backgroundImageView.setFitWidth(stage.getWidth());
        backgroundImageView.setFitHeight(stage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // --- Back Button ---
        StackPane backButton = createBackButton();

        // --- Store Title and Selection Message ---
        VBox titleVBox = createTitleSection();

        // --- Planes Display Area ---
        ScrollPane scrollPane = createPlanesScrollPane();

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

    /**
     * Creates the back button.
     *
     * @return Configured StackPane representing the back button.
     */
    private StackPane createBackButton() {
        StackPane backButton = buttonFactory.createCustomButton("Back", "Sugar Bomb", 16, 80, 30, "/com/example/demo/images/ButtonText_Small_Round.png");
        backButton.setOnMouseClicked(e -> {
            // Navigate back to the main menu
            MainMenu mainMenu = new MainMenu(stage, controller);
            mainMenu.show();
        });

        // Position the back button at top-left
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        StackPane.setMargin(backButton, new Insets(30, 0, 0, 30)); // 30px from top and left

        return backButton;
    }

    /**
     * Creates the title section containing the store title and selection message.
     *
     * @return Configured VBox containing titles.
     */
    private VBox createTitleSection() {
        VBox titleVBox = new VBox(10);
        titleVBox.setAlignment(Pos.TOP_CENTER);
        titleVBox.setPadding(new Insets(30, 0, 0, 0));

        Label storeTitle = new Label("Welcome to the Plane Store");
        storeTitle.setFont(fontManager.getFont("Cartoon cookies", 60));
        storeTitle.getStyleClass().add("title-text");

        Label storeText = new Label("Please choose your own plane");
        storeText.setFont(fontManager.getFont("Cartoon cookies", 40));
        storeText.getStyleClass().add("title-text");

        // --- Selection Message Label ---
        selectionMessageLabel = new Label();
        selectionMessageLabel.setFont(fontManager.getFont("Pixel Digivolve", 25));
        selectionMessageLabel.getStyleClass().add("title-text");

        // Initialize the selection message based on current selection
        int selectedPlaneNumber = StoreManager.getInstance().getSelectedPlaneNumber();
        updateSelectionMessage(selectedPlaneNumber);

        titleVBox.getChildren().addAll(storeTitle, storeText, selectionMessageLabel);

        return titleVBox;
    }

    /**
     * Creates the scrollable area containing plane options.
     *
     * @return Configured ScrollPane containing plane options.
     */
    private ScrollPane createPlanesScrollPane() {
        TilePane planesBox = new TilePane();
        planesBox.setAlignment(Pos.CENTER);
        planesBox.setPadding(new Insets(20));
        planesBox.setHgap(120); // Horizontal gap between tiles
        planesBox.setVgap(20); // Vertical gap between tiles
        planesBox.setPrefColumns(2); // Fixed number of columns to 2
        planesBox.setMaxWidth(600); // Adjust as needed

        // Load plane images into planesBox
        String[] planeImages = { "userplane.png", "userplane1.png", "userplane2.png", "userplane3.png", "userplane4.png",
                "userplane5.png", "userplane6.png" }; // Add more plane images as needed
        for (String planeImage : planeImages) {
            PlaneOption planeOption = new PlaneOption(planeImage, this::handlePlaneSelection);
            planeOptions.add(planeOption);
            planesBox.getChildren().add(planeOption);
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
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Set preferred size
        scrollPane.setPrefWidth(600); // Adjust as needed
        scrollPane.setPrefHeight(400); // Adjust as needed

        // Bind ScrollPane size to 60% of window width and 50% of window height for responsiveness
        scrollPane.prefWidthProperty().bind(stage.widthProperty().multiply(0.6));
        scrollPane.prefHeightProperty().bind(stage.heightProperty().multiply(0.5));

        // Add a style class to the ScrollPane for CSS styling
        scrollPane.getStyleClass().add("transparent-scroll-pane");

        return scrollPane;
    }

    /**
     * Handles the selection of a plane.
     *
     * @param selectedPlaneNumber The number of the selected plane.
     */
    private void handlePlaneSelection(int selectedPlaneNumber) {
        // Deselect all other planes
        for (PlaneOption planeOption : planeOptions) {
            if (planeOption.getPlaneNumber() != selectedPlaneNumber) {
                planeOption.deselect();
            } else {
                planeOption.select();
            }
        }

        // Update the selection message
        updateSelectionMessage(selectedPlaneNumber);

        // Save the selected plane number in StoreManager
        StoreManager.getInstance().setSelectedPlaneNumber(selectedPlaneNumber);
    }

    /**
     * Updates the selection message label based on the selected plane number.
     *
     * @param planeNumber the selected plane number
     */
    private void updateSelectionMessage(int planeNumber) {
        selectionMessageLabel.setText("You have selected plane " + planeNumber);
    }
}
