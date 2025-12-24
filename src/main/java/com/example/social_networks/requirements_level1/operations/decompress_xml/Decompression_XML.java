package com.example.social_networks.requirements_level1.operations.decompress_xml;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.io.*;
import java.util.zip.*;

public class Decompression_XML  {

    // Decompress a GZIP file
    public void decompressGZIP(String inputPath, String outputPath) throws IOException {
        System.out.println("Decompressing GZIP file...");
        try (FileInputStream fis = new FileInputStream(inputPath);
             GZIPInputStream gis = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalBytes = 0;

            while ((bytesRead = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                System.out.println("Decompressed " + totalBytes + " bytes...");
            }

            System.out.println("GZIP decompression complete. Output: " + outputPath);
        }
    }

    // Decompress a ZIP file
public void decompressZIP(String inputPath, String outputPath) throws IOException {
    System.out.println("Decompressing ZIP file...");
    File outputDir = new File(outputPath);
    if (!outputDir.exists()) outputDir.mkdirs();

    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(inputPath))) {
        ZipEntry zipEntry;

        while ((zipEntry = zis.getNextEntry()) != null) {
            File newFile = new File(outputDir, zipEntry.getName());
            System.out.println("Extracting: " + zipEntry.getName());

            // ✅ HANDLE DIRECTORIES FIRST
            if (zipEntry.isDirectory()) {
                newFile.mkdirs();
                zis.closeEntry();
                continue;
            }

            // ✅ CREATE PARENT DIRECTORIES
            File parent = newFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = zis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            zis.closeEntry();
        }
    }

    System.out.println("ZIP decompression complete.");
}


}

