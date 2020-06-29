package com.example.ideation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideation.Retrofit.INodeJS;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreFragment extends Fragment {

    private RecyclerView storeRecyclerView;
    private RecyclerView.Adapter storeAdapter;
    private RecyclerView.LayoutManager storeLayoutManager;
    ArrayList<Reward> rewardList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api  = retrofit.create(INodeJS.class);


        Call<List<Reward>> call = api.getRewardInfo();
        call.enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                rewardList = (ArrayList<Reward>) response.body();
                for(Reward reward : rewardList){
                    System.out.println(reward.getName()+"\n"+reward.getPrice()+"\n");
                }

                setupRecyclerView();


            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_store,container,false);
         storeRecyclerView = v.findViewById(R.id.recyclerView_store);


         return v;
    }


    public void setupRecyclerView(){
        storeAdapter = new StoreRecyclerAdapter(rewardList,getContext());
        storeLayoutManager = new LinearLayoutManager(getActivity());
        storeRecyclerView.setLayoutManager(storeLayoutManager);
        storeRecyclerView.setAdapter(storeAdapter);
    }
}
