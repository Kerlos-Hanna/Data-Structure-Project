package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.check_xml_consistency.CheckConsistencyHandler;
import com.example.social_networks.requirements_level1.operations.compress_xml.CompressHandler;
import com.example.social_networks.requirements_level1.operations.conver_xml_to_json.ConvertHandler;
import com.example.social_networks.requirements_level1.operations.decompress_xml.DecompressHandler;
import com.example.social_networks.requirements_level1.operations.format_xml.FormatHandler;
import com.example.social_networks.requirements_level1.operations.load_xml.Load_XML;
import com.example.social_networks.requirements_level1.operations.minify_xml.MinifyHandler;
import com.example.social_networks.requirements_level1.operations.save_output_xml.OutputSaver;
import com.example.social_networks.ui_handler.*;
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
        outputArea.setPrefHeight(200);
        outputArea.setMinHeight(200);
        outputArea.setMaxHeight(200);

        // ================= LEVEL 1 BUTTONS =================
        Label level1Label = new Label("Level 1 Operations");

        Button checkBtn = new Button("Check XML Consistency");
        Button formatBtn = new Button("Format (Prettify) XML");
        Button jsonBtn = new Button("Convert XML to JSON");
        Button minifyBtn = new Button("Minify XML");
        Button compressBtn = new Button("Compress XML");
        Button decompressBtn = new Button("Decompress XML");
        Button loadBtn = new Button("Browse XML");
        Button saveBtn = new Button("Save Output");

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

        for (Button b : new Button[]{
                checkBtn, formatBtn, jsonBtn, minifyBtn,
                compressBtn, decompressBtn, loadBtn, saveBtn
        }) {
            b.setMaxWidth(Double.MAX_VALUE);
        }

        GridPane level1Grid = new GridPane();
        level1Grid.setHgap(10);
        level1Grid.setVgap(10);

        level1Grid.add(checkBtn, 0, 0);
        level1Grid.add(formatBtn, 1, 0);
        level1Grid.add(jsonBtn, 2, 0);
        level1Grid.add(minifyBtn, 3, 0);

        level1Grid.add(compressBtn, 0, 1);
        level1Grid.add(decompressBtn, 1, 1);
        level1Grid.add(loadBtn, 2, 1);
        level1Grid.add(saveBtn, 3, 1);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            level1Grid.getColumnConstraints().add(cc);
        }

        VBox level1Box = new VBox(8, level1Label, level1Grid);
        level1Box.setPadding(new Insets(10));
        level1Box.setMaxWidth(Double.MAX_VALUE);
        level1Box.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        HBox.setHgrow(level1Box, Priority.ALWAYS);

        // ================= LEVEL 2 BUTTONS =================
        Label level2Label = new Label("Level 2 Operations");

        Button drawGraphBtn = new Button("Draw XML Graph");
        Button mostActiveBtn = new Button("Most Active User");
        Button mostInfluencerBtn = new Button("Most Influencer");
        Button mutualUsersBtn = new Button("Mutual Users");
        Button suggestUsersBtn = new Button("Suggest Users");
        Button searchWordBtn = new Button("Search by Word");
        Button searchTopicBtn = new Button("Search by Topic");

        drawGraphBtn.setOnAction(e ->
                DrawGraphHandler.handle(primaryStage, editor.getInputArea())
        );

        mostActiveBtn.setOnAction(e ->
                MostActiveUserHandler.handle(editor.getInputArea(), outputArea)
        );

        mostInfluencerBtn.setOnAction(e ->
                MostInfluencerHandler.handle(editor.getInputArea(), outputArea)
        );

        mutualUsersBtn.setOnAction(e ->
                MutualFriendsHandler.handle(editor.getInputArea(), outputArea)
        );

        searchTopicBtn.setOnAction(e ->
                SearchPostsTopicsHandler.handle(editor.getInputArea(), outputArea)
        );

        searchWordBtn.setOnAction(e ->
                SearchPostsWordsHandler.handle(editor.getInputArea(), outputArea)
        );

        suggestUsersBtn.setOnAction(e ->
                SuggestedFriendsHandler.handle(editor.getInputArea(), outputArea)
        );


        for (Button b : new Button[]{
                drawGraphBtn, mostActiveBtn, mostInfluencerBtn,
                mutualUsersBtn, suggestUsersBtn, searchWordBtn, searchTopicBtn
        }) {
            b.setMaxWidth(Double.MAX_VALUE);
        }

        GridPane level2Grid = new GridPane();
        level2Grid.setHgap(10);
        level2Grid.setVgap(10);

        level2Grid.add(drawGraphBtn, 0, 0);
        level2Grid.add(mostActiveBtn, 1, 0);
        level2Grid.add(mostInfluencerBtn, 2, 0);
        level2Grid.add(mutualUsersBtn, 3, 0);

        level2Grid.add(suggestUsersBtn, 0, 1);
        level2Grid.add(searchWordBtn, 1, 1);
        level2Grid.add(searchTopicBtn, 2, 1);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            level2Grid.getColumnConstraints().add(cc);
        }

        VBox level2Box = new VBox(8, level2Label, level2Grid);
        level2Box.setPadding(new Insets(10));
        level2Box.setMaxWidth(Double.MAX_VALUE);
        level2Box.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        HBox.setHgrow(level2Box, Priority.ALWAYS);

        // ================= BUTTONS SECTION =================
        HBox buttonsSection = new HBox(20, level1Box, level2Box);
        buttonsSection.setPrefWidth(Double.MAX_VALUE);

        // ================= ROOT =================
        VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(
                new Label("XML Input"),
                editor.getView(),
                outputLabel,
                outputArea,
                buttonsSection
        );

        Scene scene = new Scene(root, 1150, 720);
        primaryStage.setTitle("XML Processing Tool");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
