package com.example.social_networks.requirements_level1.operations.check_xml_consistency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.*;
import org.xml.sax.InputSource;

import java.io.StringReader;

import org.w3c.dom.Document;

public class Check_XML_Consistency {

    // =============================================================
    // FIX COMMON SIMPLE XML ERRORS
    // =============================================================
    public String fixXML(String xml) {
        if (xml == null || xml.isBlank()) return xml;

        // Auto-close common tags (basic fixing only)
        xml = xml.replaceAll("<price>([^<]+)(?!</price>)", "<price>$1</price>");
        xml = xml.replaceAll("<title>([^<]+)(?!</title>)", "<title>$1</title>");
        xml = xml.replaceAll("<author>([^<]+)(?!</author>)", "<author>$1</author>");
        xml = xml.replaceAll("<year>([^<]+)(?!</year>)", "<year>$1</year>");

        return xml.trim();
    }

    // =============================================================
    // CHECK XML CONSISTENCY (WELL-FORMEDNESS)
    // =============================================================
    public String checkXMLConsistency(String xmlText) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException ex) throws SAXException { throw ex; }
                public void error(SAXParseException ex) throws SAXException { throw ex; }
                public void fatalError(SAXParseException ex) throws SAXException { throw ex; }
            });

            Document doc = builder.parse(
                    new InputSource(new StringReader(xmlText))
            );

            return "XML is well-formed ✔\nNo errors detected.";

        } catch (SAXParseException ex) {
            return " XML Parsing Error\nLine " + ex.getLineNumber() +
                   ", Column " + ex.getColumnNumber() +
                   "\nMessage: " + ex.getMessage();
        } catch (Exception ex) {
            return " Error: " + ex.getMessage();
        }
    }
}
