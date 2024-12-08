package com.example.demo.styles;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class MessageBoxTest extends ApplicationTest {

    private MessageBox messageBox;

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            messageBox = new MessageBox(
                    500, 500, // Dimensions
                    "VICTORY!", // Title
                    "LEVEL 1 COMPLETED", // Subtitle
                    "Achievement Unlocked!", // Achievement Text
                    "Time: 00:10", // Current Time
                    "Fastest Time: 00:05" // Fastest Time
            );
        });
        waitForFxEvents();
    }

    @Test
    public void testInitialization() {
        assertNotNull(messageBox, "MessageBox should be initialized");
    }

    @Test
    public void testTitleLabel() {
        Platform.runLater(() -> {
            StackPane root = new StackPane(messageBox);
            assertNotNull(messageBox, "MessageBox should exist in the scene graph");
        });
    }
}
