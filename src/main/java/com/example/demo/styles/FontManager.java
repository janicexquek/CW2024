// File: com/example/demo/style/FontManager.java

package com.example.demo.styles;

import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static FontManager instance;
    private final Map<String, Font> customFonts = new HashMap<>();

    private FontManager() {
        loadCustomFonts();
    }

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    private void loadCustomFonts() {
        String[] fontPaths = {
                "/com/example/demo/fonts/Cartoon cookies.ttf",
                "/com/example/demo/fonts/Sugar Bomb.ttf",
                "/com/example/demo/fonts/Pixel Digivolve.otf"
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
                }
            } catch (Exception e) {
                System.err.println("Error loading font: " + fontPath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a loaded font by its name.
     *
     * @param name the name of the font
     * @param size the desired font size
     * @return the Font object, or a default font if not found
     */
    public Font getFont(String name, double size) {
        Font font = customFonts.get(name);
        if (font != null) {
            return Font.font(font.getFamily(), size);
        } else {
            System.err.println("Font not found: " + name + ". Using default font.");
            return Font.font("Arial", size);
        }
    }
}
