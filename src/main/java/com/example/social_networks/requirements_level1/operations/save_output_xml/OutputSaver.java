package com.example.social_networks.requirements_level1.operations.save_output_xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputSaver {

    public static void save(Stage stage, String content) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output XML File");

        // ✅ Accept only XML
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("TXT Files", "*.txt")
        );

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {

            // ✅ Force .xml extension
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            } catch (IOException e) {
                throw new RuntimeException("Error saving XML file", e);
            }
        }
    }
}
