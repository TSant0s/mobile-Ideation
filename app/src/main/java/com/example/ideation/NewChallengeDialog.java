package com.example.ideation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ideation.Retrofit.INodeJS;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
    private String res;
    private EditText editTextAddUsers;
    private ImageButton btnAddUsers;
    private RequestQueue requestQueue;
    private String loggedUserID = GroupsFragment.userID;
    private Context mContext;
    private List<User> users;
    private int userID;
    ArrayList<Integer> userIds = new ArrayList<>();

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
        btnAddUsers = view.findViewById(R.id.btn_add_users);
        editTextAddUsers = view.findViewById(R.id.new_user_email);



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

                        String data1 =
                                "{\"title\":\""+challengeTitle+"\",\"description\":\""+challengeDescription+"\",\"instructions\":\""+challengeInstructions+"\",\"end_date\":\""+challengeEnDate+"\",\"userID\":\""+loggedUserID+"\"}";
                        createNewChallenge(data1);
                        dismiss();
                        ChallengesFragment f = new ChallengesFragment();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, f);
                        ft.addToBackStack(null);
                        ft.commit();
                        Toast.makeText(getContext(),"Challenge Created Successfully", Toast.LENGTH_SHORT).show();



                    }
                });



        return builder.create();


    }



    public void createNewChallenge(String data){


        final String savedata = data;
        String URL ="http://192.168.1.9:3000/challenge/new";

        requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                res = jsonObject.getString("insertId");
                String data3 =
                        "{\"userID\":"+loggedUserID+",\"challengeID\":"+res+"}";
                hello(data3);



                System.out.println(res);
            } catch (JSONException e) {
                e.printStackTrace();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void hello(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/userChallenge/newUserChallenge";


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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void getUserInfo(String email){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api  = retrofit.create(INodeJS.class);


        Call<List<User>> call = api.getUserInfo(email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>>call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                users =  response.body();
                for(User user : users ){
                    userID = user.getUserID();
                }
                userIds.add(userID);
                System.out.println(userIds);

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });

    }



}
