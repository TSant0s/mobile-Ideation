package com.example.ideation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ideation.Retrofit.INodeJS;
import com.example.ideation.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private String email = MainActivity.getLoggedUserEmail();
    private String loggedUserID = GroupsFragment.userID;
    private List<User> users;
    private INodeJS api;
    private TextView userName;
    private TextView userBio;
    private TextView userEmail;
    private TextView userRating;
    private TextView userFunds;
    private String name;
    private String bio;
    private Double rating;
    private String funds;
    private ImageButton editBio;
    private List<Transaction> tList;
    private RecyclerView profileRecyclerView;
    private RecyclerView.Adapter profileAdapter;
    private RecyclerView.LayoutManager profileLayoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        userName = v.findViewById(R.id.textView_profile_name);
        userBio = v.findViewById(R.id.textView_profile_bio);
        userEmail = v.findViewById(R.id.profile_email);
        userRating = v.findViewById(R.id.profile_rating);
        editBio = v.findViewById(R.id.btn_edit_bio);
        userFunds = v.findViewById(R.id.profile_funds);
        profileRecyclerView = v.findViewById(R.id.profile_RecyclerView);
        return v;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api  = retrofit.create(INodeJS.class);
        getUserInfo();
        getTransactions(Integer.parseInt(loggedUserID));



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBioDialog editBioDialog = new EditBioDialog();
                editBioDialog.show(getFragmentManager(),"User Bio Update");



            }
        });
    }



    private void getUserInfo(){

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
                    name = user.getName();
                    bio = user.getUserBio();
                    rating = user.getRating();
                    funds = user.getFunds().toString();



                }

                userName.setText(name);
                userFunds.setText(funds);
                userBio.setText(bio);
                userRating.setText(rating.toString());
                userEmail.setText(email);



            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });

    }


    public void getTransactions(int id){
        Call<List<Transaction>> call = api.getTransactionByUser(id);

        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>>call, Response<List<Transaction>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                tList =  response.body();
                setupRecyclerView();






            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });



    }
    public void setupRecyclerView(){
        profileAdapter = new ProfileRecyclerAdapter(tList, getContext());
        profileLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        profileRecyclerView.setLayoutManager(profileLayoutManager);
        profileRecyclerView.setAdapter(profileAdapter);



    }


}
