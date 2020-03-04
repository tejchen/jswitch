package com.tejchen.switchclient.remote.handler;


import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPushItem;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
public class JSwitchPushHandler implements JSwitchRemoteHandle<JSwitchConfig> {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchPushHandler.class);

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
                item.setConfigCode(jswitchConfig.getConfigCode());
                item.setConfigName(jswitchConfig.getConfigName().equals("") ? jswitchConfig.getConfigCode() : jswitchConfig.getConfigName());
                item.setDefaultValue(jswitchConfig.getConfigDefaultValue());
                return item;
            }).collect(Collectors.toList());
            boolean pushResult= proxy.push(appName, items);
            if (!pushResult){
                throw new JSwitchException("push local config err!");
            }
            logger.info("push data: {}", items);
        }
    }
}

