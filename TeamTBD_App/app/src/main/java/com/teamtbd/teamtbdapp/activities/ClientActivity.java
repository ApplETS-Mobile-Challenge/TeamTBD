package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        title = (TextView)findViewById(R.id.eventTittle);
        tickets = (TextView)findViewById(R.id.ticketsValue);
        chances = (TextView)findViewById(R.id.chanceValue);
        pot = (TextView)findViewById(R.id.potValue);

        tickets.setText("0");

        eventService = new EventService(this);
        eventService.getTicketPrice("");
        eventService.getName("");
        eventService.getOnesTickets("","");
        eventService.getTotalTickets("");



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
        eventService.getTotalTickets("");
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
