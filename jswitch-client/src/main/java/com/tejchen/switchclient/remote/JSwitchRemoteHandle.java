package com.tejchen.switchclient.remote;

public interface JSwitchRemoteHandle<T> {

    void append(T item);

    void handle();
}
