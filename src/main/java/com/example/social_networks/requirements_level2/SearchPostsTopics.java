package com.example.social_networks.requirements_level2;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.mycompany.dsa.Parsing_XML ;
import com.mycompany.dsa.Tag;

public class SearchPostsTopics  {

    // Return all topics (unique, in same order)
    public List<String> listTopics(String xml) {
        List<String> result = new ArrayList<>();
        if (xml == null || xml.isBlank()) return result;

        Vector<Tag> tags = Parsing_XML .parse(xml);
        if (tags == null || tags.isEmpty()) return result;

        Set<String> unique = new LinkedHashSet<>();

        for (Tag t : tags) {
            if (t == null || t.name == null) continue;

            // Your parser stores innerText in the OPENING tag object
            if (t.isOpening && t.name.trim().equalsIgnoreCase("topic")) {
                String topicText = (t.innerText == null) ? "" : t.innerText.trim();
                if (!topicText.isEmpty()) unique.add(topicText);
            }
        }

        result.addAll(unique);
        return result;
    }

    // Quick test
    public static void main(String[] args) {
        String xml =
            "<network>" +
              "<post><body>Hello</body><topics><topic>Sports</topic><topic>News</topic></topics></post>" +
              "<post><body>Java tips</body><topics><topic>Tech</topic><topic>News</topic></topics></post>" +
            "</network>";

        SearchPostsTopics  lt = new SearchPostsTopics ();
        System.out.println(lt.listTopics(xml)); // [Sports, News, Tech]
    }
}
