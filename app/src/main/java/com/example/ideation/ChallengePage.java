package com.example.ideation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ideation.Retrofit.INodeJS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChallengePage extends AppCompatActivity {
    private FloatingActionButton btnNewFeedbackChallenge;
    private RecyclerView challengeFeedbackRecyclerView;
    private RecyclerView.Adapter challengeFeedbackAdapter;
    private RecyclerView.LayoutManager challengeFeedbackLayoutManager = new LinearLayoutManager(this);
    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<FeedbackChallenge> cfeedbacks = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page);

        TextView textViewChallengeDescription,textViewChallengeInstructions,textViewChallengeEndDate,textViewChallengeTitle;
        String challengeDescription,challengeInstructions,challengeEndDate,challengeTitle;



        textViewChallengeDescription= findViewById(R.id.textView_challengePage_description);
        textViewChallengeTitle=findViewById(R.id.textView_challengePage_title);
        textViewChallengeInstructions=findViewById(R.id.textView_challengePage_instructions);
        textViewChallengeEndDate=findViewById(R.id.textView_challengePage_endDate);
        challengeFeedbackRecyclerView=findViewById(R.id.recyclerView_feedback_challenge);
        btnNewFeedbackChallenge = findViewById(R.id.button_new_challengeFeedback);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout_challengeFeedback);


        challengeDescription=getIntent().getExtras().getString("challengeDescription");
        challengeTitle=getIntent().getExtras().getString("title");
        challengeInstructions=getIntent().getExtras().getString("instructions");
        challengeEndDate=getIntent().getExtras().getString("endDate");



        textViewChallengeDescription.setText("Description: \n"+challengeDescription);
        textViewChallengeTitle.setText(challengeTitle);
        textViewChallengeInstructions.setText("Instructions: \n "+challengeInstructions);
        textViewChallengeEndDate.setText(challengeEndDate.substring(0,10));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                INodeJS api  = retrofit.create(INodeJS.class);


                Call<List<FeedbackChallenge>> callChallengeFeedback = api.getChallengeFeedback("1");
                callChallengeFeedback.enqueue(new Callback<List<FeedbackChallenge>>() {
                    @Override
                    public void onResponse(Call<List<FeedbackChallenge>> call, Response<List<FeedbackChallenge>> response) {
                        if(!response.isSuccessful()){
                            System.out.println("!= Successful" + response.code());
                            return;
                        }

                        cfeedbacks = (ArrayList<FeedbackChallenge>) response.body();


                        setupRecyclerView();





                    }





                    public void setupRecyclerView(){
                        challengeFeedbackAdapter = new FCRecyclerAdapter(cfeedbacks,getBaseContext());
                        challengeFeedbackRecyclerView.setLayoutManager(challengeFeedbackLayoutManager);
                        challengeFeedbackRecyclerView.setAdapter(challengeFeedbackAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<FeedbackChallenge>> call, Throwable t) {
                        System.out.println("OnFailure" + t.getMessage());
                    }
                });

                swipeRefreshLayout.setRefreshing(false);

            }
        });



        btnNewFeedbackChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackChallengeDialog();



            }
            private void openFeedbackChallengeDialog(){
                ChallengeFeedbackDialog challengeFeedbackDialog = new ChallengeFeedbackDialog();
                challengeFeedbackDialog.show(getSupportFragmentManager(),"new Feedback");
            }

        });



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api  = retrofit.create(INodeJS.class);


        Call<List<FeedbackChallenge>> callChallengeFeedback = api.getChallengeFeedback("1");
        callChallengeFeedback.enqueue(new Callback<List<FeedbackChallenge>>() {
            @Override
            public void onResponse(Call<List<FeedbackChallenge>> call, Response<List<FeedbackChallenge>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                cfeedbacks = (ArrayList<FeedbackChallenge>) response.body();


                setupRecyclerView();





            }





            public void setupRecyclerView(){
                challengeFeedbackAdapter = new FCRecyclerAdapter(cfeedbacks,getBaseContext());
                challengeFeedbackRecyclerView.setLayoutManager(challengeFeedbackLayoutManager);
                challengeFeedbackRecyclerView.setAdapter(challengeFeedbackAdapter);


            }

            @Override
            public void onFailure(Call<List<FeedbackChallenge>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });










    }
}
