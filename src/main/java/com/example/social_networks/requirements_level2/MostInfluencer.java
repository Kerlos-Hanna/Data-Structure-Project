package com.example.social_networks.requirements_level2;

import com.example.social_networks.requirements_level1.parsing_xml.*;
import java.util.Vector;

public class MostInfluencer {

    // ===== Result INNER CLASS =====
    public static class Result {
        public int id;
        public String name;
        public int followers;

        public Result(int id, String name, int followers) {
            this.id = id;
            this.name = name;
            this.followers = followers;
        }
    }

    /**
     * Parses XML and returns the most influencer user
     * (id + name + number of followers)
     */
    public static Result findMostInfluencer(String xml) {

        Vector<Tag> tags = Parsing_XML.parse(xml);

        Vector<Integer> userIds = new Vector<>();
        Vector<String> userNames = new Vector<>();
        Vector<Integer> followerCounts = new Vector<>();

        boolean insideUser = false;
        int currentUserId = -1;
        String currentUserName = "";
        int currentFollowerCount = 0;

        for (Tag tag : tags) {

            // Start of user
            if (tag.isOpening && tag.name.equals("user")) {
                insideUser = true;
                currentUserId = -1;
                currentUserName = "";
                currentFollowerCount = 0;
            }

            // User ID
            else if (insideUser && tag.name.equals("id") && tag.isOpening && currentUserId == -1) {
                currentUserId = Integer.parseInt(tag.innerText.trim());
            }

            // User Name
            else if (insideUser && tag.name.equals("name") && tag.isOpening) {
                currentUserName = tag.innerText.trim();
            }

            // Count followers
            else if (insideUser && tag.name.equals("follower") && tag.isOpening) {
                currentFollowerCount++;
            }

            // End of user
            else if (tag.isClosing && tag.name.equals("user")) {
                insideUser = false;

                userIds.add(currentUserId);
                userNames.add(currentUserName);
                followerCounts.add(currentFollowerCount);
            }
        }

        int maxFollowers = -1;
        int influencerIndex = -1;

        for (int i = 0; i < userIds.size(); i++) {
            if (followerCounts.get(i) > maxFollowers) {
                maxFollowers = followerCounts.get(i);
                influencerIndex = i;
            }
        }

        if (influencerIndex == -1) {
            return null;
        }

        return new Result(
                userIds.get(influencerIndex),
                userNames.get(influencerIndex),
                followerCounts.get(influencerIndex)
        );
    }
}
