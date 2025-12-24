package com.example.social_networks.requirements_level2;

import java.util.Vector;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Ahmad Aledlbi
 */
import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.util.mxCellRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class DrawGraph {

    public static Vector<User> buildUsers(Vector<Tag> tags) {
        Vector<User> users = new Vector<>();
        User currentUser = null;
        Post currentPost = null;

        boolean inFollowers = false;
        boolean inFollower = false;
        boolean inPosts = false;
        boolean inPost = false;
        boolean inTopics = false;

        for (Tag t : tags) {
            if (t.isOpening && t.name.equals("user")) {
                currentUser = new User();
            } else if (t.isClosing && t.name.equals("user")) {
                if (currentUser != null) {
                    users.add(currentUser);
                    currentUser = null;
                }
            } else if (currentUser != null) {
                // Helper to handle text safely
                String text = (t.innerText != null) ? t.innerText.trim() : "";

                if (t.isOpening && t.name.equals("id") && !inFollowers && !inPosts) {
                    if (!text.isEmpty()) {
                        currentUser.id = Integer.parseInt(text);
                    }
                } else if (t.isOpening && t.name.equals("name")) {
                    currentUser.name = text;
                } else if (t.isOpening && t.name.equals("followers")) {
                    inFollowers = true;
                } else if (t.isClosing && t.name.equals("followers")) {
                    inFollowers = false;
                } else if (t.isOpening && t.name.equals("follower")) {
                    inFollower = true;
                } else if (t.isClosing && t.name.equals("follower")) {
                    inFollower = false;
                } else if (t.isOpening && t.name.equals("id") && inFollowers && inFollower) {
                    if (!text.isEmpty()) {
                        currentUser.followers.add(Integer.parseInt(text));
                    }
                } else if (t.isOpening && t.name.equals("posts")) {
                    inPosts = true;
                } else if (t.isClosing && t.name.equals("posts")) {
                    inPosts = false;
                } else if (t.isOpening && t.name.equals("post")) {
                    inPost = true;
                    currentPost = new Post();
                } else if (t.isClosing && t.name.equals("post")) {
                    if (currentPost != null) {
                        currentUser.posts.add(currentPost);
                        currentPost = null;
                    }
                    inPost = false;
                } else if (inPost && currentPost != null) {
                    if (t.isOpening && t.name.equals("body")) {
                        currentPost.body = text;
                    } else if (t.isOpening && t.name.equals("topics")) {
                        inTopics = true;
                    } else if (t.isClosing && t.name.equals("topics")) {
                        inTopics = false;
                    } else if (t.isOpening && t.name.equals("topic") && inTopics) {
                        if (!text.isEmpty()) {
                            currentPost.topics.add(text);
                        }
                    }
                }
            }
        }
        return users;
    }

    private static int findUserIndex(Vector<User> users, int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public static Graph buildGraph(Vector<User> users) {
        Graph g = new Graph(users.size());

        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            for (int followerId : currentUser.followers) {

                int followerIndex = findUserIndex(users, followerId);

                if (followerIndex != -1) {
                    g.addEdge(followerIndex, i);
                }
            }
        }
        return g;
    }

    public static void visualizeGraph(Vector<User> users, Graph g, String outputFile) throws IOException {

        DirectedMultigraph<String, DefaultEdge> graph = new DirectedMultigraph<>(DefaultEdge.class);

        for (User u : users) {
            graph.addVertex(u.name);
        }

        for (int i = 0; i < g.adj.size(); i++) {
            String sourceName = users.get(i).name;
            Vector<Integer> neighbors = g.neighbors(i);

            for (Integer neighborIndex : neighbors) {
                String targetName = users.get(neighborIndex).name;
                graph.addEdge(sourceName, targetName);
            }
        }

        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);

        graphAdapter.getEdgeToCellMap().values().forEach(cell -> cell.setValue(""));

        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(
                graphAdapter, null, 2, Color.WHITE, true, null);

        ImageIO.write(image, "JPG", new File(outputFile));
        System.out.println("Graph saved as " + outputFile);
    }

    private static User findUserById(Vector<User> users, int id) {
        for (User u : users) {
            if (u.id == id) {
                return u;
            }
        }
        return null;
    }
}

