package com.salwa.androidelearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ed_email)
    TextInputEditText edEmail;
    @BindView(R.id.ed_password)
    TextInputEditText edPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.register_text)
    TextView registerText;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();
//        updateUI(currentUser);
    }

    @OnClick({R.id.login, R.id.register_text, R.id.forgot_password})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.login) {
            if (edEmail.getText().toString().equals("") || edPassword.getText().toString().equals("")) {
                Toast.makeText(this, "Invalid Email / Password", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                    progressBar.setVisibility(View.GONE);
//                                updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
//                                updateUI(null);
                                }

                                // ...
                            }
                        });


            }
        } else if (view.getId() == R.id.register_text) {

            startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
            progressBar.setVisibility(View.GONE);
        } else if (view.getId() == R.id.forgot_password) {

            startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
            progressBar.setVisibility(View.GONE);
        }
    }

}
