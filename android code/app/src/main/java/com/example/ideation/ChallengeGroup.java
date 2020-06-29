package com.example.ideation;

import java.sql.Timestamp;
public class ChallengeGroup {

    private String end_date;
    private String title,description,instructions,email;
    private int challengeID;
    private int challenge_userID;

    public ChallengeGroup( String end_date, String title, String description, String instructions, String email) {

        this.end_date = end_date;

        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.email = email;
    }

    public int getChallengeID() {
        return challengeID;
    }

    public int getChallenge_userID(){
        return challenge_userID;
    }

    public String getUserEmail() {
        return email;
    }

    public String getEndDate() {
        return end_date;
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
}
