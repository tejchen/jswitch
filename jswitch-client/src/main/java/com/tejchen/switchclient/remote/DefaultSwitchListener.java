package com.tejchen.switchclient.remote;

import com.tejchen.switchclient.model.SwitchConfig;
import lombok.Getter;

public class DefaultSwitchListener implements SwitchListener{

    @Getter
    private SwitchConfig switchConfig;


    public DefaultSwitchListener (SwitchConfig switchConfig) {
        this.switchConfig = switchConfig;
    }

    @Override
    public String listenApp() {
        return switchConfig.getConfigApp();
    }

    @Override
    public String listenKey() {
        return switchConfig.getConfigKey();
    }

    @Override
    public void configUpdated(String newConfigText) {
        switchConfig.updateConfig(newConfigText);
    }
}
