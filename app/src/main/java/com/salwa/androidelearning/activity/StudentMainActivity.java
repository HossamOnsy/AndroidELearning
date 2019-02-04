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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
    @BindView(R.id.cardview)
    CardView cardview;
    @BindView(R.id.buttonscontainer)
    LinearLayout buttonscontainer;
    @BindView(R.id.english_btn)
    Button englishBtn;
    @BindView(R.id.arabic_btn)
    Button arabicBtn;
    @BindView(R.id.subjectscontainer)
    LinearLayout subjectscontainer;
    private DatabaseReference mDatabase;
    SharedPreferences myPref;


    StudentModel studentModel;
    ActionBarDrawerToggle mDrawerToggle;


    DatabaseReference ref1;
    DatabaseReference ref2;
    ArrayList<NameIdModel> NameIdList = new ArrayList<>();
    boolean value = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        ButterKnife.bind(this);

        value=true;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!getIntent().hasExtra("none")){
            finish();
        }

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
//                        startActivity(new Intent(StudentMainActivity.this, StudentProfile.class));
                        subjectscontainer.setVisibility(View.VISIBLE);
                        buttonscontainer.setVisibility(View.GONE);

                        break;

                    case 1:
                        startActivity(new Intent(StudentMainActivity.this, StudentProfile.class));
//                        startActivity(new Intent(StudentMainActivity.this, TeacherOrStudentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;

                    case 2:
                        if(auth!=null)
                        auth.signOut();
                        finish();
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

      //  getModelFromDatabase();


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

    ValueEventListener l=null;

    private void getModelFromDatabase() {




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
        ref2.addValueEventListener(new ValueEventListener() {
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

    @Override
    protected void onStart() {
        super.onStart();
        if(l==null){
            l = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentModel = dataSnapshot.getValue(StudentModel.class);

                    if(!value){
                       finish();
                    }
                    if (studentModel != null) {
                        if (studentModel.getTeacherfeedback() != null && !studentModel.getTeacherfeedback().equals("") &&
                                studentModel.getTeacher() != null && !studentModel.getTeacher().equals("")) {
                            feedbackTxt.setText(String.format("%s : %s", studentModel.getTeacher(), studentModel.getTeacherfeedback()));
                        }
                    }

                    if (studentModel == null || studentModel.getTeacher() == null || studentModel.getTeacher().equals("")) {
                        if(!getIntent().hasExtra("none")){
                            StudentMainActivity.super.onBackPressed();
                        }
                        onCreateDialog();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
                }
            };
            ref1.child("Students").child(auth.getCurrentUser().getUid()).addValueEventListener(l);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(l!=null){
            l=null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
            if(l!=null){
                l=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
            if(l!=null){
                l=null;
            }
    }

    @OnClick({R.id.lesson_btn, R.id.activity_btn, R.id.quiz_btn, R.id.english_btn, R.id.arabic_btn})
    public void onViewClicked(View view) {
        SharedPreferences settings = getSharedPreferences("defauty", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        String value = settings.getString("Subject", "");
        switch (view.getId()) {
            case R.id.english_btn:


                editor.putString("Subject", "English");
                editor.commit();
                subjectscontainer.setVisibility(View.GONE);
                buttonscontainer.setVisibility(View.VISIBLE);


//                startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("databasePath", "Lessons"));
                break;
            case R.id.arabic_btn:

                editor.putString("Subject", "Arabic");
                editor.commit();

                subjectscontainer.setVisibility(View.GONE);
                buttonscontainer.setVisibility(View.VISIBLE);


//                startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("databasePath", "Lessons"));
                break;
            case R.id.lesson_btn:
                if (value != null && value.equals("English") || value.equals(""))
                    startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("Subject", "English").putExtra("databasePath", "Lessons"));
                else
                    startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("Subject", value).putExtra("databasePath", "Lessons"));
                break;
            case R.id.activity_btn:

                if (value != null && value.equals("English") || value.equals(""))
                    startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("Subject", "English").putExtra("databasePath", "Activities"));
                else
                    startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("Subject", value).putExtra("databasePath", "Activities"));

//                startActivity(new Intent(StudentMainActivity.this, LessonsAndActivitiesActivity.class).putExtra("databasePath", "Activities"));
                break;
            case R.id.quiz_btn:
                if (value != null && value.equals("English") || value.equals(""))
                    startActivity(new Intent(StudentMainActivity.this, QuizzesActivity.class).putExtra("Subject", "English").putExtra("databasePath", "Quizzes"));
                else
                    startActivity(new Intent(StudentMainActivity.this, QuizzesActivity.class).putExtra("Subject", value).putExtra("databasePath", "Quizzes"));


//                startActivity(new Intent(StudentMainActivity.this, QuizzesActivity.class).putExtra("databasePath", "Quizzes"));
                break;
        }
    }


}
