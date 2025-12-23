package com.example.social_networks.requirements_level2;

import java.util.*;

public class MostActiveUser {

    // Level 2-style Graph (adjacency list)
    static class Graph {
        private final List<Integer>[] adj;

        @SuppressWarnings("unchecked")
        Graph(int n) {
            if (n < 0) throw new IllegalArgumentException("n must be >= 0");
            adj = (List<Integer>[]) new List[n];
            for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        }

        int size() {
            return adj.length;
        }

        void addEdge(int u, int v) {
            checkNode(u);
            checkNode(v);
            adj[u].add(v); // directed edge u -> v
        }

        List<Integer> neighbors(int u) {
            checkNode(u);
            return Collections.unmodifiableList(adj[u]);
        }

        private void checkNode(int u) {
            if (u < 0 || u >= adj.length) throw new IndexOutOfBoundsException("node " + u);
        }
    }

    // Most Active User (by OUT-degree: who follows/messages the most, depending on your model)
    static int mostActive(Graph g) {
        if (g.size() == 0) return -1;

        int bestUser = 0;
        int bestDegree = -1;

        for (int u = 0; u < g.size(); u++) {
            int deg = g.neighbors(u).size();
            // tie-breaker: smallest id
            if (deg > bestDegree || (deg == bestDegree && u < bestUser)) {
                bestDegree = deg;
                bestUser = u;
            }
        }
        return bestUser;
    }

    // Alternative: Most Followed / Most Incoming (by IN-degree)
    static int mostFollowed(Graph g) {
        if (g.size() == 0) return -1;

        int[] indeg = new int[g.size()];
        for (int u = 0; u < g.size(); u++) {
            for (int v : g.neighbors(u)) indeg[v]++;
        }

        int bestUser = 0;
        int bestDegree = -1;
        for (int u = 0; u < g.size(); u++) {
            if (indeg[u] > bestDegree || (indeg[u] == bestDegree && u < bestUser)) {
                bestDegree = indeg[u];
                bestUser = u;
            }
        }
        return bestUser;
    }

    // Quick demo (optional)
    public static void main(String[] args) {
        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(3, 2);
        g.addEdge(3, 4);

        System.out.println("Most active (out-degree): " + mostActive(g));
        System.out.println("Most followed (in-degree): " + mostFollowed(g));
    }
}
