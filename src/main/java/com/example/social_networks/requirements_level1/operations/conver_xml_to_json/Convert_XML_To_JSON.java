package com.example.social_networks.requirements_level1.operations.conver_xml_to_json;
import java.util.Vector;
import java.util.Stack;
public class Convert_XML_To_JSON { // to use this class you need to get a new object from it and pass the xml string and then use convert function and will return string of json 
// like this  Convert_XML_To_JSON jsonstring = new Convert_XML_To_JSON(xml1) ;
  //      String s = jsonstring.convert();
    private String xml;
    public Convert_XML_To_JSON(String xml) {
        this.xml = xml.trim().replace("\n", "").replace("\r", "").replace("\t", "").replace(" " ,"");
    }
    private class Node {
        String tag;
        String text = "";
        Vector<Node> children = new Vector<>();
        Node(String tag) { this.tag = tag; }
        void addChild(Node child) { children.add(child); }
        boolean hasChildren() { return children.size() > 0; }
    }

    private class JSON {
        Vector<String> keys = new Vector<>();
        Vector<String> values = new Vector<>();
        Vector<String> objectKeys = new Vector<>();
        Vector<JSON> objects = new Vector<>();
        Vector<String> arrayKeys = new Vector<>();
        Vector<Vector<JSON>> arrayObjects = new Vector<>();

        void add(String key, String value) {
            keys.add(key);
            values.add(value);
        }

        void addObject(String key, JSON obj) {
            objectKeys.add(key);
            objects.add(obj);
        }

        void addArray(String key, Vector<JSON> arr) {
            arrayKeys.add(key);
            arrayObjects.add(arr);
        }

         private String print(int indent) { // this function is recrussively make the json string from json class
            String s = "";
            s += indent(indent) + "{\n";

            int count = 0;
            int total = keys.size() + objectKeys.size() + arrayKeys.size();

            // Print simple key-value pairs
            for (int i = 0; i < keys.size(); i++, count++) {
                s += indent(indent + 2) + "\"" + keys.get(i) + "\": \"" + values.get(i) + "\"";
                if (count < total - 1){
                    s += ",";
                }
                s += "\n";
            }

            // Print nested objects
            for (int i = 0; i < objectKeys.size(); i++, count++) {
                s += indent(indent + 2) + "\"" + objectKeys.get(i) + "\": " + objects.get(i).print(indent + 2);
                if (count < total - 1) {
                    s += ",";
                }
                s += "\n";
            }

            // Print arrays
            for (int i = 0; i < arrayKeys.size(); i++, count++) {
                s += indent(indent + 2) + "\"" + arrayKeys.get(i) + "\": [\n";

                Vector<JSON> arr = arrayObjects.get(i);

                for (int j = 0; j < arr.size(); j++) {
                    s += arr.get(j).print(indent + 4);
                    if (j < arr.size() - 1) {
                        s += ",";
                    }
                    s += "\n";
                }

                s += indent(indent + 2) + "]";
                if (count < total - 1) {
                    s += ",";
                }
                s += "\n";
            }

            s += indent(indent) + "}";
            return s;
        }

        private String indent(int n) {
            String s = "";
            for (int i = 0; i < n; i++) s += " ";
            return s;
        }

    }

    private Node parseXML() { // this function parses xml into tree 
        Stack<Node> stack = new Stack<>();
        Node root = null;
        int i = 0;
        while (i < xml.length()) {
            if (xml.charAt(i) == '<') {
                            if (xml.charAt(i + 1) == '/') {
                            int close = xml.indexOf('>', i);
                            Node node = stack.pop();
                            if (!stack.isEmpty()) {
                                stack.peek().addChild(node);
                            }
                            i = close + 1;
                            } else {
                            int close = xml.indexOf('>', i);
                            String tag = xml.substring(i + 1, close).trim();
                            Node newNode = new Node(tag);
                            if (root == null) {
                                root = newNode;
                            }
                            stack.push(newNode);
                            i = close + 1;
                                    }
            } else {
                int nextTag = xml.indexOf('<', i);
                if (nextTag == -1) {
                    nextTag = xml.length();
                }
                String text = xml.substring(i, nextTag).trim();
                if (!text.isEmpty() && !stack.isEmpty()) {
                    stack.peek().text += text;
                }
                i = nextTag;
            }
        }
        return root;
    }


    private JSON convertToJSON(Node node) { // this function make a json object from xml tree and you pass to it the root of the tree
        JSON json = new JSON();
        if (!node.hasChildren()) {
            json.add(node.tag, node.text);
            return json;
        }

        // Group children by tag
        Vector<String> childTags = new Vector<>();
        Vector<Vector<Node>> groups = new Vector<>();
        for (Node child : node.children) {
            int idx = childTags.indexOf(child.tag);
            if (idx != -1) {
                groups.get(idx).add(child);
            }
            else {
                childTags.add(child.tag);
                Vector<Node> vec = new Vector<>();
                vec.add(child);
                groups.add(vec);
            }
        }

        for (int i = 0; i < childTags.size(); i++) {
            Vector<Node> group = groups.get(i);
            if (group.size() == 1) {
                Node only = group.get(0);
                if (only.hasChildren()) {
                    json.addObject(only.tag, convertToJSON(only));
                }
                else {
                    json.add(only.tag, only.text);
                }
            } else {
                // multiple nodes → array of objects
                Vector<JSON> arr = new Vector<>();
                for (Node n : group) {
                    arr.add(convertToJSON(n));
                }
                json.addArray(group.get(0).tag, arr);
            }
        }

        return json;
    }

     public String convert() { // this function do the whole operations of the xml to json converstion 
        Node root = parseXML(); // first it fill the tree of the xml 
        if (root == null) // if nothing in the tree it returns just two brackets 
        {
            return "{}";
        }                                   
        JSON json = new JSON(); // if not make a new json object 
        if (!root.hasChildren()) // travel in the tree and add the json
        {
            json.add(root.tag, root.text);
        } else
        {
            json.addObject(root.tag, convertToJSON(root));
        }
        return json.print(0); // use print function to return the json 
    }
}
