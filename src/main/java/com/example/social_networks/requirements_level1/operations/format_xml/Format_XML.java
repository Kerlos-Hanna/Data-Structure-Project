package com.example.social_networks.requirements_level1.operations.format_xml;

import java.util.Stack;

public class Format_XML {

    private static final int INDENT_SIZE = 2;

    public static String formatXML(String xml) {
        // to use this you

        StringBuilder result = new StringBuilder();
        Stack<String> stack = new Stack<>();

        int i = 0;
        int depth = 0;

        while (i < xml.length()) {

            // Skip whitespace outside text
            if (Character.isWhitespace(xml.charAt(i))) {
                i++;
                continue;
            }

            // Tag detected
            if (xml.charAt(i) == '<') {
                int end = xml.indexOf('>', i);
                if (end == -1) {
                    return "Error: Invalid XML (missing >)";
                }

                String tag = xml.substring(i + 1, end).trim();

                // Closing tag
                if (tag.startsWith("/")) {
                    depth--;
                    indent(result, depth);
                    result.append("<").append(tag).append(">\n");
                    if (!stack.isEmpty()) stack.pop();
                }
                // Opening tag
                else {
                    indent(result, depth);
                    result.append("<").append(tag).append(">\n");
                    stack.push(tag);
                    depth++;
                }

                i = end + 1;
            }
            // Text content
            else {
                int end = xml.indexOf('<', i);
                if (end == -1) end = xml.length();

                String text = xml.substring(i, end).trim();
                if (!text.isEmpty()) {
                    indent(result, depth);
                    result.append(text).append("\n");
                }

                i = end;
            }
        }

        return result.toString();
    }

    // Helper method for indentation
    private static void indent(StringBuilder sb, int depth) {
        for (int i = 0; i < depth * INDENT_SIZE; i++) {
            sb.append(' ');
        }
    }


}
