package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.services.ConnectionsService;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends AppCompatActivity {
    private EventService eventService = new EventService(this);
    private ConnectionsService connectionsService = new ConnectionsService(this);
    private EventBus eventBus = Bus.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionsService.initGoogleAPI();

        setContentView(R.layout.activity_home);
        final AppCompatActivity activity = this;

        Button buttonHost = (Button)findViewById(R.id.buttonHost);
        buttonHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SettingActivity.class);
                startActivity(i);
            }
        });

        Button buttonClient = (Button) findViewById(R.id.buttonClient);
        buttonClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, HostListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //eventBus.register(this);
        AppEventsLogger.activateApp(this);
        connectionsService.connectToGoogleAPI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //eventBus.unregister(this);
        AppEventsLogger.deactivateApp(this);
        connectionsService.disconnectFromGoogleAPI();
    }
}
