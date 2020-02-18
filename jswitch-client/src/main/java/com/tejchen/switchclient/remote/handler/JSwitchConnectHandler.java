package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchclient.remote.listener.DefaultJSwitchListener;
import com.tejchen.switchcommon.JSwitchException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class JSwitchConnectHandler implements JSwitchRemoteHandle<String> {

    private JSwitchServerProxy          proxy;

    private String                      appName;

    private List<String>                servers;

    public JSwitchConnectHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.servers = new ArrayList<>();
    }

    public void addItem(String server){
        servers.add(server);
    }

    public void handle() {
        if (!this.servers.isEmpty()) {
            boolean connected = false;
            for (String server : servers) {
                connected = connected || proxy.connect(appName, server);
                // 只要其中一台连接上即可
                if (connected){
                    break;
                }
            }
            if (!connected){
                throw new JSwitchException("connect err!");
            }
        }
    }
}

