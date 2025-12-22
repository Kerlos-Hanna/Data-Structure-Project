package com.example.social_networks.requirements_level1.operations.minify_xml;

import javafx.scene.control.TextArea;

public class MinifyHandler {

    /**
     * Handles Minify button click
     */
    public static void handle(TextArea inputArea, TextArea outputArea) {
        try {
            String xmlContent = inputArea.getText();

            if (xmlContent == null || xmlContent.isBlank()) {
                outputArea.setText("❌ No XML to minify");
                return;
            }

            String minified = Minify_XML_Size.minify(xmlContent);
            outputArea.setText(minified);

        } catch (Exception ex) {
            outputArea.setText("❌ Minification failed:\n" + ex.getMessage());
        }
    }
}
