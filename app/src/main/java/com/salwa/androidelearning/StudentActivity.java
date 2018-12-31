package com.salwa.androidelearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;

import static com.salwa.androidelearning.pref.getsharedPref;

public class StudentActivity extends AppCompatActivity {
    @BindView(R.id.sname)
    TextView textView ;
    SharedPreferences myPref;
    User user ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
//        myPref = getsharedPref(StudentActivity.this);
//        String name = myPref.getString(pref.name,"name");
        user = getIntent().getParcelableExtra("model");
          textView.setText(user.name);

    }
}
