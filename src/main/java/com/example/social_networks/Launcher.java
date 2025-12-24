package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.check_xml_consistency.CheckConsistencyHandler;
import com.example.social_networks.requirements_level1.operations.compress_xml.CompressHandler;
import com.example.social_networks.requirements_level1.operations.conver_xml_to_json.ConvertHandler;
import com.example.social_networks.requirements_level1.operations.decompress_xml.DecompressHandler;
import com.example.social_networks.requirements_level1.operations.format_xml.FormatHandler;
import com.example.social_networks.requirements_level1.operations.load_xml.Load_XML;
import com.example.social_networks.requirements_level1.operations.minify_xml.MinifyHandler;
import com.example.social_networks.requirements_level1.operations.save_output_xml.OutputSaver;
import com.example.social_networks.ui_handler.XmlEditorView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Launcher extends Application {

    TextArea outputArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {

        XmlEditorView editor = new XmlEditorView();

        // ===== Output Area =====
        Label outputLabel = new Label("Output");
        outputArea.setEditable(false);

        // 🔒 FIX OUTPUT AREA SIZE (ONLY CHANGE)
        outputArea.setPrefHeight(200);
        outputArea.setMinHeight(200);
        outputArea.setMaxHeight(200);

        // ===== Buttons (UNCHANGED) =====
        Button checkBtn = new Button("Check XML Consistency");
        Button formatBtn = new Button("Format (Prettify) XML");
        Button jsonBtn = new Button("Convert XML to JSON");
        Button minifyBtn = new Button("Minify XML");
        Button compressBtn = new Button("Compress XML");
        Button decompressBtn = new Button("Decompress XML");
        Button loadBtn = new Button("Browse XML");
        Button saveBtn = new Button("Save Output");

        // ===== Button Actions (UNCHANGED) =====
        saveBtn.setOnAction(e -> OutputSaver.save(primaryStage, outputArea.getText()));

        loadBtn.setOnAction(e -> {
            String xmlContent = Load_XML.load(primaryStage);
            if (xmlContent != null) {
                editor.getInputArea().setText(xmlContent);
            }
        });

        minifyBtn.setOnAction(e -> MinifyHandler.handle(editor.getInputArea(), outputArea));
        compressBtn.setOnAction(e -> CompressHandler.handle(editor.getInputArea(), outputArea));
        jsonBtn.setOnAction(e -> ConvertHandler.handle(editor.getInputArea(), outputArea));
        formatBtn.setOnAction(e -> FormatHandler.handle(editor.getInputArea(), outputArea));
        checkBtn.setOnAction(e -> CheckConsistencyHandler.handle(editor.getInputArea(), outputArea));
        decompressBtn.setOnAction(e -> DecompressHandler.handle(editor.getInputArea(), outputArea));

        // ===== Buttons Grid (UNCHANGED) =====
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setPadding(new Insets(10, 0, 10, 0));

        ColumnConstraints col = new ColumnConstraints();
        col.setHgrow(Priority.ALWAYS);
        buttonGrid.getColumnConstraints().addAll(col, col, col, col);

        buttonGrid.add(checkBtn, 0, 0);
        buttonGrid.add(formatBtn, 1, 0);
        buttonGrid.add(jsonBtn, 2, 0);
        buttonGrid.add(minifyBtn, 3, 0);
        buttonGrid.add(compressBtn, 0, 1);
        buttonGrid.add(decompressBtn, 1, 1);
        buttonGrid.add(loadBtn, 2, 1);
        buttonGrid.add(saveBtn, 3, 1);

        // ===== Root Layout =====
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                new Label("XML Input"),
                editor.getView(),
                outputLabel,
                outputArea,
                buttonGrid
        );

        Scene scene = new Scene(root, 650, 700);
        primaryStage.setTitle("XML Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
