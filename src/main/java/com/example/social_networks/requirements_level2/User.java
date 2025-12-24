package com.example.social_networks.requirements_level2;

import java.util.Vector;

public class User {

    public int id;
    public String name;
    public Vector<Integer> followers = new Vector<>();
    public Vector<Post> posts = new Vector<>();
}
