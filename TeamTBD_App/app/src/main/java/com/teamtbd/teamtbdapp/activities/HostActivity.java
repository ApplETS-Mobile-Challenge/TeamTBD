package com.teamtbd.teamtbdapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.teamtbd.teamtbdapp.R;

public class HostActivity extends AppCompatActivity {
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        eventId = getIntent().getStringExtra("eventId");
    }
}
