package com.tejchen.switchclient.remote;

import com.tejchen.switchcommon.event.JSwitchEvent;
import com.tejchen.switchcommon.event.JSwitchEventWrapper;

public interface JSwitchEventCollector {

    boolean collect(JSwitchEvent event, JSwitchEventWrapper data);

    boolean flush();
}
