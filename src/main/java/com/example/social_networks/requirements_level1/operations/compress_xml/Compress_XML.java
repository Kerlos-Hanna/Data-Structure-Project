package com.example.social_networks.requirements_level1.operations.compress_xml;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.*;

/**
 * XML Compressor based on proper XML parsing (DOM).
 * Repeated element tags are replaced with rule tokens {@n}.
 * Limitations:
 * - Attributes are preserved but not compressed
 * - Namespaces are ignored
 */
public class Compress_XML {

    /* ------------------------------------------------------------
     * Public API
     * ------------------------------------------------------------ */

    public static void compress(String inputFilePath, String outputFilePath) throws Exception {
        String xml = readFile(inputFilePath);

        Document doc = parseXml(xml);

        // Step 1: Count tag frequencies
        Map<String, Integer> frequency = new HashMap<>();
        countTags(doc.getDocumentElement(), frequency);

        // Step 2: Build rules (only repeated tags)
        Map<String, Integer> rules = buildRules(frequency);

        // Step 3: Serialize + compress
        StringBuilder compressed = new StringBuilder();
        serializeCompressed(doc.getDocumentElement(), rules, compressed);

        // Step 4: Write output
        writeCompressedFile(outputFilePath, rules, compressed.toString());
    }

    /* ------------------------------------------------------------
     * XML Parsing
     * ------------------------------------------------------------ */

    private static Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        factory.setNamespaceAware(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xml)));
    }

    /* ------------------------------------------------------------
     * Rule Building
     * ------------------------------------------------------------ */

    private static void countTags(Node node, Map<String, Integer> frequency) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String tag = node.getNodeName();
            frequency.put(tag, frequency.getOrDefault(tag, 0) + 1);
        }

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            countTags(children.item(i), frequency);
        }
    }

    private static Map<String, Integer> buildRules(Map<String, Integer> frequency) {
        List<String> repeated = new ArrayList<>();
        for (Map.Entry<String, Integer> e : frequency.entrySet()) {
            if (e.getValue() > 1) {
                repeated.add(e.getKey());
            }
        }

        Collections.sort(repeated);

        Map<String, Integer> rules = new LinkedHashMap<>();
        int id = 1;
        for (String tag : repeated) {
            rules.put(tag, id++);
        }
        return rules;
    }

    /* ------------------------------------------------------------
     * Compression (DOM → compressed text)
     * ------------------------------------------------------------ */

    private static void serializeCompressed(Node node, Map<String, Integer> rules, StringBuilder out) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            String tag = node.getNodeName();
            Integer ruleId = rules.get(tag);

            // Opening tag
            if (ruleId != null) {
                out.append("{@").append(ruleId).append("}");
            } else {
                out.append('<').append(tag).append('>');
            }

            // Children
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                serializeCompressed(children.item(i), rules, out);
            }

            // Closing tag
            if (ruleId != null) {
                out.append("{/@").append(ruleId).append("}");
            } else {
                out.append("</").append(tag).append('>');
            }
        }
        else if (node.getNodeType() == Node.TEXT_NODE) {
            out.append(node.getTextContent());
        }
    }

    /* ------------------------------------------------------------
     * IO Helpers
     * ------------------------------------------------------------ */

    private static String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    private static void writeCompressedFile(String path, Map<String, Integer> rules, String data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("#RULES");
            bw.newLine();
            for (Map.Entry<String, Integer> e : rules.entrySet()) {
                bw.write(e.getValue() + ":" + e.getKey());
                bw.newLine();
            }
            bw.newLine();
            bw.write("#DATA");
            bw.newLine();
            bw.write(data);
        }
    }
}
