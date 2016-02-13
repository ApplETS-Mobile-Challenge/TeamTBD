package com.teamtbd.teamtbdapp.services;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.EventCreationEvent;
import com.teamtbd.teamtbdapp.events.TestEvent;

import org.greenrobot.eventbus.EventBus;


public class EventService implements IEventService {
    private final static String API_URL = "http://teamspeak.sirkhepre.net:8081/";

    private Context context;
    private EventBus eventBus = Bus.getInstance();

    public EventService(Context context) {
        this.context = context;
    }

    @Override
    public void createEvent(String eventName, String hostID, int price) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/create/" + hostID + "/" + eventName + "/" + price, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("_TEAM_TBD_", "Event successfully created. " + response);
                eventBus.post(new EventCreationEvent(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void getTickets(String eventID, String userID, int qty) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/tickets/" + userID + "/" + qty +"/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new TestEvent(response));
                Log.i("_TEAM_TBD_", "getTickets posted.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }
}
