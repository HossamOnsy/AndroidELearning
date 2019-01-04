package com.salwa.androidelearning.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.salwa.androidelearning.models.QuizModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizzesActivity extends AppCompatActivity {
    String databasePath = "Quizzes";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ArrayList<QuizModel> quizModels = new ArrayList<>();
    DatabaseReference ref1;
    DatabaseReference ref2;
    private CustomAdapter mAdapter ;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);
        ButterKnife.bind(this);



        if (getIntent().hasExtra("databasePath")) {
            databasePath = getIntent().getStringExtra("databasePath");
        }
        ref1 = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        quizModels = new ArrayList<QuizModel>();
        mAdapter = new CustomAdapter(quizModels , QuizzesActivity.this,databasePath);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ref2 = ref1.child(databasePath);
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    QuizModel quizModel = dsp.getValue(QuizModel.class);
//                    Userlist.add(user.getName());
//                    NameIdModel nameIdModel = new NameIdModel();
//                    nameIdModel.setName(user.getName());
//                    nameIdModel.setID(user.getId());
                    quizModels.add(quizModel);

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
