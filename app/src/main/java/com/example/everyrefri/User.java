package com.example.everyrefri;

import android.content.Intent;

public class User {

    public String id;
    public String email;
    public int following;
    public int follower;
    public float grade;
    public String location;
    public User()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String email, int follower, int following, float grade, String location)
    {
        this.id = id;
        this.email = email;
        this.follower = follower;
        this.following = following;
        this.grade = grade;
        this.location = location;
    }

    public void setValue(String id, String email, int follower, int following, float grade, String location)
    {
        this.id = id;
        this.email = email;
        this.follower = follower;
        this.following = following;
        this.grade = grade;
        this.location = location;
    }

    public void getUserFromIntent(Intent intent)
    {
        this.setValue(
                intent.getExtras().getString("userId"),
                intent.getExtras().getString("userEmail"),
                intent.getExtras().getInt("userFollower"),
                intent.getExtras().getInt("userFollowing"),
                intent.getExtras().getFloat("userGrade"),
                intent.getExtras().getString("location"));
    }

    public Intent setUserToIntent(Intent intent)
    {
        intent.putExtra("userId", this.id);
        intent.putExtra("userEmail", this.email);
        intent.putExtra("userFollower", this.follower);
        intent.putExtra("userFollowing", this.following);
        intent.putExtra("userGrade", this.grade);
        intent.putExtra("location", this.location);
        return intent;
    }

}