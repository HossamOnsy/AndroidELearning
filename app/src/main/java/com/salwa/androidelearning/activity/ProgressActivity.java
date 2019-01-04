package com.salwa.androidelearning.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.CustomAdapter;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.StudentModel;
import com.salwa.androidelearning.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private CustomAdapter mAdapter;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager layoutManager;
    ArrayList list;
    User user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ButterKnife.bind(this);
        list = new ArrayList<String>();
        mAdapter = new CustomAdapter(list, ProgressActivity.this, "");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
//        user = getIntent().getParcelableExtra("model");
        String name = getIntent().getStringExtra("name");
        uid = getIntent().getStringExtra("uid");

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("Progress");
        ref2.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String user = dsp.getValue(String.class);
                    list.add(user);

                    mAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
