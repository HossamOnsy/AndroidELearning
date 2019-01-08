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
import com.salwa.androidelearning.TeacherActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonsAndActivitiesActivity extends AppCompatActivity {

    String databasePath = "Lessons";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ArrayList<String> stringArrayList = new ArrayList<>();
    DatabaseReference ref1;
    DatabaseReference ref2;
    private CustomAdapter mAdapter ;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager layoutManager;
    String path = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_and_activities);
        ButterKnife.bind(this);



        if (getIntent().hasExtra("databasePath")) {
            databasePath = getIntent().getStringExtra("databasePath");
        }
        if(getIntent().hasExtra("Subject")){
            path = getIntent().getStringExtra("Subject");
        }
        ref1 = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        stringArrayList = new ArrayList<String>();
        mAdapter = new CustomAdapter(stringArrayList , LessonsAndActivitiesActivity.this,databasePath);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ref2 = ref1.child(path).child(databasePath);
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String stringTest = dsp.getValue(String.class);
//                    Userlist.add(user.getName());
//                    NameIdModel nameIdModel = new NameIdModel();
//                    nameIdModel.setName(user.getName());
//                    nameIdModel.setID(user.getId());
                    stringArrayList.add(stringTest);

                    mAdapter.notifyDataSetChanged();
//                    ArrayAdapter adapter = new ArrayAdapter(LessonsAndActivitiesActivity.this, android.R.layout.simple_spinner_item, Userlist);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}
