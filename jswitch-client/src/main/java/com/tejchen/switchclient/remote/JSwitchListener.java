package com.tejchen.switchclient.remote;

import com.tejchen.switchcommon.JSwitchException;

public interface JSwitchListener {

    String listenApp();

    String listenKey();

    boolean receiveConfig(String val) throws JSwitchException;
}
