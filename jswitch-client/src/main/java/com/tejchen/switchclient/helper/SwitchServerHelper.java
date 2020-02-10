package com.tejchen.switchclient.helper;

import com.tejchen.switchclient.remote.DefaultSwitchServerProxy;
import com.tejchen.switchclient.remote.SwitchServerProxy;
import com.tejchen.switchclient.remote.ZookeeperSwitchServerProxy;
import com.tejchen.switchcommon.ServerEnum;
import com.tejchen.switchcommon.SwitchException;

public class SwitchServerHelper {

    public static SwitchServerProxy getProxy(ServerEnum server) {
        switch (server){
            case Default:
                return new DefaultSwitchServerProxy();
            case ZooKeeper:
                return new ZookeeperSwitchServerProxy();
        }
        throw new SwitchException("unSupport ServerEnum");
    }
}
