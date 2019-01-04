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

public class CreationActivity extends AppCompatActivity {

    @BindView(R.id.lesson_btn)
    Button lessonBtn;
    @BindView(R.id.activity_btn)
    Button activityBtn;
    @BindView(R.id.quiz_btn)
    Button quizBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.lesson_btn, R.id.activity_btn, R.id.quiz_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.lesson_btn:
                startActivity(new Intent(CreationActivity.this, LessonCreationActivity.class).putExtra("databasePath", "Lessons"));
                break;

            case R.id.activity_btn:
                startActivity(new Intent(CreationActivity.this, LessonCreationActivity.class).putExtra("databasePath", "Activities"));
                break;

            case R.id.quiz_btn:
                startActivity(new Intent(CreationActivity.this, QuizzesCreation.class).putExtra("databasePath", "Quizzes"));
                break;
        }
    }
}
