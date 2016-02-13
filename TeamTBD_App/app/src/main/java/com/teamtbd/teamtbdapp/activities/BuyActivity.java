package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Profile;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.UpdateTotalEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BuyActivity extends AppCompatActivity {

    private EventService eventService = new EventService(this);
    private EditText ticketsNumber;
    private TextView ticketsTotal;
    private String eventId;
    private EventBus eventBus = Bus.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        ticketsNumber = (EditText)findViewById(R.id.ticketsNumber);
        ticketsTotal = (TextView)findViewById(R.id.ticketsTotal);

        final AppCompatActivity activity = this;

        eventId = getIntent().getExtras().getString("ID");

        eventService.getTicketPrice(eventId);

        Button buttonValidate = (Button)findViewById(R.id.validateBuy);
        buttonValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.getTickets(eventId, Profile.getCurrentProfile().getId(), Integer.parseInt(ticketsNumber.getText().toString()));
                Intent i = new Intent(activity, ClientActivity.class);
                i.putExtra("ID", eventId);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Subscribe
    public void updateTotal(UpdateTotalEvent event){
        ticketsTotal.setText((Integer.parseInt(ticketsNumber.getText().toString()) * Integer.parseInt(event.content)));
    }

}
