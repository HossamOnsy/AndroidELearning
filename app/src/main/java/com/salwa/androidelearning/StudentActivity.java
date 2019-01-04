package com.salwa.androidelearning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.models.NameIdModel;
import com.salwa.androidelearning.models.User;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.salwa.androidelearning.pref.getsharedPref;

public class StudentActivity extends AppCompatActivity {
    SharedPreferences myPref;
    User user;

    FirebaseAuth auth;

    DatabaseReference ref1;
    DatabaseReference ref2;
    ArrayList<NameIdModel> NameIdList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);

        myPref = getsharedPref(StudentActivity.this);
        String name = myPref.getString(pref.name, "name");
        user = getIntent().getParcelableExtra("model");
        auth = FirebaseAuth.getInstance();
//        Log.v("heyyy",user.teacher);
        ref1 = FirebaseDatabase.getInstance().getReference();
        if (user.getTeacher()==null||user.getTeacher().equals("")){
            onCreateDialog();
        }






    }
    int selected=0;

    public void onCreateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = StudentActivity.this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_location, null);
        final Spinner spinnerd = view.findViewById(R.id.spinnerd);

        ref2 = ref1.child("Teachers");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList Userlist = new ArrayList<String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User user = dsp.getValue(User.class);
                    Userlist.add(user.getName());
                    NameIdModel nameIdModel = new NameIdModel();
                    nameIdModel.setName(user.getName());
                    nameIdModel.setID(user.getId());
                    NameIdList.add(nameIdModel);

                    ArrayAdapter adapter = new ArrayAdapter(StudentActivity.this, android.R.layout.simple_spinner_item, Userlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerd.setAdapter(adapter);
                    spinnerd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String country = parent.getItemAtPosition(position).toString();
                            selected = position;

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

                            setStudentToTeacher(name,userId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
               ;
        builder.create().show();
    }
    void setStudentToTeacher(String name, String userId){

        ref2 = ref1.child("users");
//        DatabaseReference databaseReference = ref2.orderByChild("name").equalTo(name).getRef();
        NameIdModel nameIdModel = NameIdList.get(selected);
        DatabaseReference databaseReference = ref2.child(nameIdModel.getID()).child("students");
//        String key = databaseReference.push().getKey();
//        Log.v("testing","key -> "+key);

        NameIdModel studentProgress = new NameIdModel();
        studentProgress.setName(name);
        studentProgress.setID(userId);
        studentProgress.setProgress("0");
        databaseReference.child(userId).setValue(studentProgress, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError != null) {
                    Log.v("testing","message -> "+databaseError.getMessage());
                }
                Log.v("testing","key -> "+databaseReference.getKey());
                Log.v("testing","path -> "+databaseReference.getPath().toString());
            }
        });

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
//            case R.id.change:
//                onCreateDialog();
//                return true;
            default:
                onCreateDialog();
                return super.onOptionsItemSelected(item);
        }
    }

}



