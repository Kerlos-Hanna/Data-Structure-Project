package com.example.social_networks.requirements_level1.parsing_xml;
import java.util.Stack;
import java.util.Vector;

public class Parsing_XML {
    static Stack<Tag> stack = new Stack<>();

    public static Vector<Tag> parse(String xml) {
        stack.clear();
        Vector<Tag> tagVec = new Vector<>();

        for (int i = 0; i < xml.length(); i++) {
            if (xml.charAt(i) == '<') {
                Object[] result = extractTag(xml, i);
                Tag tag = (Tag) result[0];
                i = (int) result[1];

                if (tag == null) { // missing '>'
                    System.out.println("Error: Malformed tag detected. Parsing stopped.");
                    return new Vector<>();
                }

                tagVec.add(tag);

                if (tag.isOpening) {
                    stack.push(tag);
                } else {
                    if (!stack.isEmpty() && tag.name.equals(stack.peek().name)) {
                        stack.peek().innerText = stack.peek().innerText.trim();
                        stack.pop();
                    } else {
                        System.out.println("Error: Unexpected closing tag </" + tag.name + ">");
                        return new Vector<>();
                    }
                }
            } else if (xml.charAt(i) == '>') {
                System.out.println("Error: '>' found without matching '<'");
                return new Vector<>();
            } else {
                if (!stack.isEmpty()) {
                    stack.peek().innerText += xml.charAt(i);
                } else if (!Character.isWhitespace(xml.charAt(i))) {
                    System.out.println("Error: Text outside of any tag: '" + xml.charAt(i) + "'");
                    return new Vector<>();
                }

            }
        }
        while (!stack.isEmpty()) {
            Tag t = stack.pop();
            t.innerText = t.innerText.trim();
            System.out.println("Error: Missing closing tag for <" + t.name + ">");
        }

        return tagVec;
    }

    static void checkErrors() {
        if (!stack.isEmpty()) {
            System.out.println("Error: Missing closing tags for the following elements:");
            while (!stack.isEmpty()) {
                System.out.println("<" + stack.pop().name + ">");
            }
        }
    }

    static Object[] extractTag(String xml, int start) {
        Tag tag = new Tag();
        start++;

        if (start < xml.length() && xml.charAt(start) == '/') {
            tag.isOpening = false;
            tag.isClosing = true;
            start++;
        } else {
            tag.isOpening = true;
            tag.isClosing = false;
        }

        StringBuilder name = new StringBuilder();
        while (start < xml.length() && xml.charAt(start) != '>') {
            if (xml.charAt(start) == '<') {
                System.out.println("Error: '<' found inside a tag at position " + start);
                return new Object[]{null, start};
            }
            name.append(xml.charAt(start));
            start++;
        }

        if (start >= xml.length()) {
            System.out.println("Error: Missing '>' for tag starting at position " + (start - name.length() - 1));
            return new Object[]{null, start};
        }

        tag.name = name.toString().trim();
        start++;

        return new Object[]{tag, start - 1};
    }

    static void printTags(Vector<Tag> tags) {
        if (tags.isEmpty()) {
            System.out.println("No tags parsed.");
            return;
        }
        for (Tag t : tags) {
            System.out.println("Tag name: " + t.name
                    + ", Opening: " + t.isOpening
                    + ", Inner text: \"" + t.innerText + "\"");
        }
    }
}

