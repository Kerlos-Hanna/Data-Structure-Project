package com.example.social_networks.requirements_level2;

public class MostInfluencer {
    /**
     * Finds the user with the most followers using graph indegree.
     * returns the ID of that user
     * note: does not handle ties
     */
    public static int findMostInfluencer(Graph g) {

        int n = g.adj.size();
        int[] indegree = new int[n];

        // Calculate indegree of each node
        for (int u = 0; u < n; u++) {
            for (int v : g.adj.get(u)) {
                indegree[v]++;
            }
        }

        int maxIndegree = -1;
        int influencerId = -1;
        
        // Find node with highest indegree
        for (int i = 0; i < n; i++) {
            if (indegree[i] > maxIndegree) {
                maxIndegree = indegree[i];
                influencerId = i;
            }
        }
        return influencerId;
    }
}
