package com.example.social_networks.requirements_level2;


/**
 *
 * @author jeremy
 */
import java.util.*;
public class SuggestedFriends {

    static class Graph {
        // user -> users he follows
        private Map<Integer, Set<Integer>> adj = new HashMap<>();

        public void addEdge(int u, int v) {
            adj.putIfAbsent(u, new HashSet<>());
            adj.get(u).add(v);
        }

        public Set<Integer> neighbors(int u) {
            return adj.getOrDefault(u, Collections.emptySet());
        }
    }

    // -------- Suggestions Class --------//
    static class Suggestions {

        public List<Integer> suggest(Graph g, int user) {

            // Users that "user" already follows
            Set<Integer> direct = g.neighbors(user);

            // Suggested users (no duplicates)
            Set<Integer> suggested = new HashSet<>();

            for (int friend : direct) {
                for (int candidate : g.neighbors(friend)) {

                    if (candidate == user) continue;          // don't suggest self
                    if (direct.contains(candidate)) continue; // don't suggest already followed accounts

                    suggested.add(candidate);
                }
            }

            return new ArrayList<>(suggested);
        }
    }

    // -------- Main Method --------//
    public static void main(String[] args) {

        Graph g = new Graph();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        g.addEdge(6, 3);
        g.addEdge(6, 2);
        Suggestions s = new Suggestions();

        List<Integer> result = s.suggest(g, 6);

        System.out.println("Suggested followers for user 6:");
        System.out.println(result);
    }
}


