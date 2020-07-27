package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.CacheData;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
public class JSwitchPullHandler implements JSwitchRemoteHandle<CacheData> {

    private JSwitchServerProxy          proxy;

    private List<CacheData>             cacheDataList;

    private String                      appName;

    public JSwitchPullHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.cacheDataList = new ArrayList<>();
    }

    public void append(CacheData cacheData){
        cacheDataList.add(cacheData);
    }

    public void handle() {
        if (!this.cacheDataList.isEmpty()) {
            List<String> keys = cacheDataList.stream().map(CacheData::getConfigCode).collect(Collectors.toList());
            Map<String, String> result = proxy.pull(appName, keys);
            if (result == null){
                return;
            }
            for (CacheData cacheData : cacheDataList) {
                String configValue = result.get(cacheData.getConfigCode());
                if (configValue == null){
                    continue;
                }
                cacheData.update(configValue);
            }
        }
    }
}

