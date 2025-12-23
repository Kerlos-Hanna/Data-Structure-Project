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
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Launcher extends Application {

    TextArea inputArea = new TextArea();
    VBox lineNumbersBox = new VBox();
    ScrollPane lineNumbersScroll = new ScrollPane();
    TextArea outputArea = new TextArea();
    Check_XML_Consistency checker = new Check_XML_Consistency();

    @Override
    public void start(Stage primaryStage) {

        // ===== Input TextArea =====
        inputArea.setWrapText(false);
        inputArea.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: lightgray;" +
                        "-fx-padding: 6 10 0 6;" +
                        "-fx-font-family: monospace;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 0;" +
                        "-fx-focus-color: transparent;" +
                        "-fx-faint-focus-color: transparent;"
        );
        inputArea.setPrefHeight(300);

        // ===== Line Numbers Box =====
        lineNumbersBox.setPadding(new Insets(6, 5, 0, 5));
        lineNumbersBox.setStyle("-fx-background-color: #f0f0f0;");

        // ===== Line Numbers Scroll =====
        lineNumbersScroll.setContent(lineNumbersBox);
        lineNumbersScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lineNumbersScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lineNumbersScroll.setFitToWidth(true);
        lineNumbersScroll.setPrefWidth(45);
        lineNumbersScroll.setStyle("-fx-background: #f0f0f0;");

        inputArea.textProperty().addListener((obs, oldText, newText) -> updateLineNumbers());

        // ===== Combine Line Numbers + Input =====
        HBox inputBox = new HBox(lineNumbersScroll, inputArea);
        HBox.setHgrow(inputArea, Priority.ALWAYS);

        // ===== Output Area =====
        Label outputLabel = new Label("Output");
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
        Button loadBtn = new Button("Browse XML");
        Button saveBtn = new Button("Save Output");

        // ===== Button Actions =====
        saveBtn.setOnAction(e -> OutputSaver.save(primaryStage, outputArea.getText()));

        loadBtn.setOnAction(e -> {
            String xmlContent = Load_XML.load(primaryStage);
            if (xmlContent != null) {
                inputArea.setText(xmlContent);
            }
        });

        minifyBtn.setOnAction(e -> MinifyHandler.handle(inputArea, outputArea));
        compressBtn.setOnAction(e -> CompressHandler.handle(inputArea, outputArea));
        jsonBtn.setOnAction(e -> ConvertHandler.handle(inputArea, outputArea));
        formatBtn.setOnAction(e -> FormatHandler.handle(inputArea, outputArea));
        checkBtn.setOnAction(e -> CheckConsistencyHandler.handle(inputArea, outputArea));
        decompressBtn.setOnAction(e -> DecompressHandler.handle(inputArea, outputArea));

        // ===== Buttons Grid =====
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setPadding(new Insets(10, 0, 10, 0));

        ColumnConstraints col = new ColumnConstraints();
        col.setHgrow(Priority.ALWAYS);
        col.setFillWidth(true);
        buttonGrid.getColumnConstraints().addAll(col, col, col, col);

        for (Button b : new Button[]{
                checkBtn, formatBtn, jsonBtn, minifyBtn,
                compressBtn, decompressBtn, loadBtn, saveBtn
        }) {
            b.setMaxWidth(Double.MAX_VALUE);
        }

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
                inputBox,
                outputLabel,
                outputArea,
                buttonGrid
        );

        Scene scene = new Scene(root, 650, 700);
        primaryStage.setTitle("XML Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();

        bindScrollBars();
        updateLineNumbers();
    }

    // ===== Calculate Real Line Height =====
    private double getLineHeight() {
        return inputArea.getFont().getSize() * 1.25;
    }

    // ===== Update Line Numbers (ALIGNED) =====
    private void updateLineNumbers() {
        lineNumbersBox.getChildren().clear();
        String[] lines = inputArea.getText().split("\n", -1);

        double lineHeight = getLineHeight();

        for (int i = 0; i < lines.length; i++) {
            Label lineNumber = new Label(String.valueOf(i + 1));

            lineNumber.setMinHeight(lineHeight);
            lineNumber.setPrefHeight(lineHeight);
            lineNumber.setMaxHeight(lineHeight);

            lineNumber.setStyle(
                    "-fx-font-family: monospace;" +
                            "-fx-font-size: 12px;" +
                            "-fx-text-fill: #555;" +
                            "-fx-alignment: CENTER-RIGHT;" +
                            "-fx-padding: 0 5 0 0;"
            );

            lineNumbersBox.getChildren().add(lineNumber);
        }
    }

    // ===== Scroll Sync =====
    private void bindScrollBars() {
        for (var node : inputArea.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar sb &&
                    sb.getOrientation() == Orientation.VERTICAL) {

                lineNumbersScroll.vvalueProperty()
                        .bindBidirectional(sb.valueProperty());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
