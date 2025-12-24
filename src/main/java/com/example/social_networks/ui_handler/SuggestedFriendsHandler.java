package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level2.SuggestedFriends;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.List;
import java.util.Vector;

import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;
import com.example.social_networks.requirements_level1.parsing_xml.Tag;

public class SuggestedFriendsHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {
        String xml = inputArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            outputArea.setText("❌ Please load XML first.");
            return;
        }

        // Ask user for the ID to suggest friends for
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Suggested Friends");
        dialog.setHeaderText("Enter user ID:");
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) return;

        int userId;
        try {
            userId = Integer.parseInt(input.get().trim());
        } catch (NumberFormatException e) {
            outputArea.setText("❌ Invalid user ID.");
            return;
        }

        // Build graph from XML
        SuggestedFriends.Graph graph = new SuggestedFriends.Graph();

        Vector<Tag> tags = Parsing_XML.parse(xml);
        int currentUserId = -1;
        boolean insideUser = false;

        for (Tag tag : tags) {
            if (tag.isOpening && tag.name.equals("user")) {
                insideUser = true;
                currentUserId = -1;
            } else if (insideUser && tag.isOpening && tag.name.equals("id") && currentUserId == -1) {
                currentUserId = Integer.parseInt(tag.innerText.trim());
            } else if (insideUser && tag.isOpening && tag.name.equals("follower")) {
                // follower contains <id>
            } else if (insideUser && tag.isOpening && tag.name.equals("id") && currentUserId != -1) {
                int followerId = Integer.parseInt(tag.innerText.trim());
                graph.addEdge(currentUserId, followerId);
            } else if (tag.isClosing && tag.name.equals("user")) {
                insideUser = false;
            }
        }

        // Suggest friends
        SuggestedFriends.Suggestions sugg = new SuggestedFriends.Suggestions();
        List<Integer> suggestedUsers = sugg.suggest(graph, userId);

        if (suggestedUsers.isEmpty()) {
            outputArea.setText("❌ No suggested users found for user ID: " + userId);
            return;
        }

        StringBuilder result = new StringBuilder();
        result.append("✅ Suggested users for ID ").append(userId).append(":\n");
        result.append("--------------------------------\n");
        for (int id : suggestedUsers) {
            result.append("User ID: ").append(id).append("\n");
        }

        outputArea.setText(result.toString());
    }
}