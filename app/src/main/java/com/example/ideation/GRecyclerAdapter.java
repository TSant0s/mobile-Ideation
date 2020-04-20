package com.example.ideation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRecyclerAdapter extends RecyclerView.Adapter<GRecyclerAdapter.GroupViewHolder> {
    private List<Group> gGroupList;
    private Context mContext;



    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewGroupName;
         CardView group_item;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewGroupName = itemView.findViewById(R.id.textView_group_name);
            group_item = itemView.findViewById(R.id.group_item);
        }
    }


    public GRecyclerAdapter(List<Group> gGroupList, Context mContext){
        this.gGroupList=gGroupList;
        this.mContext=mContext;
    }
    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);
        GroupViewHolder gvh = new GroupViewHolder(v);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
    Group currentGroup = gGroupList.get(position);
    holder.group_item.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, IdeaPage.class);
            intent.putExtra("name",gGroupList.get(position).getName());
            intent.putExtra("description",gGroupList.get(position).getDescription());
            mContext.startActivity(intent);
        }
    });
    holder.textViewGroupName.setText(currentGroup.getName());
    }

    @Override
    public int getItemCount() {
        return gGroupList.size();
    }
}
