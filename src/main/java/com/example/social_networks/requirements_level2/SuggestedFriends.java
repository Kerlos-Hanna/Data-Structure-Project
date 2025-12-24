package com.example.social_networks.requirements_level2;


/**
 *
 * @author jeremy
 */
import java.util.*;
public class SuggestedFriends {

    public static class Graph {
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
    public static class Suggestions {

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
}
