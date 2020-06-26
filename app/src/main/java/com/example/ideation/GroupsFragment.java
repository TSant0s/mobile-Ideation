package com.example.ideation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import com.example.ideation.Retrofit.INodeJS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupsFragment extends Fragment  {

private RecyclerView groupRecyclerView;
private RecyclerView.Adapter groupAdapter;
private RecyclerView.LayoutManager groupLayoutManager;
private Retrofit Retrofit;
ArrayList<Group> groups = new ArrayList<>();
ArrayList<userGroups> userGroups = new ArrayList<>();
private INodeJS api;
private String email = MainActivity.getLoggedUserEmail();
private List<User> users;

public static String userID;
public static Double funds;


private SwipeRefreshLayout swipeRefreshLayout;
private FloatingActionButton btnNewIdea;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getUserInfo(email);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups,container,false);
        groupRecyclerView = v.findViewById(R.id.groups_RecyclerView);
        btnNewIdea = v.findViewById(R.id.button_new_idea);
        swipeRefreshLayout=v.findViewById(R.id.swipeRefreshLayout_groups);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserGroups(Integer.parseInt(userID));
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnNewIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        return v;


    }

    public void openDialog(){
        IdeaDialog ideaDialog = new IdeaDialog();
        ideaDialog.show(getFragmentManager(),"New Idea");

    }



    public void setupRecyclerView(){
        groupAdapter = new GRecyclerAdapter(userGroups, getContext());
        groupLayoutManager = new LinearLayoutManager(getActivity());
        groupRecyclerView.setLayoutManager(groupLayoutManager);
        groupRecyclerView.setAdapter(groupAdapter);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

                  userID = String.valueOf(user.getUserID());
                  funds =user.getFunds();
                  getUserGroups(Integer.parseInt(userID));
                    System.out.println(userID);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });

    }


    public void getUserGroups(int id){

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api1  = retrofit1.create(INodeJS.class);


        Call<List<userGroups>> call1 = api1.getUserGroups(id);
        call1.enqueue(new Callback<List<userGroups>>() {
            @Override
            public void onResponse(Call<List<userGroups>> call, Response<List<userGroups>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                userGroups = (ArrayList<userGroups>) response.body();

                setupRecyclerView();
                for(userGroups u: userGroups){
                    System.out.println(u.getGroupID_fk()+"\n"+u.getDescription()+"\n"+u.getName()+"\n");
                }
            }

            @Override
            public void onFailure(Call<List<userGroups>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });
    }






}
