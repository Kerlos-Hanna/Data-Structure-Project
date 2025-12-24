package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;
import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import com.example.social_networks.requirements_level2.Graph;
import com.example.social_networks.requirements_level2.MutualFriends;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.*;

public class MutualFriendsHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {

        String xml = inputArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            outputArea.setText("❌ Please load XML first.");
            return;
        }

        /* ================= PARSE XML ================= */

        Vector<Tag> tags = Parsing_XML.parse(xml);

        // Map user ID -> graph index
        Map<Integer, Integer> idToIndex = new HashMap<>();
        List<Integer> indexToId = new ArrayList<>();

        boolean insideUser = false;
        int currentUserId = -1;

        // First pass: collect users
        for (Tag tag : tags) {
            if (tag.isOpening && tag.name.equals("user")) {
                insideUser = true;
                currentUserId = -1;
            }
            else if (insideUser && tag.isOpening && tag.name.equals("id") && currentUserId == -1) {
                currentUserId = Integer.parseInt(tag.innerText.trim());
                if (!idToIndex.containsKey(currentUserId)) {
                    idToIndex.put(currentUserId, indexToId.size());
                    indexToId.add(currentUserId);
                }
            }
            else if (tag.isClosing && tag.name.equals("user")) {
                insideUser = false;
            }
        }

        // Create graph with correct size
        Graph graph = new Graph(indexToId.size());

        /* ================= SECOND PASS: ADD EDGES ================= */

        insideUser = false;
        boolean insideFollower = false;
        currentUserId = -1;

        for (Tag tag : tags) {

            if (tag.isOpening && tag.name.equals("user")) {
                insideUser = true;
                currentUserId = -1;
            }

            else if (insideUser && tag.isOpening && tag.name.equals("id") && currentUserId == -1) {
                currentUserId = Integer.parseInt(tag.innerText.trim());
            }

            else if (insideUser && tag.isOpening && tag.name.equals("follower")) {
                insideFollower = true;
            }

            else if (insideFollower && tag.isOpening && tag.name.equals("id")) {
                int followerId = Integer.parseInt(tag.innerText.trim());

                int u = idToIndex.get(currentUserId);
                int v = idToIndex.get(followerId);

                graph.addEdge(u, v);
            }

            else if (tag.isClosing && tag.name.equals("follower")) {
                insideFollower = false;
            }

            else if (tag.isClosing && tag.name.equals("user")) {
                insideUser = false;
            }
        }

        /* ================= ASK USER IDS ================= */

        TextInputDialog d1 = new TextInputDialog();
        d1.setTitle("Mutual Friends");
        d1.setHeaderText("Enter first user ID:");
        Optional<String> in1 = d1.showAndWait();
        if (in1.isEmpty()) return;

        TextInputDialog d2 = new TextInputDialog();
        d2.setTitle("Mutual Friends");
        d2.setHeaderText("Enter second user ID:");
        Optional<String> in2 = d2.showAndWait();
        if (in2.isEmpty()) return;

        int id1, id2;
        try {
            id1 = Integer.parseInt(in1.get());
            id2 = Integer.parseInt(in2.get());
        } catch (NumberFormatException e) {
            outputArea.setText("❌ Invalid user ID.");
            return;
        }

        if (!idToIndex.containsKey(id1) || !idToIndex.containsKey(id2)) {
            outputArea.setText("❌ User ID not found in XML.");
            return;
        }

        int u = idToIndex.get(id1);
        int v = idToIndex.get(id2);

        /* ================= MUTUAL FRIENDS ================= */

        List<Integer> mutualIndices =
                MutualFriends.getMutualFriends(graph, u, v);

        if (mutualIndices.isEmpty()) {
            outputArea.setText("❌ No mutual friends found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("✅ Mutual Friends between ")
                .append(id1).append(" and ").append(id2).append("\n");
        sb.append("--------------------------------\n");

        for (int idx : mutualIndices) {
            sb.append("User ID: ")
                    .append(indexToId.get(idx))
                    .append("\n");
        }

        outputArea.setText(sb.toString());
    }
}
