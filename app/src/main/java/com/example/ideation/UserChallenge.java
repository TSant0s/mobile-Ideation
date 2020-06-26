package com.example.ideation;

import java.sql.Date;

public class UserChallenge {
    private int userID_fk,challengeID,challenge_userID;
    private String title,description,instructions,email;
    private String end_date;

    public int getUserID_fk() {
        return userID_fk;
    }

    public int getChallengeID() {
        return challengeID;
    }

    public int getChallenge_userID() {
        return challenge_userID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getEmail() {
        return email;
    }

    public String getEnd_date() {
        return end_date;
    }
}
