package com.example.ideation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IdeaDialog extends AppCompatDialogFragment {
    private EditText editTextIdeaTitle;
    private EditText editTextIdeaDescription;
    INodeJS api;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);



        builder.setView(view)
                .setTitle("Create New Idea")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ideaName = editTextIdeaTitle.getText().toString();
                        String ideaDescription = editTextIdeaDescription.getText().toString();
                        Group newGroup = new Group(ideaName,ideaDescription,"idea");
                        createNewIdea(newGroup);


                    }
                });

        editTextIdeaTitle = view.findViewById(R.id.new_idea_name);
        editTextIdeaDescription = view.findViewById(R.id.new_idea_description);

        return builder.create();
    }

    public void createNewIdea(Group group){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         api  = retrofit.create(INodeJS.class);

         Call<Group> call = api.createNewGroup(group);
         call.enqueue(new Callback<Group>() {
             @Override
             public void onResponse(Call<Group> call, Response<Group> response) {
                 if(!response.isSuccessful()){
                     System.out.println("!= Successful" + response.code());
                     return;
                 }
             }

             @Override
             public void onFailure(Call<Group> call, Throwable t) {
                 System.out.println("OnFailure" + t.getMessage());
             }
         });





    }



}


