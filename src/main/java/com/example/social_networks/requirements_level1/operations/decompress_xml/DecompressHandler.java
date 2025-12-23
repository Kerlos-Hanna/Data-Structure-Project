package com.example.social_networks.requirements_level1.operations.decompress_xml;

import javafx.scene.control.TextArea;

public class DecompressHandler {

    // Example rule mapping
    // You can expand this if you have more placeholders
    private static final String[] RULES = {"age", "email", "name", "user"};

    public static void handle(TextArea inputArea, TextArea outputArea) {
        String compressedText = inputArea.getText();
        if (compressedText == null || compressedText.isBlank()) {
            outputArea.setText("Input area is empty!");
            return;
        }

        try {
            String decompressed = decompressPlaceholders(compressedText);
            outputArea.setText(decompressed);

        } catch (Exception ex) {
            outputArea.setText("Error during decompression:\n" + ex.getMessage());
        }
    }

    private static String decompressPlaceholders(String text) {
        // Replace placeholders with actual XML tags based on RULES
        for (int i = 0; i < RULES.length; i++) {
            String openTag = "{@" + (i + 1) + "}";
            String closeTag = "{/@" + (i + 1) + "}";
            String realTag = RULES[i];

            text = text.replace(openTag, "<" + realTag + ">");
            text = text.replace(closeTag, "</" + realTag + ">");
        }
        return text;
    }
}
