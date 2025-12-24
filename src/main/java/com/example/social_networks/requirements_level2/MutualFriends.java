package com.example.social_networks.requirements_level2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class MutualFriends {

    /**
     * Finds mutual friends/followers between two users
     *
     * @param graph your custom Graph (from DrawGraph)
     * @param u index of first user
     * @param v index of second user
     * @return list of mutual friend indices
     */
    public static List<Integer> getMutualFriends(Graph graph, int u, int v) {

        // Defensive copy (DO NOT mutate graph)
        Vector<Integer> friendsU = new Vector<>(graph.neighbors(u));
        Vector<Integer> friendsV = new Vector<>(graph.neighbors(v));

        List<Integer> mutuals = new ArrayList<>();

        // Sort copies
        Collections.sort(friendsU);
        Collections.sort(friendsV);

        // Two-pointer intersection
        int i = 0, j = 0;
        while (i < friendsU.size() && j < friendsV.size()) {
            int a = friendsU.get(i);
            int b = friendsV.get(j);

            if (a == b) {
                mutuals.add(a);
                i++;
                j++;
            } else if (a < b) {
                i++;
            } else {
                j++;
            }
        }

        return mutuals;
    }
}
