package com.teamtbd.teamtbdapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.TitleEvent;
import com.teamtbd.teamtbdapp.events.TotalTicketEvent;
import com.teamtbd.teamtbdapp.events.UpdateTotalEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HostActivity extends AppCompatActivity {
    private String eventId;
    private int price = 0;
    private EventBus eventBus = Bus.getInstance();
    private TextView title, pot;
    private EventService eventService;
    private TextView ticketsValue;

    private Button stopSelling;
    private Button startDraft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        title = (TextView)findViewById(R.id.eventTittle);
        pot = (TextView)findViewById(R.id.potValue);
        ticketsValue = (TextView)findViewById(R.id.ticketsValue);


        eventId = getIntent().getStringExtra("eventId");

        eventService = new EventService(this);
        eventService.getTicketPrice(eventId);
        eventService.getName(eventId);
        eventService.getTotalTickets(eventId);

        startDraft = (Button)findViewById(R.id.startDraft);
        startDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.setContestStatus(eventId, "" + 1);
                eventService.setWinner(eventId);
            }
        });


        ticketsValue.setText("" + price);
    }
    @Subscribe
    public void updatePrice(UpdateTotalEvent event){
        price = Integer.parseInt(event.content);
        ticketsValue.setText(price + "$");
    }

    @Subscribe
    public void updateTitle(TitleEvent event){
        title.setText(event.content);
    }
    @Subscribe
    public  void updateTotalTickets(TotalTicketEvent event){
        pot.setText((Integer.parseInt(event.content) * price) + "$");
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
}
