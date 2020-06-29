package com.example.ideation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LbRecyclerAdapter extends RecyclerView.Adapter<LbRecyclerAdapter.LbViewHolder> {
    private List<User> userList;
    private Context mContext;

    public static class LbViewHolder extends RecyclerView.ViewHolder{

        public TextView userName;
        public TextView userEmail;
        public TextView userRating;

        public LbViewHolder(@NonNull View itemView){
            super(itemView);
            userName  = itemView.findViewById(R.id.textView_lb_name);
            userEmail  = itemView.findViewById(R.id.textView_lb_email);
            userRating = itemView.findViewById(R.id.textView_lb_rating);
        }
    }

    public LbRecyclerAdapter(List<User> userList, Context mContext) {
        this.userList = userList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LbViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item,parent,false);
        LbViewHolder lbv = new LbViewHolder(v);
        return lbv;
    }

    @Override
    public void onBindViewHolder(@NonNull LbViewHolder holder, int position) {
        User currentUser = userList.get(position);
        holder.userName.setText(currentUser.getName());
        holder.userEmail.setText(currentUser.getEmail());
        holder.userRating.setText(currentUser.getRating().toString());

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
