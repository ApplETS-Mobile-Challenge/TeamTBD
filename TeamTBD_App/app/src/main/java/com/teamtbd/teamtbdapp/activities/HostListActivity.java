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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostListActivity extends AppCompatActivity {
    private List<String> hostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_list);

        hostList = Arrays.asList("Dîner bénifice pour le cancer du sein", "Bingo du samedi soir à Saint-Jean-de-Matha", "Cocoton de Laval");

        ListView listView = (ListView)findViewById(R.id.hostListView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, hostList));

        final HostListActivity currentActivity = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1) {
                    Intent i = new Intent(currentActivity, ClientActivity.class);
                    currentActivity.startActivity(i);
                }
            }
        });
    }
}
