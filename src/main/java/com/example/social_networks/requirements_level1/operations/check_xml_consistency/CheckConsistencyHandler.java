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

        // 🔧 STEP 1: FIX XML
        String fixedXML = checker.autoFix(xmlInput);
        inputArea.setText(fixedXML);

        // ✅ STEP 2: CHECK CONSISTENCY USING validate()
        List<Check_XML_Consistency.checkXMLConsistency> errors =
                checker.validate(fixedXML);

        if (errors.isEmpty()) {
            outputArea.setText("XML is consistent ✔");
        } else {
            StringBuilder result = new StringBuilder();
            for (Check_XML_Consistency.checkXMLConsistency err : errors) {
                result.append(err.toString()).append("\n");
            }
            outputArea.setText(result.toString());
        }
    }
}
