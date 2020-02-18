package com.tejchen.switchclient.remote.listener;

import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.remote.JSwitchListener;
import lombok.Getter;

public class DefaultJSwitchListener implements JSwitchListener {

    @Getter
    private JSwitchConfig JSwitchConfig;


    public DefaultJSwitchListener(JSwitchConfig JSwitchConfig) {
        this.JSwitchConfig = JSwitchConfig;
    }

    @Override
    public String listenApp() {
        return JSwitchConfig.getConfigApp();
    }

    @Override
    public String listenKey() {
        return JSwitchConfig.getConfigKey();
    }

    @Override
    public boolean onChange(String newConfigText) {
        if (!newConfigText.equals(JSwitchConfig.getConfigValue())) {
            JSwitchConfig.updateConfig(newConfigText);
            return true;
        }
        return false;
    }
}
