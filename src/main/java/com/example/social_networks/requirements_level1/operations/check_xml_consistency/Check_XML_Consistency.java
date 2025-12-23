package com.example.social_networks.requirements_level1.operations.check_xml_consistency;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Check_XML_Consistency {

    public static class checkXMLConsistency {
        public final int line;
        public final String message;

        public checkXMLConsistency(int line, String message) {
            this.line = line;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Line " + line + ": " + message;
        }
    }

    private static class OpenTag {
        final String name;
        final int line;

        OpenTag(String name, int line) {
            this.name = name;
            this.line = line;
        }
    }

    private static class TagToken {
        final int startIndex;
        final int endExclusive;
        final int line;
        final String raw;
        final String name;
        final boolean isClosing;
        final boolean isSelfClosing;
        final boolean valid;

        TagToken(int startIndex, int endExclusive, int line, String raw,
                 String name, boolean isClosing, boolean isSelfClosing, boolean valid) {
            this.startIndex = startIndex;
            this.endExclusive = endExclusive;
            this.line = line;
            this.raw = raw;
            this.name = name;
            this.isClosing = isClosing;
            this.isSelfClosing = isSelfClosing;
            this.valid = valid;
        }
    }

    public List<checkXMLConsistency> validate(String xml) {
        List<checkXMLConsistency> errors = new ArrayList<>();
        Deque<OpenTag> stack = new ArrayDeque<>();

        int i = 0;
        int line = 1;
        final int n = xml.length();

        while (i < n) {
            char c = xml.charAt(i);

            if (c == '\n') {
                line++;
                i++;
                continue;
            }

            if (c != '<') {
                i++;
                continue;
            }

            int specialEnd = findAndSkipSpecial(xml, i);
            if (specialEnd > i) {
                line += countNewlines(xml, i, specialEnd);
                i = specialEnd;
                continue;
            }

            TagToken tok = readTagToken(xml, i, line);
            if (!tok.valid) {
                errors.add(new checkXMLConsistency(tok.line, "Malformed tag: missing closing '>'"));
                break;
            }

            line += countNewlines(xml, tok.startIndex, tok.endExclusive);
            i = tok.endExclusive;

            if (tok.name.isEmpty()) continue;

            if (!tok.isClosing) {
                if (!tok.isSelfClosing) {
                    stack.push(new OpenTag(tok.name, tok.line));
                }
                continue;
            }

            if (stack.isEmpty()) {
                errors.add(new checkXMLConsistency(tok.line,
                        "Extra closing tag </" + tok.name + "> with no matching opening tag."));
                continue;
            }

            OpenTag top = stack.peek();
            if (matchTags(top.name, tok.name)) {
                stack.pop();
                continue;
            }

            int depthToMatch = findInStack(stack, tok.name);

            if (depthToMatch == -1) {
                errors.add(new checkXMLConsistency(tok.line,
                        "Mismatched closing tag </" + tok.name + ">. Expected </" + top.name +
                                "> to close <" + top.name + "> opened at line " + top.line + "."));
                continue;
            }

            for (int k = 0; k < depthToMatch; k++) {
                OpenTag missing = stack.pop();
                errors.add(new checkXMLConsistency(tok.line,
                        "Missing closing tag </" + missing.name + "> before </" + tok.name +
                                "> (opened at line " + missing.line + ")."));
            }

            stack.pop();
        }

        while (!stack.isEmpty()) {
            OpenTag unclosed = stack.pop();
            errors.add(new checkXMLConsistency(unclosed.line,
                    "Missing closing tag </" + unclosed.name + "> for <" + unclosed.name + "> opened here."));
        }

        return errors;
    }

    public String autoFix(String xml) {
        StringBuilder out = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();

        int i = 0;
        int line = 1;
        final int n = xml.length();

        while (i < n) {
            char c = xml.charAt(i);

            if (c == '\n') {
                line++;
                out.append(c);
                i++;
                continue;
            }

            if (c != '<') {
                out.append(c);
                i++;
                continue;
            }

            int specialEnd = findAndSkipSpecial(xml, i);
            if (specialEnd > i) {
                out.append(xml, i, specialEnd);
                line += countNewlines(xml, i, specialEnd);
                i = specialEnd;
                continue;
            }

            TagToken tok = readTagToken(xml, i, line);
            if (!tok.valid) {
                out.append(xml.substring(i));
                break;
            }

            line += countNewlines(xml, tok.startIndex, tok.endExclusive);

            if (tok.name.isEmpty()) {
                out.append(tok.raw);
                i = tok.endExclusive;
                continue;
            }

            if (!tok.isClosing) {
                out.append(tok.raw);
                if (!tok.isSelfClosing) stack.push(tok.name);
                i = tok.endExclusive;
                continue;
            }

            if (stack.isEmpty()) {
                i = tok.endExclusive; // drop extra closing
                continue;
            }

            String top = stack.peek();
            if (matchTags(top, tok.name)) {
                stack.pop();
                out.append(tok.raw);
                i = tok.endExclusive;
                continue;
            }

            int depthToMatch = findInNameStack(stack, tok.name);
            if (depthToMatch == -1) {
                i = tok.endExclusive; // drop closing with no opener
                continue;
            }

            for (int k = 0; k < depthToMatch; k++) {
                String missing = stack.pop();
                out.append("</").append(missing).append(">");
            }

            stack.pop();
            out.append(tok.raw);
            i = tok.endExclusive;
        }

        while (!stack.isEmpty()) {
            out.append("</").append(stack.pop()).append(">");
        }

        return out.toString();
    }

    private boolean matchTags(String openTag, String closeTag) {
        return openTag.equals(closeTag);
    }

    private static int findAndSkipSpecial(String xml, int i) {
        if (xml.startsWith("<!--", i)) {
            int end = xml.indexOf("-->", i + 4);
            return (end == -1) ? xml.length() : end + 3;
        }
        if (xml.startsWith("<![CDATA[", i)) {
            int end = xml.indexOf("]]>", i + 9);
            return (end == -1) ? xml.length() : end + 3;
        }
        if (xml.startsWith("<?", i)) {
            int end = xml.indexOf("?>", i + 2);
            return (end == -1) ? xml.length() : end + 2;
        }
        if (xml.startsWith("<!", i)) {
            int end = xml.indexOf(">", i + 2);
            return (end == -1) ? xml.length() : end + 1;
        }
        return i;
    }

    private static int countNewlines(String s, int start, int endExclusive) {
        int count = 0;
        for (int k = start; k < endExclusive && k < s.length(); k++) {
            if (s.charAt(k) == '\n') count++;
        }
        return count;
    }

    private static TagToken readTagToken(String xml, int start, int startLine) {
        int n = xml.length();
        int j = start + 1;

        boolean inSingle = false;
        boolean inDouble = false;
        boolean foundEnd = false;

        while (j < n) {
            char ch = xml.charAt(j);

            if (ch == '"' && !inSingle) inDouble = !inDouble;
            else if (ch == '\'' && !inDouble) inSingle = !inSingle;
            else if (ch == '>' && !inSingle && !inDouble) {
                foundEnd = true;
                j++;
                break;
            }
            j++;
        }

        if (!foundEnd) {
            String raw = xml.substring(start);
            return new TagToken(start, n, startLine, raw, "", false, false, false);
        }

        String raw = xml.substring(start, j);
        String inner = raw.substring(1, raw.length() - 1).trim();

        if (inner.isEmpty()) return new TagToken(start, j, startLine, raw, "", false, false, true);

        if (inner.startsWith("!--") || inner.startsWith("![CDATA[") || inner.startsWith("?") || inner.startsWith("!")) {
            return new TagToken(start, j, startLine, raw, "", false, false, true);
        }

        boolean isClosing = inner.startsWith("/");
        String body = isClosing ? inner.substring(1).trim() : inner;

        boolean isSelfClosing = false;
        if (!isClosing) {
            String trimmed = body.trim();
            if (trimmed.endsWith("/")) {
                isSelfClosing = true;
                body = trimmed.substring(0, trimmed.length() - 1).trim();
            }
        }

        String name = extractName(body);
        return new TagToken(start, j, startLine, raw, name, isClosing, isSelfClosing, true);
    }

    private static String extractName(String body) {
        if (body == null) return "";
        body = body.trim();
        if (body.isEmpty()) return "";

        int k = 0;
        while (k < body.length() && !Character.isWhitespace(body.charAt(k))) k++;
        return body.substring(0, k).trim();
    }

    private static int findInStack(Deque<OpenTag> stack, String tagName) {
        int depth = 0;
        for (OpenTag t : stack) {
            if (t.name.equals(tagName)) return depth;
            depth++;
        }
        return -1;
    }

    private static int findInNameStack(Deque<String> stack, String tagName) {
        int depth = 0;
        for (String t : stack) {
            if (t.equals(tagName)) return depth;
            depth++;
        }
        return -1;
    }


}
