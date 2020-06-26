package com.example.ideation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.StoreViewHolder> {

    private List<Reward> rewardList;
    private Context mContext;
    private RequestQueue requestQueue;
    private String loggedUserID = GroupsFragment.userID;
    private Double userFunds = GroupsFragment.funds;

    public static class StoreViewHolder extends RecyclerView.ViewHolder{

        public TextView rewardName;
        public TextView rewardPrice;
        private ImageButton btnBuy;

        public StoreViewHolder(@NonNull View itemView){
            super(itemView);
            rewardName = itemView.findViewById(R.id.textView_reward_name);
            rewardPrice = itemView.findViewById(R.id.textView_reward_price);
            btnBuy = itemView.findViewById(R.id.imageButton_buy_reward);
        }


    }

    public StoreRecyclerAdapter(List<Reward> rewardList, Context mContext){
        this.rewardList = rewardList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_item,parent,false);
        StoreViewHolder svh = new StoreViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Reward currentReward = rewardList.get(position);
        holder.rewardName.setText(currentReward.getName());
        holder.rewardPrice.setText(currentReward.getPrice().toString());

        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userFunds >= currentReward.getPrice()){
                    String data =
                            "{\"userID\":"+loggedUserID+",\"rewardID\":"+currentReward.getRewardID()+"}";
                    String data1 =
                            "{\"value\":"+currentReward.getPrice()+",\"userID\":"+loggedUserID+"}";
                    newTransaction(data);
                    decreaseUserCoins(data1);
                    Toast.makeText(mContext, currentReward.getName()+"  was redeemed! :)", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "You don't have enough Coins to buy this! :( ", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }


    public void newTransaction(String data){
            System.out.println("BOAS AMIGO");
            final String savedata = data;
            String URL ="http://192.168.1.9:3000/newTransaction";

            requestQueue = Volley.newRequestQueue(mContext);

              StringRequest  stringRequest = new StringRequest(Request.Method.POST, URL, response -> {

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


    public void decreaseUserCoins(String data) {
        final String savedata = data;
        String URL ="http://192.168.1.9:3000/user/decreaseCoins";

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
