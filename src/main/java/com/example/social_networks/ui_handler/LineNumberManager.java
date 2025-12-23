package com.example.social_networks.ui_handler;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class LineNumberManager {

    private final TextArea textArea;
    private final VBox lineBox;

    public LineNumberManager(TextArea textArea, VBox lineBox) {
        this.textArea = textArea;
        this.lineBox = lineBox;
    }

    public void update() {
        lineBox.getChildren().clear();
        String[] lines = textArea.getText().split("\n", -1);
        double lineHeight = textArea.getFont().getSize() * 1.25;

        for (int i = 0; i < lines.length; i++) {
            Label label = new Label(String.valueOf(i + 1));
            label.setMinHeight(lineHeight);
            label.setPrefHeight(lineHeight);
            label.setStyle(
                    "-fx-font-family: monospace;" +
                            "-fx-font-size: 12px;" +
                            "-fx-alignment: CENTER-RIGHT;"
            );
            lineBox.getChildren().add(label);
        }
    }
}
