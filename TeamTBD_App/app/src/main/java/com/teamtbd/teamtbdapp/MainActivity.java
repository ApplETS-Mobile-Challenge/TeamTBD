package com.teamtbd.teamtbdapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.TestEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private EventService eventService = new EventService(this);
    private EventBus eventBus = Bus.getInstance();

    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTextView = (TextView)findViewById(R.id.testTextView);

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
