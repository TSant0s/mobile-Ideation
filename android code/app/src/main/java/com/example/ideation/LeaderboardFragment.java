package com.example.ideation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ideation.Retrofit.INodeJS;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboardRecyclerView;
    private RecyclerView.Adapter LbAdapter;
    private RecyclerView.LayoutManager LbLayoutManager;
    ArrayList<User> userList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api  = retrofit.create(INodeJS.class);


        Call<List<User>> call = api.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                userList = (ArrayList<User>) response.body();
                setupRecyclerView();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard,container,false);
        leaderboardRecyclerView = v.findViewById(R.id.leaderboard_recyclerView);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout_leaderboard);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                INodeJS api  = retrofit.create(INodeJS.class);


                Call<List<User>> call = api.getUsers();
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if(!response.isSuccessful()){
                            System.out.println("!= Successful" + response.code());
                            return;
                        }

                        userList = (ArrayList<User>) response.body();
                        setupRecyclerView();

                    }

                    public void setupRecyclerView(){
                        LbAdapter = new LbRecyclerAdapter(userList, getContext());
                        LbLayoutManager = new LinearLayoutManager(getActivity());
                        leaderboardRecyclerView.setLayoutManager(LbLayoutManager);
                        leaderboardRecyclerView.setAdapter(LbAdapter);



                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        System.out.println("OnFailure" + t.getMessage());
                    }
                });

                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"Refreshed!",Toast.LENGTH_SHORT).show();
            }


        });

        return v;
    }


    public void setupRecyclerView(){
        LbAdapter = new LbRecyclerAdapter(userList, getContext());
        LbLayoutManager = new LinearLayoutManager(getActivity());
        leaderboardRecyclerView.setLayoutManager(LbLayoutManager);
        leaderboardRecyclerView.setAdapter(LbAdapter);



    }



}
