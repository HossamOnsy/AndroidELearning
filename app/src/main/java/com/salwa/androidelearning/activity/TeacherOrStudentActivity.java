package com.salwa.androidelearning.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.salwa.androidelearning.R;
import com.salwa.androidelearning.pref;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.salwa.androidelearning.pref.getsharedPref;

public class TeacherOrStudentActivity extends AppCompatActivity {

    @BindView(R.id.student_btn)
    Button studentBtn;
    @BindView(R.id.teacher_btn)
    Button teacherBtn;
    @BindView(R.id.language)
    ImageView language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_or_student);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.student_btn, R.id.teacher_btn,R.id.language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.student_btn:

                startActivity(new Intent(TeacherOrStudentActivity.this, LoginActivity.class).putExtra("type", "students"));
                break;
            case R.id.teacher_btn:
                startActivity(new Intent(TeacherOrStudentActivity.this, LoginActivity.class).putExtra("type", "teachers"));
                break;

            case  R.id.language:

                SharedPreferences myPref = getsharedPref(TeacherOrStudentActivity.this);
                String languageID = myPref.getString("languageID", "English");

                if(languageID.equals("English")){

                    Locale locale = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = getBaseContext().getResources().getConfiguration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("languageID", "Arabic");
                    editor.apply();
                    editor.commit();

                }else {

                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = getBaseContext().getResources().getConfiguration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());


                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("languageID", "English");
                    editor.apply();
                    editor.commit();

                }

                break;

        }
    }


}
