package com.salwa.androidelearning.activity;

import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.QuizModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity {

    QuizModel quizModel;
    @BindView(R.id.guideline2)
    Guideline guideline2;
    @BindView(R.id.guideline5)
    Guideline guideline5;
    @BindView(R.id.question_image_view)
    ImageView questionImageView;
    @BindView(R.id.guideline3)
    Guideline guideline3;
    @BindView(R.id.guideline_a)
    Guideline guidelineA;
    @BindView(R.id.answer_one)
    RadioButton answerOne;
    @BindView(R.id.answer_two)
    RadioButton answerTwo;
    @BindView(R.id.answer_three)
    RadioButton answerThree;
    @BindView(R.id.answer_four)
    RadioButton answerFour;
    @BindView(R.id.radiogroupcontainer)
    RadioGroup radiogroupcontainer;
    @BindView(R.id.submit_btn)
    Button submitBtn;
    @BindView(R.id.image_one)
    ImageView imageOne;
    @BindView(R.id.image_two)
    ImageView imageTwo;
    @BindView(R.id.image_three)
    ImageView imageThree;
    @BindView(R.id.image_four)
    ImageView imageFour;

    int checked = 0;
    String type_Of_Actvitiy = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("type_Of_Actvitiy")) {
            type_Of_Actvitiy = getIntent().getStringExtra("type_Of_Actvitiy");
        }

        if (getIntent().hasExtra("quizModel")) {

            quizModel = getIntent().getParcelableExtra("quizModel");

            Glide.with(this).load(quizModel.getQuestion()).into(questionImageView);
            Glide.with(this).load(quizModel.getAnswer1()).into(imageOne);
            Glide.with(this).load(quizModel.getAnswer2()).into(imageTwo);
            Glide.with(this).load(quizModel.getAnswer3()).into(imageThree);
            Glide.with(this).load(quizModel.getAnswer4()).into(imageFour);

            radiogroupcontainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == answerOne.getId()) {
                        checked = 1;
                    } else if (checkedId == answerTwo.getId()) {
                        checked = 2;

                    } else if (checkedId == answerThree.getId()) {
                        checked = 3;

                    } else if (checkedId == answerFour.getId()) {
                        checked = 4;

                    }
                }
            });

        } else {
            Toast.makeText(this, "Loading Error Occured", Toast.LENGTH_SHORT).show();
            finish();
        }


    }


    @OnClick({R.id.submit_btn, R.id.image_one, R.id.image_two, R.id.image_three, R.id.image_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:

                DatabaseReference ref1;
                DatabaseReference ref2;


                ref1 = FirebaseDatabase.getInstance().getReference();
                ref2 = ref1.child("Progress");
                if (quizModel != null && quizModel.getCorrectAnswer() != null) {
                    ref2.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).push().setValue(type_Of_Actvitiy
                            + " , He Answered Answer Number : " + checked + ", And The Correct Answer is Answer Number "
                            + quizModel.getCorrectAnswer() + " ");
                    finish();
                } else {
                    Toast.makeText(this, "Please Choose an Answer", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.image_one:
                checked = 1;
                answerOne.setChecked(true);
                answerTwo.setChecked(false);
                answerThree.setChecked(false);
                answerFour.setChecked(false);
                break;
            case R.id.image_two:
                checked = 2;
                answerTwo.setChecked(true);
                answerOne.setChecked(false);
                answerThree.setChecked(false);
                answerFour.setChecked(false);
                break;
            case R.id.image_three:
                checked = 3;
                answerThree.setChecked(true);
                answerTwo.setChecked(false);
                answerOne.setChecked(false);
                answerFour.setChecked(false);
                break;
            case R.id.image_four:
                checked = 4;
                answerFour.setChecked(true);
                answerTwo.setChecked(false);
                answerThree.setChecked(false);
                answerOne.setChecked(false);
                break;
        }
    }


}
