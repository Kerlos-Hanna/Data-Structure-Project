package com.example.social_networks.requirements_level1.operations.load_xml;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Load_XML {

    public static String load(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose a TXT File");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files (.txt)", ".txt")
        );

        File file = chooser.showOpenDialog(stage);
        if (file == null) {
            return null;
        }

        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            return "Error reading TXT file!";
        }
    }
}
