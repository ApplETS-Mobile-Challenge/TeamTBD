package com.teamtbd.teamtbdapp.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.StartedContestEvent;
import com.teamtbd.teamtbdapp.events.EventCreationEvent;
import com.teamtbd.teamtbdapp.events.LoadedEventList;
import com.teamtbd.teamtbdapp.events.TicketEvent;
import com.teamtbd.teamtbdapp.events.TitleEvent;
import com.teamtbd.teamtbdapp.events.TotalTicketEvent;
import com.teamtbd.teamtbdapp.events.UpdateTotalEvent;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


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

    public void getName(String eventID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/name", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new TitleEvent(response));
                Log.i("_TEAM_TBD_", "getName posted.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void getTicketPrice(String eventID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/price", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("_TEAM_TBD_", "getTicketPrice posted.");
                eventBus.post(new UpdateTotalEvent(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void getTotalTickets(String eventID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/tickets/count", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new TotalTicketEvent(response));
                Log.i("_TEAM_TBD_", "getTotalTickets posted.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void getOnesTickets(String eventID, String userID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/tickets/count/" + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new TicketEvent(response));
                Log.i("_TEAM_TBD_", "getOnesTickets posted.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void getEventList(){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> events = new ArrayList<>();
                try {
                    JSONArray root = new JSONArray(response);
                    for(int i = 0; i < root.length(); i++){
                        events.add(root.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventBus.post(new LoadedEventList(events));
                Log.i("_TEAM_TBD_", "got list of events..");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void getContestStatus(String eventID){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/contestStarted", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new StartedContestEvent(response));
                Log.i("_TEAM_TBD_", "got list of events..");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void setContestStatus(String eventID, String status){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/setconteststarted/" + status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("_TEAM_TBD_", "got list of events..");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void setWinner(String eventID){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/calculatewinner", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("_TEAM_TBD_", "got list of events..");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("_TEAM_TBD_", error.networkResponse.data.toString());
            }
        });

        queue.add(stringRequest);
    }

    public void isWinning(String eventID, String userID){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL + "events/" + eventID + "/iswinner/" + userID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                eventBus.post(new StartedContestEvent(response));
                Log.i("_TEAM_TBD_", "got list of events..");
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
