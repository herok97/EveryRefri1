package com.example.everyrefri;

public class user {

    public String id;
    public String email;
    public int following;
    public int follower;
    public float grade;

    public user() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public user(String id, String email) {
        this.id = id;
        this.email = email;
        this.following = following;
        this.follower = follower;
        this.grade = grade;
    }

}