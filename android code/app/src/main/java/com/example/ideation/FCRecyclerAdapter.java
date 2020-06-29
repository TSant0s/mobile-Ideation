package com.example.ideation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.example.ideation.Retrofit.INodeJS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCRecyclerAdapter extends RecyclerView.Adapter<FCRecyclerAdapter.ChallengeFeedbackViewHolder> {

    static private ArrayList<FeedbackChallenge> feedbackChallengeList;

    INodeJS api;
    private Context mContext;
    private RequestQueue requestQueue;
    private String ratingCount;
    private String loggedUserID = GroupsFragment.userID;

    ArrayList<Integer> cFeedbacksRating = new ArrayList<>();





    public FCRecyclerAdapter(ArrayList<FeedbackChallenge> feedbackChallengeList, Context mContext) {
        this.feedbackChallengeList = feedbackChallengeList;
        this.mContext = mContext;
    }



    public static class ChallengeFeedbackViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewChallengeFeedbackText;
        public TextView textViewChallengeFeedbackRating;
        public TextView textViewChallengeFeedbackUsername;
        public LikeButton likeButton;




        CardView feedbackChallengeItem;




        public ChallengeFeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChallengeFeedbackText= itemView.findViewById(R.id.textView_challenge_feedback_text);
            textViewChallengeFeedbackRating= itemView.findViewById(R.id.textView_challenge_feedback_rating);
            textViewChallengeFeedbackUsername= itemView.findViewById(R.id.textView_challenge_feedback_username);
            likeButton=itemView.findViewById(R.id.like_button_challengeFeedback);
            feedbackChallengeItem=itemView.findViewById(R.id.challenge_feedback_item);
        }
    }

    @NonNull
    @Override
    public ChallengeFeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_challenge_item,parent,false);
        ChallengeFeedbackViewHolder fcvh= new ChallengeFeedbackViewHolder(v);
        return fcvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeFeedbackViewHolder holder, int position) {

        FeedbackChallenge currentFeedbackChallenge = feedbackChallengeList.get(position);
        getChallengeFeedbackLikes(holder,loggedUserID,currentFeedbackChallenge.getcFeedbackID());
        getChallengeFeedbackRating(holder,currentFeedbackChallenge.getcFeedbackID());





        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

             String data =
                        "{\"cFeedbackID\":"+currentFeedbackChallenge.getcFeedbackID()+",\"userID\":"+loggedUserID+"}";
             String data1 =
                        "{\"userID\":"+currentFeedbackChallenge.getcUserID_fk()+"}";
                String data2 =
                        "{\"userID\":"+currentFeedbackChallenge.getcUserID_fk()+"}";

             increaseRatingCFb(data);
             increaseUserRating(data1);
             increaseUserFunds(data2);
             likeButton.setLiked(true);


            }

            @Override
            public void unLiked(LikeButton likeButton) {
                getChallengeFeedbackRating(holder,currentFeedbackChallenge.getcFeedbackID());



                String data =
                        "{\"cFeedbackID\":"+currentFeedbackChallenge.getcFeedbackID()+",\"userID\":"+loggedUserID+"}";

                String data2 =
                        "{\"userID\":"+currentFeedbackChallenge.getcUserID_fk()+"}";

                decreaseRatingCFb(data);
                decreaseUserFunds(data2);



            }
        });
        holder.textViewChallengeFeedbackText.setText(currentFeedbackChallenge.getcFeedbackText());
        holder.textViewChallengeFeedbackUsername.setText("Creator:\n "+currentFeedbackChallenge.getEmail());





    }



    @Override
    public int getItemCount() {
        return feedbackChallengeList.size();
    }


    public static ArrayList<FeedbackChallenge> getFeedbackChallengeList() {
        return feedbackChallengeList;
    }


    public static void addFeedbackChallengeList(FeedbackChallenge feedbackChallenge) {
        feedbackChallengeList.add(feedbackChallenge);
    }


    public void getChallengeFeedbackRating(@NonNull ChallengeFeedbackViewHolder holder, int challengeFeedbackID) {

        String url = "http://192.168.1.9:3000/feedback/challenge/rating/" + challengeFeedbackID + "";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        try {
                            Log.d("Response", String.valueOf(response.getJSONObject(0).getInt("NumberOfLikes")));
                            ratingCount = String.valueOf(response.getJSONObject(0).getInt("NumberOfLikes"));
                            holder.textViewChallengeFeedbackRating.setText(ratingCount);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        requestQueue.add(getRequest);




    }

    public void getChallengeFeedbackLikes(@NonNull ChallengeFeedbackViewHolder holder, String loggedUserID, int cFeedbackID) {
        String url = "http://192.168.1.9:3000/challenge/feedback/likes";
        requestQueue = Volley.newRequestQueue(mContext);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        for(int i = 0; i< response.length();i++){
                            try {
                                JSONObject x;
                                x = response.getJSONObject(i);
                                if(x.getInt("userID_fk") == Integer.parseInt(loggedUserID) && x.getInt("cFeedbackID_fk") == cFeedbackID){
                                    System.out.println("---------------------------O id do like é: "+x.getInt("likeID"));
                                    holder.likeButton.setLiked(true);


                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("Response do metodo bacano vei", response.toString());




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        requestQueue.add(getRequest);





    }





    public void increaseRatingCFb(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/feedback/challenges/increaseRating";

        requestQueue = Volley.newRequestQueue(mContext);
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



    public void decreaseRatingCFb(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/feedback/challenges/decreaseRating";

        requestQueue = Volley.newRequestQueue(mContext);
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




    public void increaseUserRating(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/increaseRating";

        requestQueue = Volley.newRequestQueue(mContext);
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


    public void increaseUserFunds(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/increaseFunds";

        requestQueue = Volley.newRequestQueue(mContext);
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


    public void decreaseUserFunds(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/decreaseFunds";

        requestQueue = Volley.newRequestQueue(mContext);
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




}
