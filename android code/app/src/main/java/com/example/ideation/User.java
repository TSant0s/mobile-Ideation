package com.example.ideation;
import com.google.gson.annotations.SerializedName;


public class User {


    private int userID;
    private String name;
    private String email;
    private String password;
    private Double rating;
    private String user_bio;
    private Double funds;


    public User(int userID, String name, String email, String password, Double rating, String user_bio) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.user_bio = user_bio;
    }

    public int getUserID() {
        return userID;
    }

    public Double getFunds(){
        return funds;
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
        return user_bio;
    }
}
