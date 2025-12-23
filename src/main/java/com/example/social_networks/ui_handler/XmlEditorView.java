package com.example.social_networks.ui_handler;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class XmlEditorView {

    private final TextArea inputArea = new TextArea();
    private final VBox lineNumbersBox = new VBox();
    private final ScrollPane lineNumbersScroll = new ScrollPane();

    public XmlEditorView() {
        setupInputArea();
        setupLineNumbers();
        bindScroll();
        bindLineUpdates();
    }

    public HBox getView() {
        HBox box = new HBox(lineNumbersScroll, inputArea);
        HBox.setHgrow(inputArea, Priority.ALWAYS);
        return box;
    }

    public TextArea getInputArea() {
        return inputArea;
    }

    // ---------------- private helpers ----------------

    private void setupInputArea() {
        inputArea.setWrapText(false);
        inputArea.setPrefHeight(300);
        inputArea.setStyle(
                "-fx-control-inner-background: white;" +
                        "-fx-padding: 6 10 0 6;" +
                        "-fx-font-family: monospace;" +
                        "-fx-font-size: 12px;" +
                        "-fx-focus-color: transparent;"
        );
    }

    private void setupLineNumbers() {
        lineNumbersBox.setPadding(new Insets(6, 5, 0, 5));
        lineNumbersBox.setStyle("-fx-background-color: #f0f0f0;");

        lineNumbersScroll.setContent(lineNumbersBox);
        lineNumbersScroll.setPrefWidth(45);
        lineNumbersScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        lineNumbersScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void bindLineUpdates() {
        inputArea.textProperty().addListener((obs, o, n) -> updateLineNumbers());
        updateLineNumbers();
    }

    private void updateLineNumbers() {
        lineNumbersBox.getChildren().clear();
        String[] lines = inputArea.getText().split("\n", -1);
        double lineHeight = inputArea.getFont().getSize() * 1.25;

        for (int i = 0; i < lines.length; i++) {
            Label ln = new Label(String.valueOf(i + 1));
            ln.setMinHeight(lineHeight);
            ln.setPrefHeight(lineHeight);
            ln.setStyle(
                    "-fx-font-family: monospace;" +
                            "-fx-font-size: 12px;" +
                            "-fx-alignment: CENTER-RIGHT;"
            );
            lineNumbersBox.getChildren().add(ln);
        }
    }

    private void bindScroll() {
        inputArea.lookupAll(".scroll-bar").forEach(node -> {
            if (node instanceof ScrollBar sb &&
                    sb.getOrientation() == Orientation.VERTICAL) {

                lineNumbersScroll.vvalueProperty()
                        .bindBidirectional(sb.valueProperty());
            }
        });
    }
}
