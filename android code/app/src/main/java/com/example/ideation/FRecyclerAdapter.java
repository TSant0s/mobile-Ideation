package com.example.ideation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.AccessControlContext;
import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Callback;

public class FRecyclerAdapter extends RecyclerView.Adapter<FRecyclerAdapter.FeedbackViewHolder> {

    private List<Feedback> feedbackList;
    private Context mcontext;
    private String email;
    private String ratingCount;
    private RequestQueue requestQueue;
    private String loggedUserID = GroupsFragment.userID;


    public FRecyclerAdapter(List<Feedback> feedbackList, Context mcontext) {
        this.feedbackList = feedbackList;
        this.mcontext = mcontext;
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewFeedbackText;
        public TextView textViewFeedbackRating;
        public TextView textViewFeedbackUsername;
        public LikeButton likeButtonGroupFeedback;
        CardView feedback_item;
        private String userEmail = MainActivity.getLoggedUserEmail();



        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFeedbackText=itemView.findViewById(R.id.textView_feedback_text);
            textViewFeedbackRating=itemView.findViewById(R.id.textView_feedback_rating);
            textViewFeedbackUsername=itemView.findViewById(R.id.textView_feedback_username);
            feedback_item=itemView.findViewById(R.id.feedback_item);
            likeButtonGroupFeedback=itemView.findViewById(R.id.like_button_groupFeedback);



        }
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);

        FeedbackViewHolder fvh = new FeedbackViewHolder(v);



        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback currentFeedback = feedbackList.get(position);
        getFeedbackLikes(holder,loggedUserID,currentFeedback.getFeedbackID());
        getFeedbackRating(holder,currentFeedback.getFeedbackID());




        holder.likeButtonGroupFeedback.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String data =
                        "{\"feedbackID\":"+currentFeedback.getFeedbackID()+",\"userID\":"+loggedUserID+"}";
                String data1 =
                        "{\"userID\":"+currentFeedback.getUserId()+"}";

                increaseUserFunds(data1);
                increaseUserRating(data1);
                increaseRatingFb(data);
                likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String data =
                        "{\"feedbackID\":"+currentFeedback.getFeedbackID()+",\"userID\":"+loggedUserID+"}";
                String data1 =
                        "{\"userID\":"+currentFeedback.getUserId()+"}";
                decreaseUserFunds(data1);
                decreaseRatingFb(data);

            }
        });
        holder.textViewFeedbackUsername.setText("Creator:\n "+currentFeedback.getFeedbackUserEmail());
        holder.textViewFeedbackText.setText(currentFeedback.getText());
        holder.textViewFeedbackRating.setText(currentFeedback.getRating().toString().substring(0,2));




    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }



    public void getFeedbackLikes(@NonNull FRecyclerAdapter.FeedbackViewHolder holder, String loggedUserID, int cFeedbackID) {
        String url = "http://192.168.1.9:3000/groups/feedback/likes";
        requestQueue = Volley.newRequestQueue(mcontext);
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
                                if(x.getInt("userID_fk") == Integer.parseInt(loggedUserID) && x.getInt("feedbackID_fk") == cFeedbackID){
                                    System.out.println("---------------------------O id do like é: "+x.getInt("likeID"));
                                    holder.likeButtonGroupFeedback.setLiked(true);


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


    public void getFeedbackRating(@NonNull FRecyclerAdapter.FeedbackViewHolder holder, int feedbackID) {

        String url = "http://192.168.1.9:3000/feedback/group/rating/" + feedbackID + "";
        requestQueue = Volley.newRequestQueue(mcontext);
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        try {
                            Log.d("Response", String.valueOf(response.getJSONObject(0).getInt("NumberOfLikes")));
                            ratingCount = String.valueOf(response.getJSONObject(0).getInt("NumberOfLikes"));
                            holder.textViewFeedbackRating.setText(ratingCount);




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




    public void increaseRatingFb(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/feedback/groups/increaseRating";

        requestQueue = Volley.newRequestQueue(mcontext);
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





    public void decreaseRatingFb(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/feedback/groups/decreaseRating";

        requestQueue = Volley.newRequestQueue(mcontext);
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


    public void changeRatingFb(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/feedback/group/newRating";

        requestQueue = Volley.newRequestQueue(mcontext);
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
        requestQueue.add(stringRequest);


    }


    public void increaseUserRating(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/increaseRating";

        requestQueue = Volley.newRequestQueue(mcontext);
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

        requestQueue = Volley.newRequestQueue(mcontext);
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

        requestQueue = Volley.newRequestQueue(mcontext);
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
