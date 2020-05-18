package com.example.ideation;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

import static java.security.AccessController.getContext;

public class IdeaPage extends AppCompatActivity {
    private FloatingActionButton btnNewComment;
    private RecyclerView feedbackRecyclerView;
    private RecyclerView.Adapter feedbackAdapter;
    private RecyclerView.LayoutManager feedbackLayoutManager = new LinearLayoutManager(this);
    private SwipeRefreshLayout swipeRefreshLayout;


    ArrayList<Feedback> feedbacks = new ArrayList<>();
    String c ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_page);

        String ideaName = getIntent().getExtras().getString("name");
        String ideaDescription = getIntent().getExtras().getString("description");

        TextView idea_name = findViewById(R.id.textView_idea_name);
        TextView idea_description = findViewById(R.id.textView_idea_description);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout_ideaFeedback);

        feedbackRecyclerView = findViewById(R.id.feedback_RecyclerView);
        btnNewComment = findViewById(R.id.button_new_comment);
        feedbackRecyclerView.setHasFixedSize(true);

        idea_name.setText(ideaName);
        idea_description.setText(ideaDescription);



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                INodeJS api  = retrofit.create(INodeJS.class);


                Call<List<Feedback>> callFeedback = api.getGroupFeedback("1");
                callFeedback.enqueue(new Callback<List<Feedback>>() {
                    @Override
                    public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                        if(!response.isSuccessful()){
                            System.out.println("!= Successful" + response.code());
                            return;
                        }

                        feedbacks = (ArrayList<Feedback>) response.body();

                        setupRecyclerView();





                    }






                    public void setupRecyclerView(){
                        feedbackAdapter = new FRecyclerAdapter(feedbacks,getBaseContext());
                        feedbackRecyclerView.setLayoutManager(feedbackLayoutManager);
                        feedbackRecyclerView.setAdapter(feedbackAdapter);


                    }

                    @Override
                    public void onFailure(Call<List<Feedback>> call, Throwable t) {
                        System.out.println("OnFailure" + t.getMessage());
                    }
                });


                swipeRefreshLayout.setRefreshing(false);

            }
        });

        btnNewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackDialog();
            }

            private void openFeedbackDialog() {
                FeedbackDialog feedbackDialog = new FeedbackDialog();
                feedbackDialog.show(getSupportFragmentManager(),"new Feedback");
                //Não e suposto ser supportfragment managaer mas sim so fragment manager
            }
        });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api  = retrofit.create(INodeJS.class);


        Call<List<Feedback>> callFeedback = api.getGroupFeedback("1");
        callFeedback.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if(!response.isSuccessful()){
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                feedbacks = (ArrayList<Feedback>) response.body();

                setupRecyclerView();





            }






                 public void setupRecyclerView(){
            feedbackAdapter = new FRecyclerAdapter(feedbacks,getBaseContext());
            feedbackRecyclerView.setLayoutManager(feedbackLayoutManager);
            feedbackRecyclerView.setAdapter(feedbackAdapter);


        }

        @Override
        public void onFailure(Call<List<Feedback>> call, Throwable t) {
            System.out.println("OnFailure" + t.getMessage());
        }
    });



    }


}
