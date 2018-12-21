package com.salwa.androidelearning;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.ed_email)
    TextInputEditText edEmail;
    @BindView(R.id.forgotpassword)
    Button forgotpassword;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.forgotpassword)
    public void onViewClicked() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        progressBar.setMessage("verifying..");
        progressBar.setVisibility(View.VISIBLE);

        mAuth.sendPasswordResetEmail(Objects.requireNonNull(edEmail.getText()).toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Reset password instructions has sent to your email",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),
                                    "Email doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
