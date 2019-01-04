package com.salwa.androidelearning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.salwa.androidelearning.R;

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

    @OnClick({R.id.student_btn, R.id.teacher_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.student_btn:

                startActivity(new Intent(TeacherOrStudentActivity.this,LoginActivity.class).putExtra("type","student"));
                break;
            case R.id.teacher_btn:
                startActivity(new Intent(TeacherOrStudentActivity.this,LoginActivity.class).putExtra("type","teacher"));
                break;
        }
    }
}
