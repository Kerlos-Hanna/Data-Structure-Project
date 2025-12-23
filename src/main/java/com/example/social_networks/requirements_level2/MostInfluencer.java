package com.example.social_networks.requirements_level2;

import com.example.social_networks.requirements_level1.parsing_xml.*;
import java.util.Vector;

public class MostInfluencer {
    /**
     * Parses through the XML file (through parsing_xml class) 
     * then returns the ID of the user with the most followers
     * note: does not handle ties
     */
    public static int findMostInfluencer(String xml) {

        Vector<Tag> tags = Parsing_XML.parse(xml);

        Vector<Integer> userIds = new Vector<>();
        Vector<Integer> followerCounts = new Vector<>();

        boolean insideUser = false;
        int currentUserId = -1;
        int currentFollowerCount = 0;

        for (Tag tag : tags) {

            // Start of a user
            if (tag.isOpening && tag.name.equals("user")) {
                insideUser = true;
                currentUserId = -1;
                currentFollowerCount = 0;
            }

            // Save user ID
            else if (insideUser && tag.name.equals("id") && tag.isOpening && currentUserId == -1) {
                currentUserId = Integer.parseInt(tag.innerText);
            }

            // Add 1 to follower count for each follower tag
            else if (insideUser && tag.name.equals("follower") && tag.isOpening) {
                currentFollowerCount++;
            }

            // End of user tag, save data
            else if (tag.isClosing && tag.name.equals("user")) {
                insideUser = false;

                userIds.add(currentUserId);
                followerCounts.add(currentFollowerCount);
            }
        }

        int maxFollowers = -1;
        int influencerId = -1;

        for (int i = 0; i < userIds.size(); i++) {
            if (followerCounts.get(i) > maxFollowers) {
                maxFollowers = followerCounts.get(i);
                influencerId = userIds.get(i);
            }
        }

        return influencerId;
    }
}
