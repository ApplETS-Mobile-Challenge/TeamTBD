package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.teamtbd.teamtbdapp.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final AppCompatActivity activity = this;

        Button buttonFinish = (Button)findViewById(R.id.finishSettings);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, HostActivity.class);
                startActivity(i);
            }
        });
    }
}
