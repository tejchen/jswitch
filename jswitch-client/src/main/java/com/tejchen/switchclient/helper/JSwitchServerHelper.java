package com.tejchen.switchclient.helper;

import com.tejchen.switchclient.remote.proxy.DefaultJSwitchServerProxy;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchclient.remote.proxy.ZookeeperServerProxy;
import com.tejchen.switchcommon.JSwitchServer;
import com.tejchen.switchcommon.JSwitchException;

public class JSwitchServerHelper {

    public static JSwitchServerProxy getProxy(JSwitchServer server) {
        switch (server){
            case Default:
                return new DefaultJSwitchServerProxy();
            case ZooKeeper:
                return new ZookeeperServerProxy();
        }
        throw new JSwitchException("unSupport Server");
    }
}
