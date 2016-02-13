package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.CheckWinningEvent;
import com.teamtbd.teamtbdapp.events.StartedContestEvent;
import com.teamtbd.teamtbdapp.events.TicketEvent;
import com.teamtbd.teamtbdapp.events.TitleEvent;
import com.teamtbd.teamtbdapp.events.TotalTicketEvent;
import com.teamtbd.teamtbdapp.events.UpdateTotalEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ClientActivity extends AppCompatActivity {
    private TextView title, tickets, chances, pot;
    private EventService eventService;
    private int price;
    private EventBus eventBus = Bus.getInstance();

    String eventID;

    boolean won;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        title = (TextView)findViewById(R.id.eventTittle);
        tickets = (TextView)findViewById(R.id.ticketsValue);
        chances = (TextView)findViewById(R.id.chanceValue);
        pot = (TextView)findViewById(R.id.potValue);

        tickets.setText("0");

        eventID = getIntent().getExtras().getString("ID");

        eventService = new EventService(this);
        eventService.getTicketPrice(eventID);
        eventService.getName(eventID);
        eventService.getOnesTickets(eventID, Profile.getCurrentProfile().getId());

        final AppCompatActivity activity = this;
        Button buttonBuy = (Button)findViewById(R.id.buyTickets);
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, BuyActivity.class);
                i.putExtra("ID", eventID);
                startActivity(i);
            }
        });

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                eventService.getContestStatus(eventID);
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 400);

    }

    @Subscribe
    public void checkWinning(CheckWinningEvent event){
        this.won = event.won;
        Intent i = new Intent(this, AnimationActivity.class);
        i.putExtra("won", won);
        startActivity(i);
    }

    @Subscribe
    public void getStartedEvent(StartedContestEvent event){
        if(event.status != ""){
            eventService.isWinning(eventID, Profile.getCurrentProfile().getId());
        }
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
    public  void updateTickets(TicketEvent event){
        tickets.setText(event.content);
        eventService.getTotalTickets(eventID);
    }


    @Subscribe
    public  void updateTotalTickets(TotalTicketEvent event){
        pot.setText((Integer.parseInt(event.content) * price) + "$");
        if(Integer.parseInt(tickets.getText().toString()) == 0)
            chances.setText("0%");
        else
            chances.setText((Integer.parseInt(tickets.getText().toString()) / Integer.parseInt(event.content) * 100) + "%");
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
