package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.save_output_xml.OutputSaver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Launcher extends Application {

    TextArea outputArea;
    TextArea inputArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {

        // ===== XML Input Area =====
        Label inputLabel = new Label("XML Input");
        inputArea.setPromptText("Paste XML here...");
        inputArea.setPrefHeight(200);

        // ===== Output Area (Read-only) =====
        Label outputLabel = new Label("Output");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPromptText("Operation output will appear here...");
        outputArea.setPrefHeight(200);

        // ===== Buttons =====
        Button checkBtn = new Button("Check XML Consistency");
        Button formatBtn = new Button("Format (Prettify) XML");
        Button jsonBtn = new Button("Convert XML to JSON");
        Button minifyBtn = new Button("Minify XML");
        Button compressBtn = new Button("Compress XML");
        Button decompressBtn = new Button("Decompress XML");
        Button saveBtn = new Button("Save Output");

        // ===== Button Actions =====
        saveBtn.setOnAction(e -> {
            OutputSaver.save(primaryStage, outputArea.getText());
        });

        // ===== Layout =====
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        root.getChildren().addAll(
                inputLabel,
                inputArea,
                outputLabel,
                outputArea,
                checkBtn,
                formatBtn,
                jsonBtn,
                minifyBtn,
                compressBtn,
                decompressBtn,
                saveBtn
        );

        // ===== Scene & Stage =====
        Scene scene = new Scene(root, 600, 700);
        primaryStage.setTitle("XML Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
