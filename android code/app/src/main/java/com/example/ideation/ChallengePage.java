package com.example.ideation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideation.Retrofit.INodeJS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChallengePage extends AppCompatActivity {
    private static final int STORAGE_CODE =1000 ;
    private FloatingActionButton btnNewFeedbackChallenge;
    private RecyclerView challengeFeedbackRecyclerView;
    private RecyclerView.Adapter challengeFeedbackAdapter;
    private RecyclerView.LayoutManager challengeFeedbackLayoutManager = new LinearLayoutManager(this);
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnGetChallengeFeedback;
    private int clickedChallengeID = CRecyclerAdapter.getClickedChallengeID();

    private String loggedUserID = GroupsFragment.userID;


    ArrayList<FeedbackChallenge> cfeedbacks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_page);

        ActivityCompat.requestPermissions(ChallengePage.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        TextView textViewChallengeDescription, textViewChallengeInstructions, textViewChallengeEndDate, textViewChallengeTitle;
        String challengeDescription, challengeInstructions, challengeEndDate, challengeTitle;
        int challengeCreator;


        textViewChallengeDescription = findViewById(R.id.textView_challengePage_description);
        textViewChallengeTitle = findViewById(R.id.textView_challengePage_title);
        textViewChallengeInstructions = findViewById(R.id.textView_challengePage_instructions);
        textViewChallengeEndDate = findViewById(R.id.textView_challengePage_endDate);
        btnGetChallengeFeedback = findViewById(R.id.button_get_challenge_feedback);
        challengeFeedbackRecyclerView = findViewById(R.id.recyclerView_feedback_challenge);
        btnNewFeedbackChallenge = findViewById(R.id.button_new_challengeFeedback);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_challengeFeedback);


        challengeDescription = getIntent().getExtras().getString("challengeDescription");
        challengeTitle = getIntent().getExtras().getString("title");
        challengeInstructions = getIntent().getExtras().getString("instructions");
        challengeEndDate = getIntent().getExtras().getString("endDate");
        challengeCreator=getIntent().getExtras().getInt("challenge_userID") ;



        textViewChallengeDescription.setText("Description: \n" + challengeDescription);
        textViewChallengeTitle.setText(challengeTitle);
        textViewChallengeInstructions.setText("Instructions: \n " + challengeInstructions);
        textViewChallengeEndDate.setText(challengeEndDate.substring(0, 10));


        getFeedbacks();
        System.out.println("creator- "+challengeCreator+"\n"+"loggeduserid: "+loggedUserID);
        if(challengeCreator != Integer.parseInt(loggedUserID)){
            btnGetChallengeFeedback.setVisibility(View.INVISIBLE);
        }


        Date sqlEndDate=java.sql.Date.valueOf(challengeEndDate.substring(0,10));
        java.sql.Date curDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        if(curDate.after(sqlEndDate)){
            btnNewFeedbackChallenge.setVisibility(View.INVISIBLE);
        }
        System.out.println(curDate+"      "+sqlEndDate);





        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeedbacks();

                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(),"Refreshed!", Toast.LENGTH_SHORT).show();

            }
        });

        btnNewFeedbackChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedbackChallengeDialog();
            }

            private void openFeedbackChallengeDialog() {
                ChallengeFeedbackDialog challengeFeedbackDialog = new ChallengeFeedbackDialog();
                challengeFeedbackDialog.show(getSupportFragmentManager(), "new Feedback");
            }

        });


        btnGetChallengeFeedback.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions,STORAGE_CODE);

                } else{
                    savePdf(cfeedbacks);

                }

            } else{
                savePdf(cfeedbacks);

            }
            getFeedbacks();



        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case STORAGE_CODE:{
                if(grantResults.length < 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    savePdf(cfeedbacks);

                }else{
                    Toast.makeText(this, "Permission Denied..!", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    //----------------------------------------------------
    public void getFeedbacks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INodeJS api = retrofit.create(INodeJS.class);


        Call<List<FeedbackChallenge>> callChallengeFeedback = api.getChallengeFeedback(clickedChallengeID);
        callChallengeFeedback.enqueue(new Callback<List<FeedbackChallenge>>() {
            @Override
            public void onResponse(Call<List<FeedbackChallenge>> call, Response<List<FeedbackChallenge>> response) {
                if (!response.isSuccessful()) {
                    System.out.println("!= Successful" + response.code());
                    return;
                }

                cfeedbacks = (ArrayList<FeedbackChallenge>) response.body();
                setupRecyclerView();


            }

            @Override
            public void onFailure(Call<List<FeedbackChallenge>> call, Throwable t) {
                System.out.println("OnFailure" + t.getMessage());
            }
        });

    }


    public void setupRecyclerView() {
        challengeFeedbackAdapter = new FCRecyclerAdapter(cfeedbacks, getBaseContext());
        challengeFeedbackRecyclerView.setLayoutManager(challengeFeedbackLayoutManager);
        challengeFeedbackRecyclerView.setAdapter(challengeFeedbackAdapter);


    }






    private void savePdf(ArrayList<FeedbackChallenge>f)  {
        Document mDoc = new Document();
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath = Environment.getExternalStorageDirectory() + "/" +mFileName+ ".pdf";

        try{
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();

            mDoc.addTitle("Challenge Results");
            mDoc.add(new Paragraph("RESULTS OF YOUR CHALLENGE: "));
            mDoc.add(new Paragraph("\n"));
            for(FeedbackChallenge i: f){
                mDoc.add(new Paragraph("Created by: "+ i.getEmail()+" with: "+i.getcFeedbackRating()+" Votes"));
                mDoc.add(new Paragraph(i.getcFeedbackText()));
                mDoc.add(new Paragraph("\n"));
                mDoc.add(new Paragraph("\n"));
            }
            mDoc.close();
            Toast.makeText(this, mFileName+".pdf\nFile Saved", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}