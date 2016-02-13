package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.teamtbd.teamtbdapp.R;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        final AppCompatActivity activity = this;

        Button buttonBuy = (Button)findViewById(R.id.buyTickets);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, BuyActivity.class);
                startActivity(i);
            }
        });
    }
}
