package com.teamtbd.teamtbdapp.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.TestEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private EventService eventService = new EventService(this);
    private EventBus eventBus = Bus.getInstance();

    private TextView testTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.MESSAGES_API)
                .build();

        if(!googleApiClient.isConnected() && !googleApiClient.isConnecting())
            googleApiClient.connect();

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
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("_TEAM_TBD_", "Connected to Google API Services!!!! :D");
        testNearby();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("_TEAM_TBD_", "Connection to Google API Services suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("_TEAM_TBD_", "Connection to Google API Services failed!");
    }

    private void testNearby() {
        Message message = new Message("test".getBytes());

        PublishOptions options = new PublishOptions.Builder().setCallback(new PublishCallback() {
            @Override
            public void onExpired() {
                super.onExpired();
            }
        }).build();

        final AppCompatActivity currentActivity = this;
        Nearby.Messages.publish(googleApiClient, message, options).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                try {
                    status.startResolutionForResult(currentActivity, 1001);
                } catch(Exception e) {}

                if(status.isSuccess())
                    Log.i("_TEAM_TBD_", "Message published.");
                else {
                    Log.i("_TEAM_TBD_", "Error.");
                    Log.i("_TEAM_TBD", "" + status.toString());
                }
            }
        });
    }
}
