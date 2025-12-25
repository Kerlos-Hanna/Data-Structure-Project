package com.example.social_networks;

import com.example.social_networks.requirements_level1.operations.check_xml_consistency.Check_XML_Consistency;
import com.example.social_networks.requirements_level1.operations.compress_xml.Compress_XML;
import com.example.social_networks.requirements_level1.operations.conver_xml_to_json.Convert_XML_To_JSON;
import com.example.social_networks.requirements_level1.operations.decompress_xml.Decompression_XML;
import com.example.social_networks.requirements_level1.operations.format_xml.Format_XML;
import com.example.social_networks.requirements_level1.operations.minify_xml.Minify_XML_Size;
import com.example.social_networks.requirements_level1.parsing_xml.Parsing_XML;
import com.example.social_networks.requirements_level1.parsing_xml.Tag;
import com.example.social_networks.requirements_level2.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


public class CommandLine {
   public static void main(String[] args) {
       new CommandLine().run(args);
    }

    public void run(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0].toLowerCase();
        String input = null;
        String output = null;
        String id = null;
        String ids = null;
        String word = null;
        String topic = null;
        boolean fix = false;

        

        // Parse flags
         for (int i = 1; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-i": if (i + 1 < args.length) input = args[++i]; break;
                case "-o": if (i + 1 < args.length) output = args[++i]; break;
                case "-id": if (i + 1 < args.length) id = args[++i]; break;
                case "-ids": if (i + 1 < args.length) ids = args[++i]; break;
                case "-w": if (i + 1 < args.length) word = args[++i]; break;
                case "-t": if (i + 1 < args.length) topic = args[++i]; break;
                case "-f": fix = true; break;
                default: System.out.println("Unknown option: " + args[i]);
            }
        }

        if (input == null) {
            System.out.println("Input file is required (-i).");
            return;
        }

        try {
            switch (command) {
                case "verify": handleVerify(input, output, fix); break;
                case "format": handleFormat(input, output); break;
                case "json": handleConvert(input, output); break;
                case "mini": handleMinify(input, output); break;
                case "compress": handleCompress(input, output); break;
                case "decompress": handleDecompress(input, output); break;             
                case "draw": handleDraw(input, output); break;
                case "most_active": handleMostActive(input); break;
                case "most_influencer": handleMostInfluencer(input); break;
                case "mutual": handleMutual(input, ids); break;
                case "suggest": handleSuggest(input, id); break;
                case "search":
                    if (word != null) handleSearchWord(input, word);
                    else if (topic != null) handleSearchTopic(input, topic);
                    else System.out.println("Please provide -w <word> or -t <topic> for search.");
                    break;
                default: System.out.println("Unknown command: " + command); printHelp();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------- Handlers level 1 -------------------

private void handleVerify(String input, String output, boolean fix) {
    try {
        // Read XML file
        String xml = Files.readString(Path.of(input));

        Check_XML_Consistency checker = new Check_XML_Consistency();

        // 1️⃣ Validate XML
        List<Check_XML_Consistency.checkXMLConsistency> errors =
                checker.validate(xml);

        if (errors.isEmpty()) {
            System.out.println("XML is well-formed.");
        } else {
            System.out.println("XML Errors Found:");
            for (Check_XML_Consistency.checkXMLConsistency err : errors) {
                System.out.println(err);
            }
        }

        // 2️⃣ Fix XML if requested
        if (fix) {
            if (output == null) {
                System.out.println("Output file required when using --fix");
                return;
            }

            String fixedXML = checker.autoFix(xml);
            Files.writeString(Path.of(output), fixedXML);

            System.out.println("Fixed XML saved to: " + output);
        }

    } catch (IOException e) {
        System.err.println("File error: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Error verifying XML: " + e.getMessage());
    }
}



    private void handleFormat(String input, String output) {
        try {
            String xml = Files.readString(Path.of(input));
            String formattedXML = Format_XML.formatXML(xml);

            if (output != null) {
                Files.writeString(Path.of(output), formattedXML);
                System.out.println("Formatted XML saved to: " + output);
            } else {
                System.out.println(formattedXML);
            }

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error formatting XML: " + e.getMessage());
        }
    }

    private void handleConvert(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for JSON conversion (-o).");
            return;
        }

        try {
            String xml = Files.readString(Path.of(input));
            Convert_XML_To_JSON converter = new Convert_XML_To_JSON(xml);
            String jsonOutput = converter.convert();

            File outFile = new File(output);
            if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();
            Files.writeString(Path.of(output), jsonOutput);

            System.out.println("Conversion completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error converting XML to JSON: " + e.getMessage());
        }
    }

    private void handleMinify(String input, String output) {
        try {
            String xml = Files.readString(Path.of(input));
            String minified = Minify_XML_Size.minify(xml);

            if (output != null) {
                Files.writeString(Path.of(output), minified);
                System.out.println("Minified XML saved to: " + output);
            } else {
                System.out.println(minified);
            }

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error minifying XML: " + e.getMessage());
        }
    }

    private void handleCompress(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for compression (-o).");
            return;
        }

        try {
            Compress_XML.compress(input, output);
            System.out.println("Compression completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error compressing XML: " + e.getMessage());
        }
    }

    private void handleDecompress(String input, String output) {
        if (output == null) {
            System.out.println("Output file is required for decompression (-o).");
            return;
        }

        try {
            Decompression_XML decompressor = new Decompression_XML();
            if (input.toLowerCase().endsWith(".gz")) {
                decompressor.decompressGZIP(input, output);
            } else if (input.toLowerCase().endsWith(".zip")) {
                decompressor.decompressZIP(input, output);
            } else {
                System.out.println("Unsupported file type. Only .gz and .zip are supported.");
                return;
            }

            System.out.println("Decompression completed successfully! Saved to: " + output);

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error decompressing XML: " + e.getMessage());
        }
    }
    
    // ------------------- Handlers level 2 -------------------
    

  private void handleDraw(String inputFile, String outputFile) throws IOException {
   if (outputFile == null) {
        System.out.println("Output required (-o)");
        return;
    }
    String xml = Files.readString(Path.of(inputFile));
    Vector<Tag> tags = Parsing_XML.parse(xml);
    Vector<User> users = DrawGraph.buildUsers(tags);
    Graph g = DrawGraph.buildGraph(users);
    DrawGraph.visualizeGraph(users, g, outputFile);

    System.out.println("Graph saved to: " + outputFile);
}
private void handleMostActive(String inputFile) throws IOException {
    List<User> users = loadUsersFromXML(inputFile);
    if (users.isEmpty()) {
        System.out.println("No users found.");
        return;
    }

    Map<Integer, Integer> followingCount = new HashMap<>();
    for (User u : users) {
        for (int followerId : u.followers) {
            followingCount.put(followerId, followingCount.getOrDefault(followerId, 0) + 1);
        }
    }

    User mostActive = null;
    int maxFollowing = -1;

    for (User u : users) {
        int count = followingCount.getOrDefault(u.id, 0);
        if (count > maxFollowing || (count == maxFollowing && (mostActive != null && u.id < mostActive.id))) {
            maxFollowing = count;
            mostActive = u;
        }
    }

    if (mostActive != null) {
        int totalPosts = mostActive.posts.size();
      
        System.out.println("Most Active:");
        System.out.printf("%d. %s (Posts: %d)%n",
                1, mostActive.name, totalPosts);
    }
}

private void handleMostInfluencer(String inputFile) throws IOException {
    List<User> users = loadUsersFromXML(inputFile);
    if (users.isEmpty()) {
        System.out.println("No users found.");
        return;
    }

    User influencer = null;
    int maxFollowers = -1;

    for (User u : users) {
        int count = u.followers.size();
        if (count > maxFollowers || (count == maxFollowers && (influencer == null || u.id < influencer.id))) {
            maxFollowers = count;
            influencer = u;
        }
    }

    if (influencer != null) {
        System.out.println("Most Influencer:");
        System.out.printf("%d. %s (Followers: %d)%n", 1, influencer.name, maxFollowers);
    }
}

private void handleMutual(String inputFile, String ids) throws IOException {
    if (ids == null || ids.isBlank()) {
        System.out.println("Please provide -ids");
        return;
    }

    int[] userIds = Arrays.stream(ids.split(",")).mapToInt(s -> Integer.parseInt(s.trim())).toArray();
    List<User> users = loadUsersFromXML(inputFile);

    Map<Integer, Set<Integer>> follows = new HashMap<>();
    for (User u : users) follows.putIfAbsent(u.id, new HashSet<>());
    for (User u : users) {
        for (int f : u.followers) {
            follows.putIfAbsent(f, new HashSet<>());
            follows.get(f).add(u.id);
        }
    }

    Set<Integer> mutual = new HashSet<>(follows.getOrDefault(userIds[0], new HashSet<>()));
    for (int i = 1; i < userIds.length; i++) mutual.retainAll(follows.getOrDefault(userIds[i], new HashSet<>()));

    System.out.println("Mutual Users [" + ids + "]:");
    if (mutual.isEmpty()) System.out.println("None");
    else {
        for (int uid : mutual) {
            User u = users.stream().filter(x -> x.id == uid).findFirst().orElse(null);
            if (u != null) System.out.printf("- %s (ID: %d)%n", u.name, u.id);
        }
    }
}

private void handleSuggest(String inputFile, String id) throws IOException {
    if (id == null || id.isBlank()) {
        System.out.println("Please provide -id");
        return;
    }

    int userId = Integer.parseInt(id.trim());
    List<User> users = loadUsersFromXML(inputFile);

    SuggestedFriends.Graph g = new SuggestedFriends.Graph();
    for (User u : users) for (int f : u.followers) g.addEdge(u.id, f);

    SuggestedFriends.Suggestions suggester = new SuggestedFriends.Suggestions();
    List<Integer> result = suggester.suggest(g, userId);

    System.out.println("Friend Suggestions for ID " + userId + ":");
    if (result.isEmpty()) System.out.println("None");
    else for (int uid : result) {
        User u = users.stream().filter(x -> x.id == uid).findFirst().orElse(null);
        if (u != null) System.out.printf("- %s (ID: %d)%n", u.name, u.id);
    }
}
 
private void handleSearchWord(String inputFile, String word) throws IOException {   // 
    String xml = Files.readString(Path.of(inputFile));
    Vector<Tag> tags = Parsing_XML.parse(xml);
    List<User> users = DrawGraph.buildUsers(tags);

    boolean found = false;
    System.out.println("Found \"" + word + "\" in:");

    for (User u : users) {
        // Check user name
        if (u.name.equals(word)) { // exact match
            System.out.println("User ID: " + u.id + ", Name: " + u.name);
            found = true;
        }

        // Check posts
        for (Post p : u.posts) {
            if (p.body != null && p.body.contains(word)) {
                System.out.println("Post Body: \"" + p.body + "\"");
                found = true;
            }
        }
    }

    if (!found) System.out.println("None");
}



private void handleSearchTopic(String inputFile, String topic) throws IOException {
    List<User> users = loadUsersFromXML(inputFile);
    if (users.isEmpty()) {
        System.out.println("No users found.");
        return;
    }

    List<String> matchingPosts = new ArrayList<>();
    for (User u : users) {
        for (Post p : u.posts) {
            if (p.topics != null && p.topics.contains(topic)) {
                matchingPosts.add(p.body);
            }
        }
    }

    System.out.println("Posts containing topic \"" + topic + "\":");
    if (matchingPosts.isEmpty()) System.out.println("None");
    else for (String post : matchingPosts) System.out.println("- " + post);
}

        // ------------------- Helper -------------------


 private List<User> loadUsersFromXML(String inputFile) throws IOException {
        String xml = Files.readString(Path.of(inputFile));
        Vector<Tag> tags = Parsing_XML.parse(xml);
        return DrawGraph.buildUsers(tags);
    }


    // ------------------- Help -------------------

    private void printHelp() {
    System.out.println("Available commands:");
    System.out.println("  verify -i <input.xml> [-f] [-o <output.xml>]   : Check XML consistency, optionally fix errors.");
    System.out.println("  format -i <input.xml> [-o <output.xml>]        : Prettify XML and save.");
    System.out.println("  json -i <input.xml> -o <output.json>           : Convert XML to JSON.");
    System.out.println("  mini -i <input.xml> [-o <output.xml>]          : Minify XML.");
    System.out.println("  compress -i <input.xml> -o <output.comp>       : Compress XML.");
    System.out.println("  decompress -i <input.gz|.zip> -o <output.xml>  : Decompress XML.");
    System.out.println("  draw -i <input.xml> -o <output.jpg>            : Draw XML data as a graph.");
    System.out.println("  most_active -i <input.xml>                     : Display the most active user.");
    System.out.println("  most_influencer -i <input.xml>                 : Display the most influencer user.");
    System.out.println("  mutual -i <input.xml> -ids 1,2,3               : List mutual users between given IDs.");
    System.out.println("  suggest -i <input.xml> -id 1                   : Suggest users for the given user ID.");
    System.out.println("  search -w <word> -i <input.xml>                : Search posts containing a specific word.");
    System.out.println("  search -t <topic> -i <input.xml>               : Search posts related to a specific topic.");
}
}
