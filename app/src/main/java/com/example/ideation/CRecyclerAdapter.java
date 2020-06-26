package com.example.ideation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CRecyclerAdapter extends RecyclerView.Adapter<CRecyclerAdapter.ChallengeViewHolder> {
    private List<UserChallenge> challengeGroupList;
    private Context mContext;
    public  static int clickedChallengeID;



    public class ChallengeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewChallengeName,textViewChallengeUsername,textViewChallengeDate;
        CardView challengeItem;



        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChallengeName = itemView.findViewById(R.id.textView_challenge_name);
            textViewChallengeUsername = itemView.findViewById(R.id.textView_challenge_username);
            textViewChallengeDate = itemView.findViewById(R.id.textView_challenge_date);
            challengeItem = itemView.findViewById(R.id.challenge_item);




        }
    }

    public CRecyclerAdapter(ArrayList<UserChallenge> challengeGroupList, Context mContext) {
        this.challengeGroupList = challengeGroupList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenge_item,parent,false);
        ChallengeViewHolder chv = new ChallengeViewHolder(v);
        return chv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        UserChallenge currentChallengeGroup = challengeGroupList.get(position);
        holder.textViewChallengeName.setText(currentChallengeGroup.getTitle());
        holder.textViewChallengeUsername.setText(currentChallengeGroup.getEmail());

        holder.textViewChallengeDate.setText("End Date:  " + currentChallengeGroup.getEnd_date().substring(0,10));
        holder.challengeItem.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                clickedChallengeID = currentChallengeGroup.getChallengeID();
                //System.out.println("bora bora: "+clickedChallengeID);


                Intent intent = new Intent(mContext,ChallengePage.class);
                intent.putExtra("title",challengeGroupList.get(position).getTitle());
                intent.putExtra("challengeDescription",challengeGroupList.get(position).getDescription());
                intent.putExtra("instructions",challengeGroupList.get(position).getInstructions());
                intent.putExtra("endDate",challengeGroupList.get(position).getEnd_date());
                intent.putExtra("challenge_userID",challengeGroupList.get(position).getChallenge_userID());

                mContext.startActivity(intent);
            }
        });


    }

    public static int getClickedChallengeID(){
        return clickedChallengeID;
    }


    @Override
    public int getItemCount() {
        return challengeGroupList.size();
    }

}
