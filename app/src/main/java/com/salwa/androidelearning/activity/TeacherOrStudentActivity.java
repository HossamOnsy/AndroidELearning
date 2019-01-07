package com.salwa.androidelearning.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.salwa.androidelearning.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TeacherOrStudentActivity extends AppCompatActivity {

    @BindView(R.id.student_btn)
    Button studentBtn;
    @BindView(R.id.teacher_btn)
    Button teacherBtn;

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

                startActivity(new Intent(TeacherOrStudentActivity.this,LoginActivity.class).putExtra("type","Students"));
                break;
            case R.id.teacher_btn:
                startActivity(new Intent(TeacherOrStudentActivity.this,LoginActivity.class).putExtra("type","Teachers"));
                break;
            case R.id.language:{
                SharedPreferences settings = getSharedPreferences("defauty", MODE_PRIVATE);
                String value = settings.getString("key", "");
               if   (value==null || value.equals("")||value.equals("en")){

                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("key", "ar");
                    editor.commit();

                    Locale locale = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    finishAffinity();
                    startActivity(new Intent(this,TeacherOrStudentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } else {

                   SharedPreferences.Editor editor = settings.edit();
                   editor.putString("key", "en");
                   editor.commit();

                   Locale locale = new Locale("en");
                   Locale.setDefault(locale);
                   Configuration config = new Configuration();
                   config.locale = locale;
                   getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                   finishAffinity();
                   startActivity(new Intent(this,TeacherOrStudentActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               }

            }
        }
    }
}
