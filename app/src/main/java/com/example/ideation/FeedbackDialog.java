package com.example.ideation;

import android.app.Dialog;
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

public class FeedbackDialog extends AppCompatDialogFragment {

    private String loggedUserID = GroupsFragment.userID;
    private String loggedEmail = MainActivity.getLoggedUserEmail();
    private String clickedGroupID = GRecyclerAdapter.getClickedGroupID();

    private EditText editTextFeedbackText;
    INodeJS api;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.feedback_layout,null);
       // System.out.println("napagina certa:  "+loggedUserID);


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

                        String newFeedBackText = editTextFeedbackText.getText().toString();
                        Feedback newFeedback = new Feedback(newFeedBackText, (double) 0,Integer.parseInt(loggedUserID),Integer.parseInt(clickedGroupID),loggedEmail);
                        createNewFeedback(newFeedback);
                        Toast.makeText(getContext(),"Thank you for sharing your Feedback!", Toast.LENGTH_SHORT).show();


                    }
                });

        editTextFeedbackText = view.findViewById(R.id.new_feedback_text);


        return builder.create();
    }

    public void createNewFeedback(Feedback feedback){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api  = retrofit.create(INodeJS.class);

        Call<Feedback> call = api.createNewFeedback(feedback);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });





    }






}
