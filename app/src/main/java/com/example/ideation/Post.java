package com.example.ideation;

import com.google.gson.annotations.SerializedName;

public class Post {

    private int userId;
    private int id;
    private String Title;


    @SerializedName("body")
    private String text;


    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public String getText() {
        return text;
    }
}
