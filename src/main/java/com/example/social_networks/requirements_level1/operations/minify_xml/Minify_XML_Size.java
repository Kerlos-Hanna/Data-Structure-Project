package com.example.social_networks.requirements_level1.operations.minify_xml;

public class Minify_XML_Size {

    private String xml;

    public Minify_XML_Size(String xml) {
        if (xml == null) {
            throw new IllegalArgumentException("XML input cannot be null");
        }
        this.xml = xml;
    }

    public String minify_size() {
        // Removes whitespace
        return xml.replaceAll(">\\s+<", "><");
    }
    
     public static void main(String[] args) {
        /////////////////////////////////////////////////////////////////
        if (args.length < 3 || !args[0].equals("mini")) {
            System.out.println("Usage: xml_editor mini -i input.xml -o output.xml");
            return;
        }

        String input = null;
        String output = null;

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-i"))
                input = args[++i];
            else if (args[i].equals("-o"))
                output = args[++i];
        }

        if (input == null || output == null) {
            System.out.println("Missing input or output file.");
            return;
        }
        
        
        //////////////////////////remove the above block to test////////////////
        try {
            /*
            file loading here
            */
            String data = "<root>test</root> <path> </path>"; //put input string here;
            Minify_XML_Size minifier = new Minify_XML_Size(data);
            data = minifier.minify_size();
            /*
            file creation
            */
            System.out.println(data);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        
     }
}
