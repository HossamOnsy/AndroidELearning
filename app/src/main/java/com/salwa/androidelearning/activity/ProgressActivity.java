package com.salwa.androidelearning.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.CustomAdapter;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.feedback_btn)
    Button feedbackBtn;
    @BindView(R.id.profile_ofstudent)
    Button profileOfstudent;
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

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        SharedPreferences settings = getSharedPreferences("defauty", MODE_PRIVATE);
        String value = settings.getString("key", "");


            dialogBuilder.setTitle("Feed Back");
            dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref2 = ref1.child("Students");

                ref2.child(uid).child("teacherfeedback").setValue(edt.getText().toString());

                feedbackBtn.setEnabled(false);
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }



    @OnClick({R.id.profile_ofstudent, R.id.feedback_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_ofstudent:
                startActivity(new Intent(ProgressActivity.this,StudentProfile.class).putExtra("id",uid
                ));
                break;
            case R.id.feedback_btn:
                showChangeLangDialog();
                break;
        }
    }
}
