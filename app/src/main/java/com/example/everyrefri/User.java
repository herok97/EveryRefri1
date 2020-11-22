package com.example.everyrefri;

public class User {

    public String id;
    public String email;
    public int following;
    public int follower;
    public float grade;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
        this.following = following;
        this.follower = follower;
        this.grade = grade;
    }

}