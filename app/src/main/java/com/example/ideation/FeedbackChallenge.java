package com.example.ideation;

public class FeedbackChallenge {
    private int cChallengeID_fk, cFeedbackID;
    private String cFeedbackText,email;
    private Double cFeedbackRating;
    private int cUserID_fk;



    public FeedbackChallenge( int cChallengeID_fk, String cFeedbackText, Double cFeedbackRating,String email,int cUserID_fk) {

        this.email = email;
        this.cUserID_fk = cUserID_fk;

        this.cChallengeID_fk = cChallengeID_fk;
        this.cFeedbackText = cFeedbackText;
        this.cFeedbackRating = cFeedbackRating;
    }

    public String getEmail() {
        return email;
    }


    public int getcUserID_fk() {
        return cUserID_fk;
    }

    public int getcFeedbackID() {
        return cFeedbackID;
    }

    public int getcChallengeID_fk() {
        return cChallengeID_fk;
    }

    public String getcFeedbackText() {
        return cFeedbackText;
    }

    public Double getcFeedbackRating() {
        return cFeedbackRating;
    }

    public void setcFeedbackRating(Double cFeedbackRating) {
        this.cFeedbackRating = cFeedbackRating;
    }
}
