package com.tejchen.switchclient.remote.listener;

import com.tejchen.switchclient.model.CacheData;
import com.tejchen.switchclient.remote.JSwitchListener;
import com.tejchen.switchcommon.JSwitchException;
import lombok.Getter;

public class DefaultJSwitchListener implements JSwitchListener {

    @Getter
    private CacheData cacheData;


    public DefaultJSwitchListener(CacheData cacheData) {
        this.cacheData = cacheData;
    }

    @Override
    public String listenApp() {
        return cacheData.getConfigApp();
    }

    @Override
    public String listenKey() {
        return cacheData.getConfigCode();
    }

    @Override
    public boolean receiveConfig(String val) throws JSwitchException {
        if (!val.equals(cacheData.getConfigValue())) {
            cacheData.update(val);
            return true;
        }
        return false;
    }
}
