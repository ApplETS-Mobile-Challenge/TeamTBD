package com.teamtbd.teamtbdapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.services.FacebookSocialManager;

public class LoginActivity extends AppCompatActivity {
    FacebookSocialManager fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fb = new FacebookSocialManager(this);

        Button buttonLogin = (Button)findViewById(R.id.login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.login();
            }
        });
    }
}
