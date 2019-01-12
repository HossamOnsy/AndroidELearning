package com.salwa.androidelearning.activity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.StudentModel;
import com.salwa.androidelearning.models.TeacherModel;
import com.salwa.androidelearning.models.User;

import java.util.Objects;

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
    private String type = "students";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);

        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
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
            // For Testing Sake
            progressBar.setVisibility(View.VISIBLE);

            view.setClickable(false);
//            auth.signInWithEmailAndPassword("1@1.com", "111111")
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                if (type.equals("student"))
//                                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
//                                else {
//                                    startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class));
////                                    startActivity(new Intent(LoginActivity.this, TeacherTempMain.class));
//
//                                }
//                                finish();
//                                progressBar.setVisibility(View.GONE);
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Toast.makeText(LoginActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                progressBar.setVisibility(View.GONE);
//                            }
//
//                            view.setClickable(true);
//                            // ...
//                        }
//                    });


            if (edEmail.getText().toString().equals("") || edPassword.getText().toString().equals("")) {
                Toast.makeText(this, getString(R.string.invalid_em), Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    checkRole();
                                    // Sign in success, update UI with the signed-in user's information

                                } else {
//                                    task.getResult().
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                                view.setClickable(true);
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

    private void checkRole() {


        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();

        if (type.toLowerCase().equals("teachers"))
            ref1.child("Teachers").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    TeacherModel user = dataSnapshot.getValue(TeacherModel.class);

                    if (user != null && !user.getRole().toLowerCase().equals("student")) {
                        {
                            startActivity(new Intent(LoginActivity.this, TeacherMainActivity.class).putExtra("name",user.getName()));
                            //                                    startActivity(new Intent(LoginActivity.this, TeacherTempMain.class));

                            finishAffinity();
                            progressBar.setVisibility(View.GONE);


                        }

                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    // Failed to read value
                    //                Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        else
            ref1.child("Students").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    StudentModel user = dataSnapshot.getValue(StudentModel.class);

                    if (user != null && user.getRole().toLowerCase().equals("student")) {

                        startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
//                                    startActivity(new Intent(LoginActivity.this, TeacherTempMain.class));

                        finishAffinity();


                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


    }

}
