package com.example.demo.display;

import static org.mockito.Mockito.*;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.testfx.api.FxRobot;

@ExtendWith(ApplicationExtension.class)
public class DisplayManagerTest {

    private Group root;
    private Runnable pauseGameCallback;
    private Runnable showExitOverlayCallback;
    private DisplayManager displayManager;

    @Start
    public void start(Stage stage) {
        root = new Group();
        pauseGameCallback = mock(Runnable.class);
        showExitOverlayCallback = mock(Runnable.class);

        displayManager = new DisplayManager(root, 5, 1280, 720, pauseGameCallback, showExitOverlayCallback);

        Scene scene = new Scene(root, 1280, 720);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testInitialSetup() {
        Platform.runLater(() -> {
            // Verify that the root contains all necessary components
            assert root.getChildren().contains(displayManager.heartDisplay.getContainer());
            assert root.getChildren().contains(displayManager.exitDisplay.getContainer());
            assert root.getChildren().stream().anyMatch(node -> node instanceof Text);
        });
    }

    @Test
    public void testUpdateKillCount(FxRobot robot) {
        Platform.runLater(() -> {
            // Update kill count and verify text
            displayManager.updateKillCount(10, 20);
            Text infoDisplay = (Text) root.getChildren().stream()
                    .filter(node -> node instanceof Text)
                    .findFirst()
                    .orElse(null);
            assert infoDisplay != null;
            assert infoDisplay.getText().equals("Kills: 10 / 20");
        });
    }

    @Test
    public void testUpdateBossHealth(FxRobot robot) {
        Platform.runLater(() -> {
            // Update boss health and verify text
            displayManager.updateBossHealth(50);
            Text infoDisplay = (Text) root.getChildren().stream()
                    .filter(node -> node instanceof Text)
                    .findFirst()
                    .orElse(null);
            assert infoDisplay != null;
            assert infoDisplay.getText().equals("Boss Health: 50");
        });
    }

    @Test
    public void testRemoveHearts(FxRobot robot) {
        Platform.runLater(() -> {
            // Remove hearts and verify
            displayManager.removeHearts(3);
            assert displayManager.heartDisplay.getContainer().getChildren().size() == 3;
        });
    }

    @Test
    public void testShowHeartDisplay(FxRobot robot) {
        Platform.runLater(() -> {
            // Remove HeartDisplay and ensure it is shown again
            root.getChildren().remove(displayManager.heartDisplay.getContainer());
            displayManager.showHeartDisplay();
            assert root.getChildren().contains(displayManager.heartDisplay.getContainer());
        });
    }

    @Test
    public void testShowExitDisplay(FxRobot robot) {
        Platform.runLater(() -> {
            // Remove ExitDisplay and ensure it is shown again
            root.getChildren().remove(displayManager.exitDisplay.getContainer());
            displayManager.showExitDisplay();
            assert root.getChildren().contains(displayManager.exitDisplay.getContainer());
        });
    }

    @Test
    public void testExitDisplayCallbacks(FxRobot robot) {
        Platform.runLater(() -> {
            // Simulate click on ExitDisplay's button
            displayManager.exitDisplay.getContainer().getChildren().get(0).fireEvent(
                    new javafx.scene.input.MouseEvent(
                            javafx.scene.input.MouseEvent.MOUSE_CLICKED,
                            0, 0, 0, 0,
                            javafx.scene.input.MouseButton.PRIMARY,
                            1, true, true, true, true,
                            true, true, true, true,
                            true, true, null
                    )
            );

            // Verify callbacks
            verify(pauseGameCallback).run();
            verify(showExitOverlayCallback).run();
        });
    }
}
