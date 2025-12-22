package com.example.social_networks.requirements_level1.operations.compress_xml;

import javafx.scene.control.TextArea;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;

public class CompressHandler {

    public static void handle(TextArea inputArea, TextArea outputArea) {
        try {
            String xmlContent = inputArea.getText();

            if (xmlContent == null || xmlContent.isBlank()) {
                outputArea.setText("❌ No XML to compress");
                return;
            }

            // Temp input file
            File inputFile = File.createTempFile("xml_input", ".txt");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile))) {
                bw.write(xmlContent);
            }

            // Temp output file
            File outputFile = File.createTempFile("xml_compressed", ".txt");

            // Call compressor
            Compress_XML.compress(
                    inputFile.getAbsolutePath(),
                    outputFile.getAbsolutePath()
            );

            // Read result
            String compressed = new String(
                    java.nio.file.Files.readAllBytes(outputFile.toPath())
            );

            outputArea.setText(compressed);

            long before = inputFile.length();
            long after = outputFile.length();

            System.out.println("Before: " + before + " bytes");
            System.out.println("After: " + after + " bytes");

            // Cleanup
            inputFile.delete();
            outputFile.delete();

        } catch (Exception ex) {
            outputArea.setText("❌ Compression failed:\n" + ex.getMessage());
        }
    }
}
