package com.salwa.androidelearning.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.NameIdModel;
import com.salwa.androidelearning.models.StudentModel;
import com.salwa.androidelearning.models.User;
import com.salwa.androidelearning.pref;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.salwa.androidelearning.pref.getsharedPref;

public class StudentMainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @BindView(R.id.lesson_btn)
    Button lessonBtn;
    @BindView(R.id.activity_btn)
    Button activityBtn;
    @BindView(R.id.quiz_btn)
    Button quizBtn;
    @BindView(R.id.guideline1)
    Guideline guideline1;
    @BindView(R.id.feedback_txt)
    TextView feedbackTxt;
    @BindView(R.id.left_drawer)
    ListView leftDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private DatabaseReference mDatabase;
    SharedPreferences myPref;


    StudentModel studentModel;
    ActionBarDrawerToggle mDrawerToggle;


    DatabaseReference ref1;
    DatabaseReference ref2;
    ArrayList<NameIdModel> NameIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        ButterKnife.bind(this);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
//        leftDrawer = (ListView) findViewById(R.id.left_drawer);
        leftDrawer.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(StudentMainActivity.this, StudentProfile.class));

                        break;

                    case 1:
                        finishAffinity();
                        startActivity(new Intent(StudentMainActivity.this, TeacherOrStudentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
//                messageTextView.setText("Menu Item at position " + position + " clicked.");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });


        myPref = getsharedPref(StudentMainActivity.this);
        final String TAG = "erorrr";
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = mDatabase.child("Students");
        String userId = auth.getCurrentUser().getUid();


        myPref = getsharedPref(StudentMainActivity.this);
        String name = myPref.getString(pref.name, "name");
//        user = getIntent().getParcelableExtra("model");
        auth = FirebaseAuth.getInstance();
//        Log.v("heyyy",user.teacher);
        ref1 = FirebaseDatabase.getInstance().getReference();

        getModelFromDatabase();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void getModelFromDatabase() {


        ref1.child("Students").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                studentModel = dataSnapshot.getValue(StudentModel.class);
                if (studentModel != null) {
                    if (studentModel.getTeacherfeedback() != null && !studentModel.getTeacherfeedback().equals("") &&
                            studentModel.getTeacher() != null && !studentModel.getTeacher().equals("")) {
                        feedbackTxt.setText(String.format("%s : %s", studentModel.getTeacher(), studentModel.getTeacherfeedback()));
                    }
                }

                if (studentModel.getTeacher() == null || studentModel.getTeacher().equals("")) {
                    onCreateDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    int selected = 0;

    public void onCreateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StudentMainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = StudentMainActivity.this.getLayoutInflater();

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

                    ArrayAdapter adapter = new ArrayAdapter(StudentMainActivity.this, android.R.layout.simple_spinner_item, Userlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinnerd.setAdapter(adapter);
                    spinnerd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String country = parent.getItemAtPosition(position).toString();
                            selected = position;

                            myPref = getsharedPref(StudentMainActivity.this);
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putString(pref.name, country);
                            editor.apply();
                            editor.commit();
                            String name1 = myPref.getString(pref.name, "Default");
                            Toast.makeText(StudentMainActivity.this, name1, Toast.LENGTH_SHORT).show();
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
                .setPositiveButton("OK", (dialog, id) -> {
                    try {
                        myPref = getsharedPref(StudentMainActivity.this);
                        String name = myPref.getString(pref.name, "name");
                        Log.v("heyyy", name);
                        auth = FirebaseAuth.getInstance();
                        String userId = auth.getCurrentUser().getUid();
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference ref2;
                        ref2 = ref1.child("Students");
                        myPref = getsharedPref(StudentMainActivity.this);
                        ref2.child(userId).child("teacher").setValue(name);
//                            setStudentToTeacher(name,userId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        builder.create().show();
    }

    @OnClick({R.id.lesson_btn, R.id.activity_btn, R.id.quiz_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lesson_btn:
                startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("databasePath", "Lessons"));
                break;
            case R.id.activity_btn:
                startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("databasePath", "Activities"));
                break;
            case R.id.quiz_btn:
                startActivity(new Intent(StudentMainActivity.this, QuizzesActivity.class).putExtra("databasePath", "Quizzes"));
                break;
        }
    }
}
