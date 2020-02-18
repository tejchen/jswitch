package com.tejchen.switchcommon.protocol.zookeeper;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ZkPushProtocol {

    private String appName;

    private List<ZkPushItem> pushConfig;

    public ZkPushProtocol(){
        if (pushConfig == null){
            pushConfig = new ArrayList<>();
        }
    }
}
