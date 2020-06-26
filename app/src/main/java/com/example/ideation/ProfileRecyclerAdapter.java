package com.example.ideation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileRecyclerAdapter extends RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileViewHolder>{
    private List<Transaction> tList;
    private Context mContext;

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        private TextView rName, rPrice;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            rName = itemView.findViewById(R.id.textView_item_name);
            rPrice = itemView.findViewById(R.id.textView_item_price);

        }
    }

    public ProfileRecyclerAdapter(List<Transaction> tList, Context mContext) {
        this.tList = tList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transacation_item,parent,false);
        ProfileViewHolder pvh = new ProfileViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Transaction currentTransaction = tList.get(position);
        holder.rName.setText(currentTransaction.getName());
        holder.rPrice.setText(currentTransaction.getPrice().toString());
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }
}
