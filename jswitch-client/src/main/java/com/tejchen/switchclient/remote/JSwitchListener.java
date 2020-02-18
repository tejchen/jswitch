package com.tejchen.switchclient.remote;

public interface JSwitchListener {

    String listenApp();

    String listenKey();

    boolean onChange(String newConfigText);
}
