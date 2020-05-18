package com.example.ideation;

import com.example.ideation.Retrofit.INodeJS;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Feedback {
    private String text;
    private Double rating;
    private int userID_fk,grupoId;

    private INodeJS api;
    private List<User> usersEmails;
    private String email;



    public Feedback(String text, Double rating, int userID_fk, int grupoId, String email) {
        this.text = text;
        this.email=email;
        this.rating = rating;
        this.userID_fk = userID_fk;
        this.grupoId = grupoId;
    }

    public String getFeedbackUserEmail() {
        return email;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public Double getRating() {
        return rating;
    }

    public int getUserId() {
        return userID_fk;
    }

    public int getGrupoId() {
        return grupoId;
    }


}
