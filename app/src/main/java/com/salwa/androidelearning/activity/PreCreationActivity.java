package com.salwa.androidelearning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.salwa.androidelearning.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreCreationActivity extends AppCompatActivity {

    @BindView(R.id.english_btn)
    Button englishBtn;
    @BindView(R.id.arabic_btn)
    Button arabicBtn;
    @BindView(R.id.subjectscontainer)
    LinearLayout subjectscontainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_creation);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.english_btn, R.id.arabic_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.english_btn:

                startActivity(new Intent(PreCreationActivity.this, CreationActivity.class).putExtra("Subject","English"));
                break;
            case R.id.arabic_btn:

                startActivity(new Intent(PreCreationActivity.this, CreationActivity.class).putExtra("Subject","Arabic"));
                break;
        }
    }
}
