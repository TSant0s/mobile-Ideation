package com.example.ideation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ideation.Retrofit.INodeJS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChallengesFragment extends Fragment {
    private RecyclerView challengeRecyclerView;
    private RecyclerView.Adapter challengerAdapter;
    private RecyclerView.LayoutManager challengeLayoutManager;
    private FloatingActionButton btnNewChallenge;
    ArrayList<ChallengeGroup> challenges=new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api = retrofit.create(INodeJS.class);


        Call<List<ChallengeGroup>> call = api.getChallenges();
        call.enqueue(new Callback<List<ChallengeGroup>>() {
            @Override
            public void onResponse(Call<List<ChallengeGroup>> call, Response<List<ChallengeGroup>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                challenges = (ArrayList<ChallengeGroup>) response.body();

                setupRecyclerView();

            }

            @Override
            public void onFailure(Call<List<ChallengeGroup>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenges,container,false);
        challengeRecyclerView = v.findViewById(R.id.challenges_RecyclerView);
        btnNewChallenge = v.findViewById(R.id.button_new_challenge);
        swipeRefreshLayout=v.findViewById(R.id.swipeRefreshLayout_challenges);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                INodeJS api = retrofit.create(INodeJS.class);


                Call<List<ChallengeGroup>> call = api.getChallenges();
                call.enqueue(new Callback<List<ChallengeGroup>>() {
                    @Override
                    public void onResponse(Call<List<ChallengeGroup>> call, Response<List<ChallengeGroup>> response) {
                        if (!response.isSuccessful()) {
                            System.out.println("!= Successful" + response.code());
                            return;
                        }

                        challenges = (ArrayList<ChallengeGroup>) response.body();

                        setupRecyclerView();

                    }

                    @Override
                    public void onFailure(Call<List<ChallengeGroup>> call, Throwable t) {
                        System.out.println("OnFailure" + t.getMessage());
                    }


                });

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        btnNewChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();


            }
        });

        return v;
    }

    public void openDialog(){
        NewChallengeDialog newChallengeDialog= new NewChallengeDialog();
        newChallengeDialog.show(getFragmentManager(),"New Challenge");

    }



    public void setupRecyclerView(){
        challengerAdapter = new CRecyclerAdapter(challenges,getContext());
        challengeLayoutManager = new LinearLayoutManager(getActivity());
        challengeRecyclerView.setLayoutManager(challengeLayoutManager);
        challengeRecyclerView.setAdapter(challengerAdapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
