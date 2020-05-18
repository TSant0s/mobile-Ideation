package com.example.ideation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

public class NewChallengeDialog extends AppCompatDialogFragment {
    private EditText editTextNewChallengeTitle;
    private EditText editTextNewChallengeDescription;
    private EditText editTextNewChallengeInstructions;
    private EditText editTextNewChallengeEndDate;
    INodeJS api;
    private Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_challenge_dialog,null);
        mContext=getContext();

        editTextNewChallengeTitle = view.findViewById(R.id.new_challenge_title);
        editTextNewChallengeDescription = view.findViewById(R.id.new_challenge_description);
        editTextNewChallengeInstructions = view.findViewById(R.id.new_challenge_instructions);
        editTextNewChallengeEndDate = view.findViewById(R.id.new_challenge_endDate);

        builder.setView(view)
                .setTitle("Create New Challenge")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String challengeTitle = editTextNewChallengeTitle.getText().toString();
                        String challengeDescription = editTextNewChallengeDescription.getText().toString();
                        String challengeInstructions = editTextNewChallengeInstructions.getText().toString();
                        String challengeEnDate = editTextNewChallengeEndDate.getText().toString();
                        ChallengeGroup newChallenge = new ChallengeGroup(challengeEnDate,challengeTitle,challengeDescription,challengeInstructions,"1234");
                        createNewChallenge(newChallenge);



                    }
                });



        return builder.create();
    }



    public void createNewChallenge(ChallengeGroup ChallengeGroup){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api  = retrofit.create(INodeJS.class);

        Call<ChallengeGroup> call = api.createNewChallengeGroup(ChallengeGroup);
        call.enqueue(new Callback<ChallengeGroup>() {
            @Override
            public void onResponse(Call<ChallengeGroup> call, Response<ChallengeGroup> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<ChallengeGroup> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });





    }



}
