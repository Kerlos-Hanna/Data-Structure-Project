package com.example.social_networks.requirements_level1.operations.minify_xml;

public class Minify_XML_Size {

    /**
     * Minifies XML by removing unnecessary whitespaces and newlines
     * without changing the XML structure.
     */
    
    public static String minify(String xml) {

        if (xml == null || xml.isBlank()) {
            throw new IllegalArgumentException("XML input cannot be null or empty");
        }

        // Remove whitespace between tags
        xml = xml.replaceAll(">\\s+<", "><");

        // Remove whitespace after opening tags and before closing tags
        xml = xml.replaceAll(">\\s+([^<]*?)\\s+<", ">$1<");
        
        // Trim leading and trailing whitespace
        xml = xml.trim();

        return xml;
    }
}
