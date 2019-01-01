package com.salwa.androidelearning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.salwa.androidelearning.pref.getsharedPref;

public class StudentActivity extends AppCompatActivity {
    SharedPreferences myPref;
    User user;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);

        myPref = getsharedPref(StudentActivity.this);
        String name = myPref.getString(pref.name, "name");
        user = getIntent().getParcelableExtra("model");
        Log.v("heyyy",user.teacher);
        if (user.teacher.equals("")){
            onCreateDialog();
        }






    }

    public void onCreateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = StudentActivity.this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_location, null);
        final Spinner spinnerd = view.findViewById(R.id.spinnerd);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2;
        ref2 = ref1.child("users");
        ref2.orderByChild("role").equalTo("Teacher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    Userlist.add(user.name);
                    ArrayAdapter adapter = new ArrayAdapter(StudentActivity.this, android.R.layout.simple_spinner_item, Userlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerd.setAdapter(adapter);
                    spinnerd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String country = parent.getItemAtPosition(position).toString();


                            myPref = getsharedPref(StudentActivity.this);
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putString(pref.name, country);
                            editor.apply();
                            editor.commit();
                            String name1 = myPref.getString(pref.name, "Default");
                            Toast.makeText(StudentActivity.this, name1, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setView(view)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            myPref = getsharedPref(StudentActivity.this);
                            String name = myPref.getString(pref.name, "name");
                            Log.v("heyyy",name);
                            auth = FirebaseAuth.getInstance();
                            String userId = auth.getCurrentUser().getUid();
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref2;
                            ref2 = ref1.child("users");
                            myPref = getsharedPref(StudentActivity.this);
                            ref2.child(userId).child("teacher").setValue(name);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
               ;
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.changet:
                onCreateDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



