package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPushItem;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
public class JSwitchPushHandler implements JSwitchRemoteHandle<JSwitchConfig> {

    private JSwitchServerProxy          proxy;

    private String                      appName;

    private List<JSwitchConfig>         jswitchConfigs;

    public JSwitchPushHandler(String appName, JSwitchServerProxy proxy) {
        this.appName = appName;
        this.proxy = proxy;
        this.jswitchConfigs = new ArrayList<>();
    }

    public void addItem(JSwitchConfig jswitchConfig){
        jswitchConfigs.add(jswitchConfig);
    }

    public void handle() {
        // 推送
        if (!this.jswitchConfigs.isEmpty()) {
            List<JSwitchPushItem> items = jswitchConfigs.stream().map(jswitchConfig->{
                JSwitchPushItem item = new JSwitchPushItem();
                item.setType(jswitchConfig.getConfigOrigin().getTypeName());
                item.setConfigKey(jswitchConfig.getConfigKey());
                item.setDefaultValue(jswitchConfig.getConfigDefaultValue());
                return item;
            }).collect(Collectors.toList());
            boolean pushResult= proxy.push(appName, items);
            if (!pushResult){
                throw new JSwitchException("push local config err!");
            }
        }
    }
}

