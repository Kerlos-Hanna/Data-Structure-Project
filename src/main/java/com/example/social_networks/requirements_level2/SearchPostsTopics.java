package com.example.social_networks.requirements_level2;

import java.util.Vector;

public class SearchPostsTopics {

    /**
     * Return all posts that contain a specific topic
     */
    public static Vector<Post> searchPostsByTopic(Vector<User> users, String topic) {
        Vector<Post> result = new Vector<>();

        if (users == null || topic == null || topic.isBlank()) {
            return result;
        }

        String targetTopic = topic.trim().toLowerCase();

        for (User user : users) {
            if (user == null || user.posts == null) continue;

            for (Post post : user.posts) {
                if (post == null || post.topics == null) continue;

                for (String t : post.topics) {
                    if (t != null && t.trim().toLowerCase().equals(targetTopic)) {
                        result.add(post);
                        break; // avoid duplicate add for same post
                    }
                }
            }
        }

        return result;
    }
}
