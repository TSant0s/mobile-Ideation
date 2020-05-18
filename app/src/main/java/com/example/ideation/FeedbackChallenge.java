package com.example.ideation;

public class FeedbackChallenge {
    private int cChallengeID_fk;
    private String cFeedbackText,email;
    private Double cFeedbackRating;



    public FeedbackChallenge( int cChallengeID_fk, String cFeedbackText, Double cFeedbackRating,String email) {

        this.email = email;

        this.cChallengeID_fk = cChallengeID_fk;
        this.cFeedbackText = cFeedbackText;
        this.cFeedbackRating = cFeedbackRating;
    }

    public String getEmail() {
        return email;
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
