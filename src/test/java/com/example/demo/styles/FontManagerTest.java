package com.example.demo.styles;

import javafx.scene.text.Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class FontManagerTest {

    private FontManager fontManager;

    @BeforeEach
    public void setUp() {
        fontManager = FontManager.getInstance(); // Get the singleton instance
    }

    @Test
    public void testSingletonInstance() {
        // Ensure the singleton instance is always the same
        FontManager anotherInstance = FontManager.getInstance();
        assertSame(fontManager, anotherInstance, "FontManager should be a singleton");
    }

    @Test
    public void testCustomFontLoading() {
        // Verify that specific fonts are loaded successfully
        Font cartoonCookiesFont = fontManager.getFont("Cartoon cookies", 12);
        assertNotNull(cartoonCookiesFont, "Cartoon cookies font should be loaded");

        Font sugarBombFont = fontManager.getFont("Sugar Bomb", 12);
        assertNotNull(sugarBombFont, "Sugar Bomb font should be loaded");

        Font pixelDigivolveFont = fontManager.getFont("Pixel Digivolve", 12);
        assertNotNull(pixelDigivolveFont, "Pixel Digivolve font should be loaded");
    }

    @Test
    public void testGetFontReturnsDefaultForUnknownFont() {
        // Request a font that doesn't exist
        Font unknownFont = fontManager.getFont("Unknown Font", 12);
        assertNotNull(unknownFont, "FontManager should return a default font for unknown fonts");
        assertEquals("Arial", unknownFont.getFamily(), "Default font family should be Arial");
    }

    @Test
    public void testFontLoadingFromResources() {
        // Ensure all fonts can be accessed directly from resources
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Sugar Bomb.ttf",
                "/com/example/demo/fonts/Pixel Digivolve.otf"
        };

        for (String fontPath : fontPaths) {
            try (InputStream fontStream = getClass().getResourceAsStream(fontPath)) {
                assertNotNull(fontStream, "Font file should exist: " + fontPath);
                Font loadedFont = Font.loadFont(fontStream, 12);
                assertNotNull(loadedFont, "Font should be loaded from: " + fontPath);
            } catch (Exception e) {
                fail("Error loading font from path: " + fontPath);
            }
        }
    }
}
