package com.salwa.androidelearning;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private ArrayList list;
    android.content.Context Context ;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name ;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name1);

        }
    }


    public StudentAdapter(ArrayList list , Context Context)
    {
        this.Context = Context;
        this.list =list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.name.setText(list.get(position).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}


