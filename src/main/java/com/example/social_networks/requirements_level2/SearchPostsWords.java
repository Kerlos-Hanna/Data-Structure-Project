package com.example.social_networks.requirements_level2;

import java.util.List;
import java.util.Vector;

public class SearchPostsWords {

    private SearchPostsWords() {}

    public static List<String> searchPostsByWord(String xml, String word) {
        List<String> matches = new Vector<>();
        if (xml == null || word == null) return matches;

        String wanted = word.trim();
        if (wanted.isEmpty()) return matches;

        String wantedLower = wanted.toLowerCase();
        String lowerXml = xml.toLowerCase();

        int from = 0;
        while (true) {
            int postStart = indexOfStartTag(lowerXml, "post", from);
            if (postStart == -1) break;

            int postStartEnd = xml.indexOf('>', postStart);
            if (postStartEnd == -1) break;

            if (postStartEnd > 0 && xml.charAt(postStartEnd - 1) == '/') {
                from = postStartEnd + 1;
                continue;
            }

            int postEnd = lowerXml.indexOf("</post>", postStartEnd + 1);
            if (postEnd == -1) break;

            String inner = xml.substring(postStartEnd + 1, postEnd);
            String postText = extractPostText(inner);

            if (containsWholeWord(postText, wantedLower)) {
                matches.add(postText);
            }

            from = postEnd + "</post>".length();
        }

        return matches;
    }

    public static String formatAsText(List<String> posts) {
        if (posts == null || posts.isEmpty()) {
            return "No posts found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(posts.size()).append(" post(s):\n");
        for (int i = 0; i < posts.size(); i++) {
            sb.append("[").append(i + 1).append("] ").append(posts.get(i)).append("\n");
        }
        return sb.toString();
    }

    private static int indexOfStartTag(String lowerXml, String tagName, int from) {
        String needle = "<" + tagName;
        int idx = lowerXml.indexOf(needle, from);
        while (idx != -1) {
            int after = idx + needle.length();
            if (after < lowerXml.length()) {
                char c = lowerXml.charAt(after);
                if (c == '>' || c == '/' || Character.isWhitespace(c)) {
                    return idx;
                }
            } else {
                return idx;
            }
            idx = lowerXml.indexOf(needle, after);
        }
        return -1;
    }

    private static String extractPostText(String postInner) {
        if (postInner == null) return "";

        String body = extractFirstTagInner(postInner, "body");
        if (body == null) body = extractFirstTagInner(postInner, "text");
        if (body == null) body = extractFirstTagInner(postInner, "content");

        if (body != null) {
            return normalizeWhitespace(stripTags(body));
        }

        String withoutTopics = removeAllTagBlocks(postInner, "topics");
        return normalizeWhitespace(stripTags(withoutTopics));
    }

    private static String extractFirstTagInner(String input, String tagName) {
        String lower = input.toLowerCase();
        int start = indexOfStartTag(lower, tagName, 0);
        if (start == -1) return null;

        int startEnd = input.indexOf('>', start);
        if (startEnd == -1) return null;

        if (startEnd > 0 && input.charAt(startEnd - 1) == '/') return null;

        String close = "</" + tagName.toLowerCase() + ">";
        int end = lower.indexOf(close, startEnd + 1);
        if (end == -1) return null;

        return input.substring(startEnd + 1, end);
    }

    private static String removeAllTagBlocks(String input, String tagName) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder out = new StringBuilder();
        String lower = input.toLowerCase();

        String openNeedle = "<" + tagName.toLowerCase();
        String closeNeedle = "</" + tagName.toLowerCase() + ">";

        int i = 0;
        while (i < input.length()) {
            int open = lower.indexOf(openNeedle, i);
            if (open == -1) {
                out.append(input, i, input.length());
                break;
            }

            int afterOpenName = open + openNeedle.length();
            if (afterOpenName < lower.length()) {
                char boundary = lower.charAt(afterOpenName);
                if (!(boundary == '>' || boundary == '/' || Character.isWhitespace(boundary))) {
                    out.append(input, i, afterOpenName);
                    i = afterOpenName;
                    continue;
                }
            }

            out.append(input, i, open);

            int openEnd = input.indexOf('>', open);
            if (openEnd == -1) {
                out.append(input.substring(open));
                break;
            }

            if (openEnd > 0 && input.charAt(openEnd - 1) == '/') {
                i = openEnd + 1;
                continue;
            }

            int close = lower.indexOf(closeNeedle, openEnd + 1);
            if (close == -1) {
                break;
            }

            i = close + closeNeedle.length();
        }

        return out.toString();
    }

    private static String stripTags(String s) {
        if (s == null || s.isEmpty()) return "";

        StringBuilder out = new StringBuilder(s.length());
        boolean insideTag = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (insideTag) {
                if (c == '>') {
                    insideTag = false;
                    out.append(' ');
                }
            } else {
                if (c == '<') {
                    insideTag = true;
                    out.append(' ');
                } else {
                    out.append(c);
                }
            }
        }

        return out.toString();
    }

    private static String normalizeWhitespace(String s) {
        if (s == null || s.isEmpty()) return "";

        StringBuilder out = new StringBuilder(s.length());
        boolean prevSpace = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean isSpace = Character.isWhitespace(c);

            if (isSpace) {
                if (!prevSpace) out.append(' ');
                prevSpace = true;
            } else {
                out.append(c);
                prevSpace = false;
            }
        }

        int start = 0;
        while (start < out.length() && out.charAt(start) == ' ') start++;
        int end = out.length();
        while (end > start && out.charAt(end - 1) == ' ') end--;

        return out.substring(start, end);
    }

    private static boolean containsWholeWord(String text, String wantedLower) {
        if (text == null) return false;

        String lower = text.toLowerCase();
        int from = 0;
        int len = wantedLower.length();

        while (true) {
            int idx = lower.indexOf(wantedLower, from);
            if (idx == -1) return false;

            int before = idx - 1;
            int after = idx + len;

            boolean leftOk = (before < 0) || !isWordChar(lower.charAt(before));
            boolean rightOk = (after >= lower.length()) || !isWordChar(lower.charAt(after));

            if (leftOk && rightOk) return true;

            from = idx + 1;
        }
    }

    private static boolean isWordChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}


