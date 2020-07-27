package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.CacheData;
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
public class JSwitchListenHandler implements JSwitchRemoteHandle<CacheData> {

    private JSwitchServerProxy          proxy;

    private List<CacheData>             cacheDataList;

    private String                      appName;

    public JSwitchListenHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.cacheDataList = new ArrayList<>();
    }

    public void append(CacheData cacheData){
        cacheDataList.add(cacheData);
    }

    public void handle() {
        if (!this.cacheDataList.isEmpty()) {
            for (CacheData cacheData : cacheDataList) {
                proxy.addListener(new DefaultJSwitchListener(cacheData));
            }
        }
    }
}

