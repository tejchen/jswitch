package com.tejchen.switchclient.remote;

import com.tejchen.switchcommon.event.JSwitchClientEvent;
import com.tejchen.switchcommon.event.JSwitchEventWrapper;

public interface JSwitchEventCollector {

    boolean collect(JSwitchClientEvent event, JSwitchEventWrapper data);

    boolean flush();
}
