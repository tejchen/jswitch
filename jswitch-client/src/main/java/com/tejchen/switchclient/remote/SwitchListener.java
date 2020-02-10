package com.tejchen.switchclient.remote;

public interface SwitchListener {

    String listenApp();

    String listenKey();

    void configUpdated(String newConfigText);
}
