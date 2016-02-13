package com.teamtbd.teamtbdapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private int price;
    private EventBus eventBus = Bus.getInstance();
    private TextView title, pot, tickets;
    private EventService eventService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        title = (TextView)findViewById(R.id.eventTittle);
        pot = (TextView)findViewById(R.id.potValue);
        tickets = (TextView)findViewById(R.id.ticketsValue);

        eventId = getIntent().getStringExtra("eventId");

        eventService = new EventService(this);
        eventService.getTicketPrice(eventId);
        eventService.getName(eventId);
        eventService.getTotalTickets(eventId);
    }
    @Subscribe
    public void updatePrice(UpdateTotalEvent event){
        price = Integer.parseInt(event.content);
    }

    @Subscribe
    public void updateTitle(TitleEvent event){
        title.setText(event.content);
    }
    @Subscribe
    public  void updateTotalTickets(TotalTicketEvent event){
        pot.setText((Integer.parseInt(event.content) * price) + "$");
        tickets.setText(event.content);
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
