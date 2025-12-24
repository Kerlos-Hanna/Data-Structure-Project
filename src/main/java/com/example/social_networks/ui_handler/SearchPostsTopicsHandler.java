package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level2.SearchPostsTopics;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class SearchPostsTopicsHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {
        String xml = inputArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            outputArea.setText("❌ Please load XML first.");
            return;
        }

        // Ask user for topic
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Posts by Topic");
        dialog.setHeaderText("Enter topic to search:");
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) return;

        String topic = input.get().trim();
        if (topic.isEmpty()) {
            outputArea.setText("❌ Topic cannot be empty.");
            return;
        }

        // Use SearchPostsTopics class
        SearchPostsTopics searcher = new SearchPostsTopics();
        List<String> posts = searcher.searchTopics(xml, topic);

        if (posts.isEmpty()) {
            outputArea.setText("❌ No posts found for topic: " + topic);
            return;
        }

        // Build result
        StringBuilder sb = new StringBuilder();
        sb.append("✅ Posts with topic: ").append(topic).append("\n");
        sb.append("--------------------------------\n");
        for (String post : posts) {
            sb.append(post).append("\n\n");
        }

        outputArea.setText(sb.toString());
    }
}