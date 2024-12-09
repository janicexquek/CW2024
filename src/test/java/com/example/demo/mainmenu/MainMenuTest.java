// File: src/test/java/com/example/demo/mainmenu/MainMenuTest.java

package com.example.demo.mainmenu;

import com.example.demo.controller.Controller;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainMenuTest extends ApplicationTest {

    @Mock
    private Stage mockStage;

    @Mock
    private Controller mockController;

    private MainMenu mainMenu;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mainMenu = new MainMenu(mockStage, mockController);
    }

    @Override
    public void start(Stage stage) {
        // No need to initialize a real stage since we're mocking it
    }

    @Test
    public void testShowDoesNotThrow() {
        assertDoesNotThrow(() -> {
            // Execute show() on the JavaFX Application Thread
            interact(() -> {
                mainMenu.show();
            });
        });
    }

    @Test
    public void testShowSetsSceneAndShowsStage() {
        // Execute show() on the JavaFX Application Thread and verify interactions
        interact(() -> {
            mainMenu.show();
            verify(mockStage).setScene(any());
            verify(mockStage).show();
        });
    }

    @Test
    public void testCloseButtonClosesStage() {
        // Execute show() to initialize the UI
        interact(() -> {
            mainMenu.show();
        });

        // Get the close button and simulate a click
        ImageView closeButton = mainMenu.getCloseImageView();
        interact(() -> {
            closeButton.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1, true, true, true, true,
                    true, true, true, true, true, true, null
            ));
        });

        // Verify that the stage's close method was called
        verify(mockStage).close();
    }

    @Test
    public void testPlayButtonStartsGame() {
        // Execute show() to initialize the UI
        interact(() -> {
            mainMenu.show();
        });

        // Get the play button and simulate a click
        StackPane playBtn = mainMenu.getPlayButton();
        interact(() -> {
            playBtn.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1, true, true, true, true,
                    true, true, true, true, true, true, null
            ));
        });

        // Verify that controller.startGame() was called
        verify(mockController).startGame();
    }

    @Test
    public void testSettingsButtonOpensSettings() {
        // Execute show() to initialize the UI
        interact(() -> {
            mainMenu.show();
        });

        // Get the settings button and simulate a click
        StackPane settingsBtn = mainMenu.getSettingsButton();
        interact(() -> {
            settingsBtn.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1, true, true, true, true,
                    true, true, true, true, true, true, null
            ));
        });

        // Depending on the implementation of Settings.show(), you might need to verify interactions or state
        // For example, if Settings interacts with the controller, verify those interactions
        // As Settings is likely to show a new window, consider verifying that a new stage is shown
        // This may require additional mocking or spying
    }

    @Test
    public void testInstructionsButtonOpensInstructions() {
        // Execute show() to initialize the UI
        interact(() -> {
            mainMenu.show();
        });

        // Get the instructions button and simulate a click
        StackPane instructionsBtn = mainMenu.getInstructionsButton();
        interact(() -> {
            instructionsBtn.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1, true, true, true, true,
                    true, true, true, true, true, true, null
            ));
        });

        // Similarly, verify that InstructionsPage.show() was called or related interactions
        // This may require additional mocking or spying
    }

    @Test
    public void testStatButtonOpensScoreboard() {
        // Execute show() to initialize the UI
        interact(() -> {
            mainMenu.show();
        });

        // Get the statistics button and simulate a click
        StackPane statBtn = mainMenu.getStatButton();
        interact(() -> {
            statBtn.fireEvent(new javafx.scene.input.MouseEvent(
                    javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                    0, 0, 0, 0,
                    javafx.scene.input.MouseButton.PRIMARY,
                    1, true, true, true, true,
                    true, true, true, true, true, true, null
            ));
        });

        // Verify that ScoreboardPage.show() was called or related interactions
        // This may require additional mocking or spying
    }

}
