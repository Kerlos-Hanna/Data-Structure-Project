package com.example.social_networks.requirements_level1.operations.format_xml;

public class Format_XML {
   public static String formatXML(String xmlText) {
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

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();

        } catch (SAXParseException ex) {
            return "XML Parsing Error\nLine " + ex.getLineNumber() +
                    ", Column " + ex.getColumnNumber() +
                    "\nMessage: " + ex.getMessage();
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
}
}
