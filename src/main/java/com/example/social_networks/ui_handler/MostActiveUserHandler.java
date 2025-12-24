package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;
import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import com.example.social_networks.requirements_level2.DrawGraph;
import com.example.social_networks.requirements_level2.Graph;
import com.example.social_networks.requirements_level2.MostActiveUser;

import javafx.scene.control.TextArea;
import java.util.Vector;

public class MostActiveUserHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {

        outputArea.clear();

        try {
            String xml = inputArea.getText();
            if (xml == null || xml.isBlank()) {
                outputArea.setText("Please enter XML content first.");
                return;
            }

            // 1️⃣ Parse XML
            Vector<Tag> tags = Parsing_XML.parse(xml);

            // 2️⃣ Build users
            var users = DrawGraph.buildUsers(tags);

            if (users.isEmpty()) {
                outputArea.setText("No users found in XML.");
                return;
            }

            // 3️⃣ Build graph (THIS is your real Graph)
            Graph graph = DrawGraph.buildGraph(users);

            // 4️⃣ Compute most active user
            int idx = MostActiveUser.mostActive(graph);

            if (idx == -1) {
                outputArea.setText("No active users found.");
                return;
            }

            // 5️⃣ Output
            outputArea.setText(
                    "Most Active User\n" +
                            "-----------------\n" +
                            "Name: " + users.get(idx).name + "\n" +
                            "User ID: " + users.get(idx).id + "\n" +
                            "Outgoing Connections: " + graph.neighbors(idx).size()
            );

        } catch (Exception e) {
            e.printStackTrace();
            outputArea.setText("Error while computing most active user.");
        }
    }
}
