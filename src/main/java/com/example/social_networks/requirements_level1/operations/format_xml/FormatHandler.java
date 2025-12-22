package com.example.social_networks.requirements_level1.operations.format_xml;

import javafx.scene.control.TextArea;

public class FormatHandler {
    public static void handle(TextArea inputArea, TextArea outputArea) {
        try {
            String xmlContent = inputArea.getText();

            if (xmlContent == null || xmlContent.isBlank()) {
                outputArea.setText("❌ No XML to format");
                return;
            }

            String formatted = Format_XML.formatXML(xmlContent);
            outputArea.setText(formatted);

        } catch (Exception ex) {
            outputArea.setText("❌ Formatting failed:\n" + ex.getMessage());
        }
    }
}
