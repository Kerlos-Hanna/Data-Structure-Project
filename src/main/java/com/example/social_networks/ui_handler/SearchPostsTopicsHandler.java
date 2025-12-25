package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;
import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import com.example.social_networks.requirements_level2.DrawGraph;
import com.example.social_networks.requirements_level2.Post;
import com.example.social_networks.requirements_level2.SearchPostsTopics;
import com.example.social_networks.requirements_level2.User;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.Vector;

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

        // Parse XML → Tags
        Vector<Tag> tags = Parsing_XML.parse(xml);
        if (tags == null || tags.isEmpty()) {
            outputArea.setText("❌ Invalid XML.");
            return;
        }

        // Build users from tags
        Vector<User> users = DrawGraph.buildUsers(tags);

        // Search posts by topic
        Vector<Post> posts = SearchPostsTopics.searchPostsByTopic(users, topic);

        if (posts.isEmpty()) {
            outputArea.setText("❌ No posts found for topic: " + topic);
            return;
        }

        // Build output
        StringBuilder sb = new StringBuilder();
        sb.append("✅ Posts with topic: ").append(topic).append("\n");
        sb.append("--------------------------------\n\n");

        for (Post post : posts) {
            sb.append(post.body).append("\n\n");
        }

        outputArea.setText(sb.toString());
    }
}
