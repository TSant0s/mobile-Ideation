package com.example.ideation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private List<User> users;
    private INodeJS api;
    private TextView userName;
    private TextView userBio;
    private TextView userEmail;
    private TextView userRating;
    private String name;
    private String bio;
    private Double rating;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        userName = v.findViewById(R.id.textView_profile_name);
        userBio = v.findViewById(R.id.textView_profile_bio);
        userEmail = v.findViewById(R.id.profile_email);
        userRating = v.findViewById(R.id.profile_rating);
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
                }
                userName.setText(name);
               // userBio.setText(bio); ------------------------------Está a dar null
                userRating.setText(rating.toString());
                userEmail.setText(email);



            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });

    }




}
