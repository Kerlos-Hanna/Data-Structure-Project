package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level2.MostInfluencer;
import javafx.scene.control.TextArea;

public class MostInfluencerHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {

        // Get XML from input
        String xml = inputArea.getText();

        // Validate input
        if (xml == null || xml.trim().isEmpty()) {
            outputArea.setText("❌ Please load XML first.");
            return;
        }

        // Call logic class
        MostInfluencer.Result result =
                MostInfluencer.findMostInfluencer(xml);

        // Handle no result
        if (result == null) {
            outputArea.setText("❌ No users found in XML.");
            return;
        }

        // Display result
        StringBuilder output = new StringBuilder();
        output.append("✅ Most Influencer User\n");
        output.append("----------------------------\n");
        output.append("ID        : ").append(result.id).append("\n");
        output.append("Name      : ").append(result.name).append("\n");
        output.append("Followers : ").append(result.followers).append("\n");

        outputArea.setText(output.toString());
    }
}
