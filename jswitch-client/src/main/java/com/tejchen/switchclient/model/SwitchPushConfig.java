package com.tejchen.switchclient.model;


import com.tejchen.switchcommon.SerializeHelper;
import com.tejchen.switchclient.remote.SwitchServerProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class SwitchPushConfig {

    private SwitchServerProxy proxy;

    private Map<String, List<PushItem>> pushConfigs;

    public SwitchPushConfig(SwitchServerProxy proxy) {
        this.proxy = proxy;
        this.pushConfigs = new HashMap<>();
    }

    public void addPushItem(SwitchConfig switchConfig){
        List<PushItem> items = this.pushConfigs.getOrDefault(switchConfig.getConfigApp(), new ArrayList<>());
        PushItem item = new PushItem();
        item.setConfigApp(switchConfig.getConfigApp());
        item.setConfigApp(switchConfig.getConfigKey());
        item.setDefaultValue(SerializeHelper.serialize(switchConfig.getConfigDefaultValue()));
        items.add(item);
        this.pushConfigs.put(switchConfig.getConfigApp(), items);
    }

    public boolean isEmpty(){
        return this.pushConfigs.isEmpty();
    }

    public byte[] getPushData(){
        SerializeHelper.
    }
}

