package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.JSwitchConfig;
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
public class JSwitchPullHandler implements JSwitchRemoteHandle<JSwitchConfig> {

    private JSwitchServerProxy          proxy;

    private List<JSwitchConfig>         jswitchConfigs;

    private String                      appName;

    public JSwitchPullHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.jswitchConfigs = new ArrayList<>();
    }

    public void addItem(JSwitchConfig jswitchConfig){
        jswitchConfigs.add(jswitchConfig);
    }

    public void handle() {
        if (!this.jswitchConfigs.isEmpty()) {
            List<String> keys = jswitchConfigs.stream().map(JSwitchConfig::getConfigCode).collect(Collectors.toList());
            Map<String, String> result = proxy.pull(appName, keys);
            if (result == null){
                return;
            }
            for (JSwitchConfig jswitchConfig : jswitchConfigs) {
                String configValue = result.get(jswitchConfig.getConfigCode());
                if (configValue == null){
                    continue;
                }
                jswitchConfig.updateConfig(configValue);
            }
        }
    }
}

