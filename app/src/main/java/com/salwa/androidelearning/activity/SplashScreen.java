package com.salwa.androidelearning.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.salwa.androidelearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.iv)
    LottieAnimationView iv;

    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

//        animator.addUpdateListener(animation -> {
//            iv.setAlpha((Float) animation.getAnimatedValue());
//        });
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e("Animation:","start");
                count++;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("Animation:","end");
                if(count == 4) {
                    startActivity(new Intent(SplashScreen.this, TeacherOrStudentActivity.class));
                    finish();
                }else {
                    animator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e("Animation:","cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e("Animation:","repeat");
            }
        });





    }
}
