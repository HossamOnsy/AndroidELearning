package com.salwa.androidelearning;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salwa.androidelearning.activity.ProgressActivity;
import com.salwa.androidelearning.activity.QuizActivity;
import com.salwa.androidelearning.activity.QuizzesActivity;
import com.salwa.androidelearning.activity.VideoPreview;
import com.salwa.androidelearning.models.QuizModel;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList list;
    android.content.Context Context;
    String fromActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name1);

        }
    }


    public CustomAdapter(ArrayList list, Context Context, String fromActivity) {
        this.Context = Context;
        this.list = list;
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

        int t = position + 1;
        switch (fromActivity) {
            case "Lessons": {
                holder.name.setText("Lesson : " + t);
                break;
            }
            case "Activities": {
                holder.name.setText("Activity : " + t);
                break;
            }
            case "Teacher": {
                holder.name.setText(list.get(position).toString());
                break;
            }
            case "Quizzes": {

                holder.name.setText("Quiz : " + t);

                break;

            }
            default: {
                holder.name.setText(list.get(position).toString());
                break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (fromActivity) {
                    case "Lessons": {
                        Context.startActivity(new Intent(Context, VideoPreview.class).putExtra("uri", list.get(position).toString())
                                .putExtra("type_Of_Actvitiy", "Checked " + "Lesson: " + t));
                        break;
                    }
                    case "Activities": {
                        Context.startActivity(new Intent(Context, VideoPreview.class).putExtra("uri", list.get(position).toString())
                                .putExtra("type_Of_Actvitiy", "Checked " + "Activity: " + t));
                        break;
                    }
                    case "Teacher": {
                        Context.startActivity(new Intent(Context, ProgressActivity.class).putExtra("uri", list.get(position).toString()));
                        break;
                    }
                    case "Quizzes": {
                        Context.startActivity(new Intent(Context, QuizActivity.class).putExtra("quizModel", (QuizModel) list.get(position))
                                .putExtra("type_Of_Actvitiy", "Checked " + "Quiz: " + t));

                    }
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}


