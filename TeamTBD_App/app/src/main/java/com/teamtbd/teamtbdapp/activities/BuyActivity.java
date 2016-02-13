package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.teamtbd.teamtbdapp.R;

public class BuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        final NumberPicker nbrTickets = (NumberPicker)findViewById(R.id.ticketsNumber);
        nbrTickets.setMinValue(0);

        final AppCompatActivity activity = this;

        Button buttonValidate = (Button)findViewById(R.id.validateBuy);
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, ClientActivity.class);
                startActivity(i);
            }
        });
    }
}
