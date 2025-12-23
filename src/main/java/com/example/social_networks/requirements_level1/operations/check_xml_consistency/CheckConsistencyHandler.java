package com.example.social_networks.requirements_level1.operations.check_xml_consistency;

import javafx.scene.control.TextArea;

public class CheckConsistencyHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {

        String xmlInput = inputArea.getText();

        if (xmlInput == null || xmlInput.isBlank()) {
            outputArea.setText("Please enter XML content first.");
            return;
        }

        Check_XML_Consistency checker = new Check_XML_Consistency();

        // 🔧 FIX XML FIRST
        String fixedXML = checker.fixXML(xmlInput);
        inputArea.setText(fixedXML);

        // ✅ THEN CHECK CONSISTENCY
        String result = checker.checkXMLConsistency(fixedXML);
        outputArea.setText(result);
    }
}
