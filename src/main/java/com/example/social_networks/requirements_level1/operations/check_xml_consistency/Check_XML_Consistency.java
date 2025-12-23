package com.example.social_networks.requirements_level1.operations.check_xml_consistency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.*;
import java.io.StringReader;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Check_XML_Consistency {
    
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

            Document doc = builder.parse(new InputSource(new StringReader(xmlText)));

            return "XML is well-formed \nNo errors detected.";

        } catch (SAXParseException ex) {
            return " XML Parsing Error\nLine " + ex.getLineNumber() +
                    ", Column " + ex.getColumnNumber() +
                    "\nMessage: " + ex.getMessage();
        } catch (Exception ex) {
            return " Error: " + ex.getMessage();
        }
    }
}
