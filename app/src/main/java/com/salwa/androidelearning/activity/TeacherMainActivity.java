package com.salwa.androidelearning.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.TeacherActivity;
import com.salwa.androidelearning.models.TeacherModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherMainActivity extends AppCompatActivity {

    //    @BindView(R.id.content_frame)
//    FrameLayout contentFrame;
//    @BindView(R.id.nav_view)
//    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    FirebaseAuth auth;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.email)
    TextView email;

    private DatabaseReference mDatabase;
    ActionBarDrawerToggle mDrawerToggle;
    @BindView(R.id.messageTextView)
    TextView messageTextView;
    @BindView(R.id.left_drawer)
    ListView leftDrawer;

    boolean roleAuthorized = false;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);


        ButterKnife.bind(this);
        if(getIntent().hasExtra("name")){
//            "name",user.getName();
            name = getIntent().getStringExtra("name");
        }

        checkRole();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
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
                        if (roleAuthorized)
                            startActivity(new Intent(TeacherMainActivity.this, PreCreationActivity.class));
                        else
                            Toast.makeText(TeacherMainActivity.this, "You Can't Create Module ... Appologies !!! ", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:

                        startActivity(new Intent(TeacherMainActivity.this, TeacherActivity.class).putExtra("name",name));

                        break;


                    case 2:
                        finishAffinity();
                        startActivity(new Intent(TeacherMainActivity.this, TeacherOrStudentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
//                messageTextView.setText("Menu Item at position " + position + " clicked.");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_iconfinder_menu);
    }

    private void checkRole() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = mDatabase.child("Teachers");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        users.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TeacherModel user = dataSnapshot.getValue(TeacherModel.class);

                SharedPreferences settings = getSharedPreferences("defauty", MODE_PRIVATE);
                String value = settings.getString("key", "");
                if(value.equals("en")) {
                    contact.setText(String.format("Contact : %s", user != null ? user.getContactNumber() : null));
                    age.setText(String.format("Age : %s", user != null ? user.getAge() : null));
                    email.setText(String.format("Email : %s", user != null ? user.getEmail() : null));
                    messageTextView.setText(String.format("Name : %s", user != null ? user.getName() : null));
                }else{
                    contact.setText(String.format("%s : طريقة التواصل", user != null ? user.getContactNumber() : null));
                    age.setText(String.format("%s : العمر", user != null ? user.getAge() : null));
                    email.setText(String.format("%s : البريد الالكتروني", user != null ? user.getEmail() : null));
                    messageTextView.setText(String.format("%s : الاسم", user != null ? user.getName() : null));
                }


                if (user != null) {
                    if (user.getRole().toLowerCase().equals("teacher")) {
                        roleAuthorized = false;
                    } else {
                        roleAuthorized = true;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    boolean opened = false;

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                if (!opened) {
//                    opened = true;
//                    drawerLayout.openDrawer(GravityCompat.START);
//                } else {
//                    opened = false;
//                    drawerLayout.closeDrawer(GravityCompat.START);
//
//                }
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
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
}
