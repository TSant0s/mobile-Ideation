package com.example.ideation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ideation.Retrofit.INodeJS;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EditBioDialog extends AppCompatDialogFragment {


    private String loggedUserID = GroupsFragment.userID;
    private String loggedEmail = MainActivity.getLoggedUserEmail();
    private RequestQueue requestQueue;

    private EditText bioText;
    INodeJS api;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_edit_bio_dialog,null);



        builder.setView(view)
                .setTitle("Write your Bio")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String bio = bioText.getText().toString();
                        String data = "{\"userID\":\""+loggedUserID+"\",\"bio\":\""+bio+"\"}";
                        updateUserBio(data);
                        dismiss();
                        ProfileFragment f = new ProfileFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, f);
                        ft.addToBackStack(null);
                        ft.commit();
                        Toast.makeText(getContext(),"Bio Updated!", Toast.LENGTH_SHORT).show();


                    }
                });

        bioText = view.findViewById(R.id.new_bio);


        return builder.create();
    }

    public void updateUserBio(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/updateBio";
        System.out.println("asdasdasd");

        requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
            try {
                JSONObject objres = new JSONObject(response);
                System.out.print("-------------enviado" + objres.toString());
            } catch (JSONException e) {
                System.out.println("error");
            }
        }, error -> System.out.println("erro: " + error.getMessage())){
            @Override
            public String getBodyContentType(){ return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() {
                try{
                    return savedata == null ? null : savedata.getBytes("utf-8");
                } catch(UnsupportedEncodingException uee){
                    return null;
                }
            }
        };
        requestQueue.add(stringRequest);


    }


}


