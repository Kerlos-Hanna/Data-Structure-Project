package com.example.social_networks.requirements_level2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutualFriends {

    /**
     * Logic for Person 5: Finds common followers/friends between two users.
     * * @param graph The custom Graph structure 
     * @param u ID of the first user
     * @param v ID of the second user
     * @return List of mutual friend IDs
     */
    public static List<Integer> getMutualFriends(MostActiveUser.Graph graph, int u, int v) {
        // 1. Get the list of neighbors (friends/followers) for both users 
        List<Integer> friendsU = graph.neighbors(u);
        List<Integer> friendsV = graph.neighbors(v);

        List<Integer> mutuals = new ArrayList<>();

        // 2. Sort both lists to allow for an efficient linear comparison
        Collections.sort(friendsU);
        Collections.sort(friendsV);

        // 3. Two-pointer approach to find the intersection (Manual Logic) 
        int i = 0, j = 0;
        while (i < friendsU.size() && j < friendsV.size()) {
            if (friendsU.get(i).equals(friendsV.get(j))) {
                mutuals.add(friendsU.get(i));
                i++;
                j++;
            } else if (friendsU.get(i) < friendsV.get(j)) {
                i++;
            } else {
                j++;
            }
        }

        return mutuals;
    }
}
