package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.Profile;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.EventCreationEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SettingActivity extends AppCompatActivity {
    private EventService eventService = new EventService(this);
    private EventBus eventBus = Bus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final AppCompatActivity activity = this;

        final EditText eventName = (EditText)findViewById(R.id.eventName);
        final Spinner draftType = (Spinner)findViewById(R.id.draftType);
        final EditText price = (EditText)findViewById(R.id.priceValue);

        Button buttonFinish = (Button)findViewById(R.id.finishSettings);
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventService.createEvent(eventName.getText().toString(), Profile.getCurrentProfile().getId(), Integer.parseInt(price.getText().toString()));
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
    public void onEventCreated(EventCreationEvent event) {
        Intent i = new Intent(this, HostActivity.class);
        i.putExtra("eventId", event.content);
        startActivity(i);
    }
}
