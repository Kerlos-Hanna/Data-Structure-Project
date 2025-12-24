package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.check_xml_consistency.Check_XML_Consistency;
import com.example.social_networks.requirements_level1.operations.compress_xml.Compress_XML;
import com.example.social_networks.requirements_level1.operations.conver_xml_to_json.Convert_XML_To_JSON;
import com.example.social_networks.requirements_level1.operations.decompress_xml.Decompression_XML;
import com.example.social_networks.requirements_level1.operations.format_xml.Format_XML;
import com.example.social_networks.requirements_level1.operations.minify_xml.Minify_XML_Size;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class CommandLine {
    public static void main(String[] args) {
        new CommandLine().run(args);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0].toLowerCase();
        String input = null;
        String output = null;
        boolean fix = false;

        // Parse flags
        for (int i = 1; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-i":
                    if (i + 1 < args.length) input = args[++i];
                    break;
                case "-o":
                    if (i + 1 < args.length) output = args[++i];
                    break;
                case "-f":
                    fix = true;
                    break;
                default:
                    System.out.println("Unknown option: " + args[i]);
            }
        }

        if (input == null) {
            System.out.println("Input file is required (-i).");
            return;
        }

        try {
            switch (command) {
                case "verify":
                    handleVerify(input, output, fix);
                    break;
                case "format":
                    handleFormat(input, output);
                    break;
                case "json":
                    handleConvert(input, output);
                    break;
                case "mini":
                    handleMinify(input, output);
                    break;
                case "compress":
                    handleCompress(input, output);
                    break;
                case "decompress":
                    handleDecompress(input, output);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    printHelp();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------- Handlers -------------------

private void handleVerify(String input, String output, boolean fix) {
    try {
        // Read the input XML file
        String xml = Files.readString(Path.of(input));
        Check_XML_Consistency checker = new Check_XML_Consistency();

        // Validate XML and print the result
        //String validationResult = checker.checkXMLConsistency(xml);
        //System.out.println(validationResult);

        // Fix XML if requested
        if (fix && output != null) {
            String fixedXML = checker.autoFix(xml);  // <-- Use fixXML instead of checkXMLConsistency
            Files.writeString(Path.of(output), fixedXML);
            System.out.println("Fixed XML saved to: " + output);
        }

    } catch (IOException e) {
        System.err.println("File error: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Error verifying XML: " + e.getMessage());
    }
}


    private void handleFormat(String input, String output) {
        try {
            String xml = Files.readString(Path.of(input));
            String formattedXML = Format_XML.formatXML(xml);

            if (output != null) {
                Files.writeString(Path.of(output), formattedXML);
                System.out.println("Formatted XML saved to: " + output);
            } else {
                System.out.println(formattedXML);
            }

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error formatting XML: " + e.getMessage());
        }
    }

    private void handleConvert(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for JSON conversion (-o).");
            return;
        }

        try {
            String xml = Files.readString(Path.of(input));
            Convert_XML_To_JSON converter = new Convert_XML_To_JSON(xml);
            String jsonOutput = converter.convert();

            File outFile = new File(output);
            if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();
            Files.writeString(Path.of(output), jsonOutput);

            System.out.println("Conversion completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error converting XML to JSON: " + e.getMessage());
        }
    }

    private void handleMinify(String input, String output) {
        try {
            String xml = Files.readString(Path.of(input));
            String minified = Minify_XML_Size.minify(xml);

            if (output != null) {
                Files.writeString(Path.of(output), minified);
                System.out.println("Minified XML saved to: " + output);
            } else {
                System.out.println(minified);
            }

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error minifying XML: " + e.getMessage());
        }
    }

    private void handleCompress(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for compression (-o).");
            return;
        }

        try {
            Compress_XML.compress(input, output);
            System.out.println("Compression completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error compressing XML: " + e.getMessage());
        }
    }

    private void handleDecompress(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for decompression (-o).");
            return;
        }

        try {
            Decompression_XML decompressor = new Decompression_XML();
            if (input.toLowerCase().endsWith(".gz")) {
                decompressor.decompressGZIP(input, output);
            } else if (input.toLowerCase().endsWith(".zip")) {
                decompressor.decompressZIP(input, output);
            } else {
                System.out.println("Unsupported file type. Only .gz and .zip are supported.");
                return;
            }

            System.out.println("Decompression completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error decompressing XML: " + e.getMessage());
        }
    }

    // ------------------- Help -------------------

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  verify -i <input.xml> [-f] [-o <output.xml>]   : Check XML consistency, optionally fix errors.");
        System.out.println("  format -i <input.xml> [-o <output.xml>]       : Prettify XML and save.");
        System.out.println("  json -i <input.xml> -o <output.json>          : Convert XML to JSON.");
        System.out.println("  mini -i <input.xml> [-o <output.xml>]         : Minify XML.");
        System.out.println("  compress -i <input.xml> -o <output.comp>      : Compress XML.");
        System.out.println("  decompress -i <input.gz|.zip> -o <output.xml>: Decompress XML.");
    }
}
