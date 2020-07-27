package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
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
            String result = "";
            for (String server : servers) {
                result = this.connect(appName, server);
                connected = connected || "".equals(result);
                if (connected){
                    break;
                }
            }
            if (!connected){
                throw new JSwitchException(result);
            }
        }
    }

    private String connect(String appName, String server){
        try{
            proxy.connect(appName, server);
            return "";
        }catch (JSwitchException je){
            return je.getMessage();
        }catch (Exception e){
            return String.format("connect got an unknown exception: %s", e);
        }
    }
}

