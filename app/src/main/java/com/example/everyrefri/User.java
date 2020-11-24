package com.example.everyrefri;

public class User {

    public String id;
    public String email;
    public int following;
    public int follower;
    public float grade;

    public User()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email, int follower, int following, float grade)
    {
        this.id = id;
        this.email = email;
        this.follower = follower;
        this.following = following;
        this.grade = grade;
    }

    public void setValue(String id, String email, int follower, int following, float grade)
    {
        this.id = id;
        this.email = email;
        this.follower = follower;
        this.following = following;
        this.grade = grade;
    }

}