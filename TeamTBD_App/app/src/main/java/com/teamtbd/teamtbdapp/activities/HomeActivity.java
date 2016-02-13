package com.teamtbd.teamtbdapp.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.TestEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends AppCompatActivity {
    private EventService eventService = new EventService(this);
    private EventBus eventBus = Bus.getInstance();

    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        testTextView = (TextView)findViewById(R.id.testTextView);

        final AppCompatActivity currentActivity = this;
        Button buttonHost = (Button)findViewById(R.id.buttonHost);
        buttonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(currentActivity, HostActivity.class);
                startActivity(i);
            }
        });

        Button buttonClient = (Button)findViewById(R.id.buttonClient);
        buttonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(currentActivity, ClientActivity.class);
                startActivity(i);
            }
        });

        eventService.createEvent("", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    @Subscribe
    public void onTestEvent(TestEvent event) {
        testTextView.setText(event.content);
        Log.i("_TEAM_TBD_", "TestEvent received.");
    }
}
