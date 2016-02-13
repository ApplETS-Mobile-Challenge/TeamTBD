package com.teamtbd.teamtbdapp.services;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AppIdentifier;
import com.google.android.gms.nearby.connection.AppMetadata;
import com.google.android.gms.nearby.connection.Connections;
import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.ColorChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class ConnectionsService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        Connections.ConnectionRequestListener,
        Connections.EndpointDiscoveryListener,
        Connections.MessageListener {

    private boolean isHost = false;

    private Context context;
    private GoogleApiClient googleApiClient;

    private String otherEndpointID;

    public ConnectionsService(Context context) {
        this.context = context;
    }

    /* Google API Client */
    public void initGoogleAPI() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Nearby.CONNECTIONS_API)
                .build();
    }

    public void connectToGoogleAPI() {
        googleApiClient.connect();
    }

    public void disconnectFromGoogleAPI() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("_TEAM_TBD_", "Connected to Google API Services.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("_TEAM_TBD_", "Connection to Google API Services suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("_TEAM_TBD_", "Connection to Google API Services failed!");
    }
    /* Google API Client */

    /* Host */
    public void startAdvertising() {
        isHost = true;

        long NO_TIMEOUT = 0L;

        List<AppIdentifier> appIdentifierList = new ArrayList<>();
        appIdentifierList.add(new AppIdentifier(context.getPackageName()));
        AppMetadata appMetadata = new AppMetadata(appIdentifierList);

        String name = null;
        Nearby.Connections.startAdvertising(googleApiClient, name, appMetadata, NO_TIMEOUT, this)
                .setResultCallback(new ResultCallback<Connections.StartAdvertisingResult>() {
                    @Override
                    public void onResult(Connections.StartAdvertisingResult result) {
                        if (result.getStatus().isSuccess())
                            Log.i("_TEAM_TBD_", "Advertising successfully.");
                        else {
                            Log.i("_TEAM_TBD_", "Could not startAdvertising.");
                            Log.i("_TEAM_TBD", "" + result.getStatus().toString());
                        }
                    }
                });
    }

    @Override
    public void onConnectionRequest(final String remoteEndpointId, String remoteDeviceId, String remoteEndpointName, byte[] payload) {
        if(isHost) {
            byte[] myPayload = null;
            // Automatically accept all requests
            final String remoteEndpoint = remoteEndpointName;
            Nearby.Connections.acceptConnectionRequest(googleApiClient, remoteEndpointId,
                    myPayload, this).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    if (status.isSuccess()) {
                        Toast.makeText(context, "Connected to " + remoteEndpoint, Toast.LENGTH_SHORT).show();
                        otherEndpointID = remoteEndpointId;
                    } else {
                        Toast.makeText(context, "Failed to connect to: " + remoteEndpoint, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Nearby.Connections.rejectConnectionRequest(googleApiClient, remoteEndpointId);
        }
    }
    /* Host */

    /* Client */
    public void startDiscovering() {
        String serviceId = context.getString(R.string.service_id);

        long DISCOVER_TIMEOUT = 1000L;

        Nearby.Connections.startDiscovery(googleApiClient, serviceId, DISCOVER_TIMEOUT, this)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i("_TEAM_TBD_", "Discovering successfully.");
                        } else {
                            Log.i("_TEAM_TBD_", "Could not startDiscovering.");
                            Log.i("_TEAM_TBD", "" + status.toString());
                        }
                    }
                });
    }

    public void connectTo(final String endpointId, final String endpointName) {
        // Send a connection request to a remote endpoint. By passing 'null' for
        // the name, the Nearby Connections API will construct a default name
        // based on device model such as 'LGE Nexus 5'.
        String myName = null;
        byte[] myPayload = null;
        Nearby.Connections.sendConnectionRequest(googleApiClient, myName, endpointId, myPayload, new Connections.ConnectionResponseCallback() {
            @Override
            public void onConnectionResponse(String remoteEndpointId, Status status, byte[] bytes) {
                if (status.isSuccess()) {
                    Toast.makeText(context, "Connected! Endpoint: " + endpointId, Toast.LENGTH_LONG).show();
                    otherEndpointID = endpointId;
                } else {
                    Log.e("_TEAM_TBD_", "Could not connect to endpoint.");
                }
            }
        }, this);
    }

    @Override
    public void onEndpointFound(final String endpointId, String deviceId, String serviceId, final String endpointName) {
        Log.i("_TEAM_TBD_", "Endpoint found! " + endpointName);
        connectTo(endpointId, endpointName);
    }

    @Override
    public void onEndpointLost(String s) {
        Log.e("_TEAM_TBD_", "ENDPOINT LOST ALL. IS. VAIN. I MEAN W T F");
    }

    @Override
    public void onMessageReceived(String endpointId, byte[] payload, boolean isReliable) {
        String message = new String(payload);
        Log.d("fuckoff", message);
        if(message.substring(0, message.indexOf('/')).equals("COLOR")) {
            String[] splittedColors = message.substring(message.indexOf('/')).split("/");
            Log.d("fuckoff", splittedColors.toString());
            Bus.getInstance().post(new ColorChangeEvent(Color.rgb(Integer.parseInt(splittedColors[0]), Integer.parseInt(splittedColors[1]), Integer.parseInt(splittedColors[2]))));
        }
    }

    @Override
    public void onDisconnected(String endpointID) {

    }
    /* Client */

    /* Shared */
    public void sendMessage(String messageText) {
        Nearby.Connections.sendReliableMessage(googleApiClient, otherEndpointID, messageText.getBytes());
    }

    public boolean isConnectedToEndPoint() {
        return otherEndpointID != null;
    }

    public boolean isHost() {
        return isHost;
    }
    /* Shared */
}
