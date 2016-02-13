package com.teamtbd.teamtbdapp.events;

import org.greenrobot.eventbus.EventBus;

public class Bus {
    private static EventBus ourInstance = new EventBus();

    public static EventBus getInstance() {
        return ourInstance;
    }

    private Bus() {
    }
}
