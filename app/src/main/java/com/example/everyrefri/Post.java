package com.example.everyrefri;

public class Post {

    public String id;
    public String email;
    public String title;
    public String category;
    public String buy;
    public String exp;
    public Boolean isSold;
    public String storage;
    public String inst;
    public String name;

    public Post()
    {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String name,
                String id,
                String email,
                String title,
                String category,
                String buy,
                String exp,
                Boolean isSold,
                String storage,
                String inst)
    {
        this.name = name;
        this.id = id;
        this.email = email;
        this.title = title;
        this.category = category;
        this.buy = buy;
        this.exp = exp;
        this.isSold = isSold;
        this.storage = storage;
        this.inst = inst;
    }

    public void setValue(String name,
                         String id,
                         String email,
                         String title,
                         String category,
                         String buy,
                         String exp,
                         Boolean isSold,
                         String storage,
                         String inst)
    {
        this.name = name;
        this.id = id;
        this.email = email;
        this.title = title;
        this.category = category;
        this.buy = buy;
        this.exp = exp;
        this.isSold = isSold;
        this.storage = storage;
        this.inst = inst;
    }

}