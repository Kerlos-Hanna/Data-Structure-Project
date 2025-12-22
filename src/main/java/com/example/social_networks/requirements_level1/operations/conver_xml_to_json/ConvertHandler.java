package com.example.social_networks.requirements_level1.operations.conver_xml_to_json;

import javafx.scene.control.TextArea;

public class ConvertHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {
        try {
            String xmlContent = inputArea.getText();

            if (xmlContent == null || xmlContent.isBlank()) {
                outputArea.setText("❌ No XML to convert");
                return;
            }

            Convert_XML_To_JSON converter = new Convert_XML_To_JSON(xmlContent);
            String jsonResult = converter.convert();
            outputArea.setText(jsonResult);

        } catch (Exception ex) {
            outputArea.setText("❌ Conversion failed:\n" + ex.getMessage());
        }
    }
}
