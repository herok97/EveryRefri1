package com.example.everyrefri;

import android.content.Intent;

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

    public void getPostFromIntent(Intent intent)
    {
        this.setValue(
                intent.getExtras().getString("postName"),
                intent.getExtras().getString("postId"),
                intent.getExtras().getString("postEmail"),
                intent.getExtras().getString("postTitle"),
                intent.getExtras().getString("postCategory"),
                intent.getExtras().getString("postBuy"),
                intent.getExtras().getString("postExp"),
                intent.getExtras().getBoolean("postIsSold"),
                intent.getExtras().getString("postStorage"),
                intent.getExtras().getString("postInst"));
    }

    public Intent setPostToIntent(Intent intent)
    {
        intent.putExtra("postName", this.name);
        intent.putExtra("postId", this.id);
        intent.putExtra("postEmail", this.email);
        intent.putExtra("postTitle", this.title);
        intent.putExtra("postCategory", this.category);
        intent.putExtra("postBuy", this.buy);
        intent.putExtra("postExp", this.exp);
        intent.putExtra("postIsSold", this.isSold);
        intent.putExtra("postStorage", this.storage);
        intent.putExtra("postInst", this.inst);
        return intent;
    }

}