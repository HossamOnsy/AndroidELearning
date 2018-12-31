package com.salwa.androidelearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.salwa.androidelearning.pref.getsharedPref;

public class MainActivity extends AppCompatActivity {
     @BindView(R.id.useerr)
    TextView textView ;
     FirebaseAuth auth;
    private DatabaseReference mDatabase;
    SharedPreferences myPref ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        myPref = getsharedPref(MainActivity.this);
         final String TAG = "erorrr";
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users= mDatabase.child("users");
        String userId = auth.getCurrentUser().getUid();
        users.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                if(user.role.equals( "Student")) {

                    String name = String.valueOf(user.name);
//                    SharedPreferences.Editor editor = myPref.edit();
//                    editor.putString(pref.name, name);
//                    editor.apply();
//                    editor.commit();
//                    String name1 = myPref.getString(pref.name,"Default");
                    textView.setText(name);
                   Intent intent= new Intent(MainActivity.this, StudentActivity.class).
                           putExtra("model",user);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
