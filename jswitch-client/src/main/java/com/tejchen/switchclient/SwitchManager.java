package com.tejchen.switchclient;

import com.tejchen.switchclient.helper.AnnotationHelper;
import com.tejchen.switchclient.helper.SwitchServerHelper;
import com.tejchen.switchclient.model.SwitchConfig;
import com.tejchen.switchclient.model.SwitchContext;
import com.tejchen.switchclient.model.SwitchPushConfig;
import com.tejchen.switchclient.remote.DefaultSwitchListener;
import com.tejchen.switchclient.remote.SwitchListener;
import com.tejchen.switchclient.remote.SwitchServerProxy;
import com.tejchen.switchcommon.ServerEnum;
import com.tejchen.switchcommon.SwitchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SwitchManager {

    private String                      serverUrl;
    private ServerEnum                  server;
    private static SwitchManager        switchManager;
    private static SwitchServerProxy    proxy;

    private SwitchManager(String serverUrl, ServerEnum server){
        this.serverUrl = serverUrl;
        this.server = server;
        if (serverUrl == null || server == null) {
            throw new SwitchException("serverUrl or server empty");
        }
    }

    public static SwitchManager newSwitchManager(SwitchContext entity){
        if (switchManager == null) {
            synchronized (SwitchManager.class) {
                if (switchManager == null) {
                    switchManager = new SwitchManager(entity.getServerUrl(), entity.getServer());
                }
            }
        }
        return switchManager;
    }

    public void init(Class... classes){
        if (classes.length == 0) {
            throw new SwitchException("empty config!");
        }
        // 连接服务器
        proxy   = SwitchServerHelper.getProxy(server);
        boolean connectResult = proxy.connect(serverUrl);
        if (!connectResult) {
            throw new SwitchException("connect err!");
        }
        // 解析注解
        List<SwitchConfig> configList = AnnotationHelper.parseAnnotation(classes);
        if (configList.isEmpty()) {
            throw new SwitchException("empty config!");
        }
        // 最新配置
        Map<String, List<String>> appKeys = configList
                .stream()
                .collect(Collectors.groupingBy(x->x.getConfigApp(), Collectors.mapping(x->x.getConfigKey(), Collectors.toList())));
        Map<String, Map<String, String>> appKeyConfigs = proxy.get(appKeys);
        // 待推送配置
        SwitchPushConfig pushConfig = new SwitchPushConfig(proxy);
        //
        for (SwitchConfig switchConfig : configList) {
            // 添加监听
            SwitchListener listener = new DefaultSwitchListener(switchConfig);
            proxy.addListener(listener);
            // 获取配置
            Map<String, String> keyConfigs = appKeyConfigs.getOrDefault(switchConfig.getConfigApp(), new HashMap<>());
            String configValue = keyConfigs.get(switchConfig.getConfigKey());
            // 空配置
            if (configValue == null) {
                pushConfig.addPushItem(switchConfig);
                continue;
            }
            // 更新配置
            switchConfig.updateConfig(configValue);
        }
        // 推送
        if (!pushConfig.isEmpty()) {
            boolean pushResult= proxy.push(pushConfig);
            if (!pushResult){
                throw new SwitchException("push local config err!");
            }
        }
    }



}
