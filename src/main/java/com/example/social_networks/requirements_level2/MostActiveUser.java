package com.example.social_networks.requirements_level2;

public class MostActiveUser {

    // Most Active = highest OUT degree
    public static int mostActive(Graph g) {

        if (g == null || g.adj.isEmpty()) return -1;

        int bestUser = 0;
        int bestDegree = -1;

        for (int u = 0; u < g.adj.size(); u++) {
            int deg = g.neighbors(u).size();
            if (deg > bestDegree) {
                bestDegree = deg;
                bestUser = u;
            }
        }
        return bestUser;
    }
}
