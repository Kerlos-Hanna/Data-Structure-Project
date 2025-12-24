package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import com.example.social_networks.requirements_level2.DrawGraph;
import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Vector;

public class DrawGraphHandler {

    public static void handle(Stage stage, TextArea inputArea) {
        try {
            String xml = inputArea.getText();
            if (xml == null || xml.isEmpty()) {
                showError("XML input is empty");
                return;
            }

            // 1️⃣ Parse XML → Tags
            Vector<Tag> tags = Parsing_XML.parse(xml);

            // 2️⃣ Build Users
            var users = DrawGraph.buildUsers(tags);

            // 3️⃣ Build Graph
            var graph = DrawGraph.buildGraph(users);

            // 4️⃣ Choose output image file
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Graph Image");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image (*.jpg)", "*.jpg")
            );

            File file = fc.showSaveDialog(stage);
            if (file == null) return;

            // 5️⃣ Visualize graph
            DrawGraph.visualizeGraph(users, graph, file.getAbsolutePath());

            showInfo("Graph saved successfully");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to draw graph");
        }
    }

    private static void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }

    private static void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.show();
    }
}
