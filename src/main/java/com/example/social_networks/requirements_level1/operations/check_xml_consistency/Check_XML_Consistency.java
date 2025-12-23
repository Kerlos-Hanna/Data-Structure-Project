package com.example.social_networks.requirements_level1.operations.check_xml_consistency;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.w3c.dom.Document;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;


public class Check_XML_Consistency {


    // FIX COMMON SIMPLE XML ERRORS
  
    public String fixXML(String xml) {
        if (xml == null || xml.isBlank()) return xml;

        // Auto-close common tags (basic fixing only)
        xml = xml.replaceAll("<price>([^<]+)(?!</price>)", "<price>$1</price>");
        xml = xml.replaceAll("<title>([^<]+)(?!</title>)", "<title>$1</title>");
        xml = xml.replaceAll("<author>([^<]+)(?!</author>)", "<author>$1</author>");
        xml = xml.replaceAll("<year>([^<]+)(?!</year>)", "<year>$1</year>");

        return xml.trim();
    }

    // CHECK XML CONSISTENCY (WELL-FORMEDNESS)


    public String checkXMLConsistency(String xmlText) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(new StringReader(xmlText)), new DefaultHandler());

            return "XML is well-formed ✔\nNo errors detected.";

        } catch (SAXParseException ex) {
            String msg = ex.getMessage();
            // Look for pattern "The element type "xxx" must be terminated..."
            if (msg.contains("must be terminated") || msg.contains("was not closed")) {
                String tag = "unknown";
                int start = msg.indexOf("\"");
                int end = msg.indexOf("\"", start + 1);
                if (start >= 0 && end > start) {
                    tag = msg.substring(start + 1, end);
                }
                return "XML Parsing Error: The <" + tag + "> tag was closed without being opened\n" +
                       "Line " + ex.getLineNumber() +
                       ", Column " + ex.getColumnNumber();
            }

            return "XML Parsing Error\nLine " + ex.getLineNumber() +
                   ", Column " + ex.getColumnNumber() +
                   "\nMessage: " + msg;

        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }
}

