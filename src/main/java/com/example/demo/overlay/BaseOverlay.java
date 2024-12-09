// File: com/example/demo/overlay/BaseOverlay.java

package com.example.demo.overlay;

import com.example.demo.styles.ButtonFactory;
import com.example.demo.styles.FontManager;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;

/**
 * Abstract base class for overlays.
 * Provides common functionalities shared by all overlays.
 */
public abstract class BaseOverlay extends StackPane {

    protected final FontManager fontManager;
    protected final ButtonFactory buttonFactory;

    /**
     * Flags to prevent duplicate button initialization
     */
    protected boolean buttonsInitialized = false;

    /**
     * Constructor for BaseOverlay.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    public BaseOverlay(double screenWidth, double screenHeight) {
        this.fontManager = FontManager.getInstance();
        this.buttonFactory = new ButtonFactory();

        // Set the size of the overlay to cover the entire screen
        setPrefSize(screenWidth, screenHeight);
        setMaxSize(screenWidth, screenHeight);
        setMinSize(screenWidth, screenHeight);

        // Make the overlay non-focusable and ignore mouse events when not visible
        setFocusTraversable(false);
        setMouseTransparent(true);

        // Semi-transparent background
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // 50% opacity

        // Link the CSS stylesheet
        linkStylesheet();

        // Add a key event handler to consume ESC key when overlay is active
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });

        // Create and add the message box
        StackPane messageBox = createMessageBox();
        getChildren().add(messageBox);

        // Initially, the overlay is not visible
        setVisible(false);
    }

    /**
     * Links the CSS stylesheet to this overlay.
     */
    private void linkStylesheet() {
        URL cssResource = getClass().getResource("/com/example/demo/styles/styles.css");
        if (cssResource == null) {
            System.err.println("CSS file not found!");
        } else {
            getStylesheets().add(cssResource.toExternalForm());
        }
    }

    /**
     * Creates the message box. To be implemented by subclasses.
     *
     * @return the created StackPane representing the message box
     */
    protected abstract StackPane createMessageBox();
}
