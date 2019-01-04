package com.salwa.androidelearning.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salwa.androidelearning.R;
import com.salwa.androidelearning.activity.ProgressActivity;
import com.salwa.androidelearning.activity.VideoPreview;
import com.salwa.androidelearning.models.StudentModel;

import java.util.ArrayList;

public class CustomAdapterForProgress extends RecyclerView.Adapter<CustomAdapterForProgress.MyViewHolder> {

    private ArrayList<StudentModel> list;
    android.content.Context Context ;
    String fromActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name ;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name1);

        }
    }


    public CustomAdapterForProgress(ArrayList<StudentModel> list , Context Context, String fromActivity)
    {
        this.Context = Context;
        this.list =list;
        this.fromActivity = fromActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        int t = position+1;
        switch (fromActivity){
            case "Lessons" : {
                holder.name.setText("Lesson : " + t);
                break;
            }
            case "Activities" : {
                holder.name.setText("Activity : " + t);
                break;
            }
            case "Teacher" : {
                holder.name.setText(list.get(position).getName());
            break;
        }
            default :{
                holder.name.setText(list.get(position).getName());
                break;
            }
        }

        holder.itemView.setOnClickListener(v -> Context.startActivity(new Intent(Context,ProgressActivity.class).putExtra("uid",list.get(position).getID())
                .putExtra("name",list.get(position).getName())));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}


