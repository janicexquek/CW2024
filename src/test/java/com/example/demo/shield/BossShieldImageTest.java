package com.example.demo.shield;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BossShieldImageTest {

    @BeforeAll
    public static void initToolkit() {
        Platform.startup(() -> {
            // Initialize JavaFX toolkit
        });
    }

    private BossShieldImage bossShieldImage;

    @BeforeEach
    public void setUp() {
        // Initialize the BossShieldImage for testing
        bossShieldImage = new BossShieldImage(100.0, 200.0);
    }

    @Test
    public void testConstructor() {
        // Verify initial X and Y positions
        assertEquals(100.0, bossShieldImage.getLayoutX(), "Initial X position should be 100.0");
        assertEquals(200.0, bossShieldImage.getLayoutY(), "Initial Y position should be 200.0");

        // Verify shield dimensions
        assertEquals(200, bossShieldImage.getFitHeight(), "Shield height should be 200");
        assertEquals(200, bossShieldImage.getFitWidth(), "Shield width should be 200");

        // Verify initial visibility
        assertFalse(bossShieldImage.isVisible(), "Shield should be initially invisible");

        // Verify image is loaded
        assertNotNull(bossShieldImage.getImage(), "Shield image should be loaded");
    }

    @Test
    public void testShowShield() {
        // Show the shield and verify visibility
        bossShieldImage.showShield();
        assertTrue(bossShieldImage.isVisible(), "Shield should be visible after calling showShield()");
    }

    @Test
    public void testHideShield() {
        // Hide the shield and verify visibility
        bossShieldImage.showShield(); // Ensure shield is visible first
        bossShieldImage.hideShield();
        assertFalse(bossShieldImage.isVisible(), "Shield should be invisible after calling hideShield()");
    }
}
