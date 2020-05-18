package com.example.ideation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.security.AccessControlContext;
import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Callback;

public class FRecyclerAdapter extends RecyclerView.Adapter<FRecyclerAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;
    private Context mcontext;
    private String email;


    public FRecyclerAdapter(List<Feedback> feedbackList, Context mcontext) {
        this.feedbackList = feedbackList;
        this.mcontext = mcontext;
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewFeedbackText;
        public TextView textViewFeedbackRating;
        public TextView textViewFeedbackUsername;
        CardView feedback_item;
        private String userEmail = MainActivity.getLoggedUserEmail();


        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFeedbackText=itemView.findViewById(R.id.textView_feedback_text);
            textViewFeedbackRating=itemView.findViewById(R.id.textView_feedback_rating);
            textViewFeedbackUsername=itemView.findViewById(R.id.textView_feedback_username);
            feedback_item=itemView.findViewById(R.id.feedback_item);
        }
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        FeedbackViewHolder fvh = new FeedbackViewHolder(v);

        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback currentFeedback = feedbackList.get(position);



        /*holder.feedback_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,IdeaPage.class);
                intent.putExtra("text",feedbackList.get(position).getText());
                mcontext.startActivity(intent);
            }
        });*/
        //System.out.println("----------------------------------------------------------------------------------------------------------" + currentFeedback.getFeedbackUserEmail() + position );
        holder.textViewFeedbackUsername.setText("Creator:\n "+currentFeedback.getFeedbackUserEmail());
        holder.textViewFeedbackText.setText(currentFeedback.getText());
        holder.textViewFeedbackRating.setText(currentFeedback.getRating().toString());


    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }


}
