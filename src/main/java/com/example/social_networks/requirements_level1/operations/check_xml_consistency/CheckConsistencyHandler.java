package com.example.social_networks.requirements_level1.operations.check_xml_consistency;

import javafx.scene.control.TextArea;
import java.util.List;

public class CheckConsistencyHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {

        String xmlInput = inputArea.getText();

        if (xmlInput == null || xmlInput.isBlank()) {
            outputArea.setText("Please enter XML content first.");
            return;
        }

        Check_XML_Consistency checker = new Check_XML_Consistency();

        // ✅ CHECK ONLY (NO AUTO FIX)
        List<Check_XML_Consistency.checkXMLConsistency> errors =
                checker.validate(xmlInput);

        if (errors.isEmpty()) {
            outputArea.setText("XML is consistent ✔");
        } else {
            StringBuilder result = new StringBuilder();
            result.append("❌ XML Consistency Errors:\n\n");
            for (Check_XML_Consistency.checkXMLConsistency err : errors) {
                result.append(err).append("\n");
            }
            outputArea.setText(result.toString());
        }
    }
}
