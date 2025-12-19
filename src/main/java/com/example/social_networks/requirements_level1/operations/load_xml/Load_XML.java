package com.example.social_networks.requirements_level1.operations.load_xml;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class Load_XML extends Application {

    @Override
    public void start(Stage stage) {
        Label result = new Label("No file selected");

        Button chooseBtn = new Button("Choose XML File");
        chooseBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose an XML File");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml")
            );

            File file = chooser.showOpenDialog(stage);
            if (file == null) {
                result.setText("No file selected");
            } else {
                result.setText("Selected: " + file.getAbsolutePath());
                System.out.println("Selected XML: " + file.getAbsolutePath());
            }
        });

        VBox root = new VBox(10, chooseBtn, result);
        root.setStyle("-fx-padding: 15;");

        stage.setTitle("Task 9 - XML File Chooser");
        stage.setScene(new Scene(root, 520, 140));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

