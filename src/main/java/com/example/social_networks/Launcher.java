package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.check_xml_consistency.CheckConsistencyHandler;
import com.example.social_networks.requirements_level1.operations.check_xml_consistency.Check_XML_Consistency;
import com.example.social_networks.requirements_level1.operations.compress_xml.CompressHandler;
import com.example.social_networks.requirements_level1.operations.conver_xml_to_json.ConvertHandler;
import com.example.social_networks.requirements_level1.operations.decompress_xml.DecompressHandler;
import com.example.social_networks.requirements_level1.operations.format_xml.FormatHandler;
import com.example.social_networks.requirements_level1.operations.load_xml.Load_XML;
import com.example.social_networks.requirements_level1.operations.minify_xml.MinifyHandler;
import com.example.social_networks.requirements_level1.operations.save_output_xml.OutputSaver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Launcher extends Application {

    TextArea outputArea;
    TextArea inputArea = new TextArea();
    Check_XML_Consistency checker = new Check_XML_Consistency();

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
        Button load = new Button("Browse XML");
        Button saveBtn = new Button("Save Output");

        // ===== Button Actions =====
        saveBtn.setOnAction(e -> {
            OutputSaver.save(primaryStage, outputArea.getText());
        });

        load.setOnAction(e -> {
            String xmlContent = Load_XML.load(primaryStage);
            if (xmlContent != null) {
                inputArea.setText(xmlContent);
            }
        });

        minifyBtn.setOnAction(e ->
                MinifyHandler.handle(inputArea, outputArea)
        );

        compressBtn.setOnAction(e ->
                CompressHandler.handle(inputArea, outputArea)
        );

        jsonBtn.setOnAction(e ->
                ConvertHandler.handle(inputArea, outputArea)
        );

        formatBtn.setOnAction(e ->{
            FormatHandler.handle(inputArea, outputArea);
        });

        checkBtn.setOnAction(e ->
                CheckConsistencyHandler.handle(inputArea, outputArea)
        );

        decompressBtn.setOnAction(e ->
                DecompressHandler.handle(inputArea, outputArea)
        );




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
                load,
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
