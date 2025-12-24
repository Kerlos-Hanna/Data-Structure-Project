package com.example.social_networks.requirements_level2;

import java.util.Vector;

public class Graph {

    Vector<Vector<Integer>> adj;

    public Graph(int n) {
        adj = new Vector<>();
        for (int i = 0; i < n; i++) {
            adj.add(new Vector<>());
        }
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    public Vector<Integer> neighbors(int u) {
        return adj.get(u);
    }
}
