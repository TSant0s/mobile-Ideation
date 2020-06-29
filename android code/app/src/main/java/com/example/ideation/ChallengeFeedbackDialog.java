package com.example.ideation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.ideation.Retrofit.INodeJS;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChallengeFeedbackDialog extends AppCompatDialogFragment {
    private String loggedEmail = MainActivity.getLoggedUserEmail();
    private String loggedUserID = GroupsFragment.userID;
    private int clickedChallengeID = CRecyclerAdapter.getClickedChallengeID();

    private EditText editTextChallengeFeedbackText;
    INodeJS api;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.challenge_feedback_layout,null);

        System.out.println("id atraves do dialog: "+loggedUserID);



        builder.setView(view)
                .setTitle("Give your Feedback")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newChallengeFeedbackText = editTextChallengeFeedbackText.getText().toString();
                        FeedbackChallenge newFeedbackChallenge = new FeedbackChallenge(clickedChallengeID, newChallengeFeedbackText, (double) 1,loggedEmail,Integer.parseInt(loggedUserID));
                        createNewFeedbackChallenge(newFeedbackChallenge);


                        Toast.makeText(getContext(),"Thank you for sharing your feedback!", Toast.LENGTH_SHORT).show();


                    }
                });

        editTextChallengeFeedbackText = view.findViewById(R.id.new_challenge_feedback_text);


        return builder.create();
    }





    public void createNewFeedbackChallenge(FeedbackChallenge feedbackChallenge){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api  = retrofit.create(INodeJS.class);

        Call<FeedbackChallenge> call = api.createNewFeedbackChallenge(feedbackChallenge);
        call.enqueue(new Callback<FeedbackChallenge>() {
            @Override
            public void onResponse(Call<FeedbackChallenge> call, Response<FeedbackChallenge> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<FeedbackChallenge> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });


}}
