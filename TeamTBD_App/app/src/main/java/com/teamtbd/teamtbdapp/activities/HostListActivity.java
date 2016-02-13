package com.teamtbd.teamtbdapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.teamtbd.teamtbdapp.R;
import com.teamtbd.teamtbdapp.events.Bus;
import com.teamtbd.teamtbdapp.events.LoadedEventList;
import com.teamtbd.teamtbdapp.events.TitleEvent;
import com.teamtbd.teamtbdapp.events.UpdateTotalEvent;
import com.teamtbd.teamtbdapp.services.EventService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostListActivity extends AppCompatActivity {
    private List<String> hostList;
    private List<String> idsList;
    EventService eventService;
    private EventBus eventBus = Bus.getInstance();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);
        eventService = new EventService(this);

        hostList = new ArrayList<String>();
        idsList = new ArrayList<String>();
        eventService.getEventList();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, hostList);

        ListView listView = (ListView)findViewById(R.id.hostListView);
        listView.setAdapter(adapter);

        final HostListActivity currentActivity = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(currentActivity, ClientActivity.class);
                    i.putExtra("ID", idsList.get(0));
                    currentActivity.startActivity(i);
            }
        });
    }

    @Subscribe
    public void getName(TitleEvent event){
        adapter.add(event.content);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void updateList(LoadedEventList event){
        for(String str : event.events){
            eventService.getName(str);
            idsList.add(str);
        }
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
