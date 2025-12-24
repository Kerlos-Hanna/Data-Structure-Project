package com.example.social_networks.requirements_level2;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SearchPostsTopics {

    /**
     * Returns a list of posts (as text) that contain the given topic.
     * - It looks for <post> elements.
     * - Inside each post, it searches for <topic> elements (any depth).
     * - If a matching topic is found, it returns the post content (body/text/content if exists),
     *   otherwise it returns the whole post element serialized.
     */
    public List<String> searchTopics(String xml, String topic) {
        List<String> result = new ArrayList<>();
        if (xml == null || topic == null) return result;

        String wanted = topic.trim();
        if (wanted.isEmpty()) return result;

        try {
            Document doc = parseXml(xml);
            if (doc == null) return result;

            NodeList posts = doc.getElementsByTagName("post");
            for (int i = 0; i < posts.getLength(); i++) {
                Element postEl = (Element) posts.item(i);

                if (postHasTopic(postEl, wanted)) {
                    // Prefer common content tags; fallback to whole <post> text.
                    String content = firstTextByTag(postEl, "body");
                    if (content == null) content = firstTextByTag(postEl, "text");
                    if (content == null) content = firstTextByTag(postEl, "content");
                    if (content == null) content = postEl.getTextContent();

                    result.add(content == null ? "" : content.trim());
                }
            }
        } catch (Exception e) {
            // If XML is not well-formed, return empty list (or you can rethrow).
            return result;
        }

        return result;
    }

    // ---------------- Helpers ----------------

    private boolean postHasTopic(Element postEl, String wantedTopic) {
        NodeList topics = postEl.getElementsByTagName("topic");
        for (int j = 0; j < topics.getLength(); j++) {
            Node t = topics.item(j);
            String text = t.getTextContent();
            if (text != null && text.trim().equalsIgnoreCase(wantedTopic)) {
                return true;
            }
        }
        return false;
    }

    private String firstTextByTag(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        String txt = nodes.item(0).getTextContent();
        return (txt == null) ? null : txt.trim();
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setIgnoringComments(true);
        dbf.setCoalescing(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new InputSource(new StringReader(xml)));
    }
}

