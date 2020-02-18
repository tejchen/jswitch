package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchclient.remote.listener.DefaultJSwitchListener;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class JSwitchListenHandler implements JSwitchRemoteHandle<JSwitchConfig> {

    private JSwitchServerProxy          proxy;

    private List<JSwitchConfig>         jswitchConfigs;

    private String                      appName;

    public JSwitchListenHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.jswitchConfigs = new ArrayList<>();
    }

    public void addItem(JSwitchConfig jswitchConfig){
        jswitchConfigs.add(jswitchConfig);
    }

    public void handle() {
        if (!this.jswitchConfigs.isEmpty()) {
            for (JSwitchConfig jswitchConfig : jswitchConfigs) {
                proxy.addListener(new DefaultJSwitchListener(jswitchConfig));
            }
        }
    }
}

