package com.example.ideation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FCRecyclerAdapter extends RecyclerView.Adapter<FCRecyclerAdapter.ChallengeFeedbackViewHolder> {

    private ArrayList<FeedbackChallenge> feedbackChallengeList;
    private Context mContext;


    public FCRecyclerAdapter(ArrayList<FeedbackChallenge> feedbackChallengeList, Context mContext) {
        this.feedbackChallengeList = feedbackChallengeList;
        this.mContext = mContext;
    }



    public static class ChallengeFeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewChallengeFeedbackText;
        public TextView textViewChallengeFeedbackRating;
        public TextView textViewChallengeFeedbackUsername;
        CardView feedbackChallengeItem;




        public ChallengeFeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChallengeFeedbackText= itemView.findViewById(R.id.textView_challenge_feedback_text);
            textViewChallengeFeedbackRating= itemView.findViewById(R.id.textView_challenge_feedback_rating);
            textViewChallengeFeedbackUsername= itemView.findViewById(R.id.textView_challenge_feedback_username);
            feedbackChallengeItem=itemView.findViewById(R.id.challenge_feedback_item);
        }
    }

    @NonNull
    @Override
    public ChallengeFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_challenge_item,parent,false);
        ChallengeFeedbackViewHolder fcvh= new ChallengeFeedbackViewHolder(v);
        return fcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeFeedbackViewHolder holder, int position) {
        FeedbackChallenge currentFeedbackChallenge = feedbackChallengeList.get(position);

        holder.textViewChallengeFeedbackText.setText(currentFeedbackChallenge.getcFeedbackText());
        holder.textViewChallengeFeedbackRating.setText(String.valueOf(currentFeedbackChallenge.getcFeedbackRating()));
        holder.textViewChallengeFeedbackUsername.setText("Creator:\n "+currentFeedbackChallenge.getEmail());

    }

    @Override
    public int getItemCount() {
        return feedbackChallengeList.size();
    }


}
