package com.example.ideation;
import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("Body")
    private int userID;
    private String name;
    private String email;
    private String password;
    private Double rating;
    private String userBio;


    public User(int userID, String name, String email, String password, Double rating, String userBio) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.userBio = userBio;
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Double getRating() {
        return rating;
    }

    public String getUserBio() {
        return userBio;
    }
}
