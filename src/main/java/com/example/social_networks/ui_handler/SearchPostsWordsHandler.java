package com.example.social_networks.ui_handler;

import com.example.social_networks.requirements_level2.SearchPostsWords;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.List;
import java.util.Optional;

public class SearchPostsWordsHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {
        String xml = inputArea.getText();
        if (xml == null || xml.trim().isEmpty()) {
            outputArea.setText("❌ Please load XML first.");
            return;
        }

        // Ask user for a word
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Posts by Word");
        dialog.setHeaderText("Enter word to search:");
        Optional<String> input = dialog.showAndWait();
        if (input.isEmpty()) return;

        String word = input.get().trim();
        if (word.isEmpty()) {
            outputArea.setText("❌ Word cannot be empty.");
            return;
        }

        // Search posts using SearchPostsWords
        List<String> posts = SearchPostsWords.searchPostsByWord(xml, word);

        if (posts.isEmpty()) {
            outputArea.setText("❌ No posts found containing the word: " + word);
            return;
        }

        // Format results
        String result = SearchPostsWords.formatAsText(posts);
        outputArea.setText(result);
    }
}