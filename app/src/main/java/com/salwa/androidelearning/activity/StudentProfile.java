package com.salwa.androidelearning.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.R;
import com.salwa.androidelearning.models.StudentModel;
import com.salwa.androidelearning.models.TeacherModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentProfile extends AppCompatActivity {

    @BindView(R.id.student_name_text_view)
    TextView studentNameTextView;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.email)
    TextView email;
    String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        ButterKnife.bind(this);

        if(getIntent().hasExtra("id")){
            id = getIntent().getStringExtra("id");
        }
        getStudentData(id);
    }

    private DatabaseReference mDatabase;

    private void getStudentData(String id) {


        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = mDatabase.child("Students");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(!id.equals(""))
            userId = id;


        users.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                StudentModel user = dataSnapshot.getValue(StudentModel.class);

                SharedPreferences settings = getSharedPreferences("defauty", MODE_PRIVATE);
                String value = settings.getString("key", "");
                if(value.equals("en")) {
                    contact.setText(String.format("Contact : %s", user != null ? user.getContactNumber() : null));
                    age.setText(String.format("Age : %s", user != null ? user.getAge() : null));
                    email.setText(String.format("Email : %s", user != null ? user.getEmail() : null));
                    studentNameTextView.setText(String.format("Name : %s", user != null ? user.getName() : null));
                }else{
                    contact.setText(String.format("%s : طريقة التواصل", user != null ? user.getContactNumber() : null));
                    age.setText(String.format("%s : العمر", user != null ? user.getAge() : null));
                    email.setText(String.format("%s : البريد الالكتروني", user != null ? user.getEmail() : null));
                    studentNameTextView.setText(String.format("%s : الاسم", user != null ? user.getName() : null));
                }



            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}
