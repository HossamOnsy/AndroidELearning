package com.salwa.androidelearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterationActivity extends AppCompatActivity {

    @BindView(R.id.ed_email)
    TextInputEditText edEmail;
    @BindView(R.id.ed_password)
    TextInputEditText edPassword;
    @BindView(R.id.ed_re_password)
    TextInputEditText edRePassword;
    @BindView(R.id.Age)
    TextInputEditText edAge ;
    @BindView(R.id.name)
    TextInputEditText edName ;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.role)
    RadioGroup radioGroup ;
    @BindView(R.id.gender)
    RadioGroup radioGroup1 ;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);
//        FirebaseUser currentUser = auth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @OnClick(R.id.register)
    public void onViewClicked() {
        final String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String repassword = edRePassword.getText().toString().trim();
        final String age = edAge.getText().toString();
        final String name = edName.getText().toString();

        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton)  radioGroup.findViewById(radioButtonID);
        final String role = (String) radioButton.getText();

        int radioButtonID1 = radioGroup1.getCheckedRadioButtonId();
        RadioButton radioButton1 = (RadioButton)  radioGroup1.findViewById(radioButtonID1);
        final String gender = (String) radioButton1.getText();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repassword)) {
            Toast.makeText(this, "Password Mismatch !!!", Toast.LENGTH_SHORT).show();
            return;
        }

      /*  if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterationActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mDatabase = FirebaseDatabase.getInstance().getReference("users");
                            String userId = auth.getCurrentUser().getUid();

                            User user = new User(name,email, age,gender,role);

                            mDatabase.child(userId).setValue(user);

                            startActivity(new Intent(RegisterationActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });


    }
}
