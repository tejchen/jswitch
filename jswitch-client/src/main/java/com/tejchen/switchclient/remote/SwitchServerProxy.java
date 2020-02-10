package com.tejchen.switchclient.remote;

import com.tejchen.switchclient.model.SwitchPushConfig;

import java.util.List;
import java.util.Map;

public interface SwitchServerProxy {

    /**
     * 连接服务器
     * @param server
     * @return
     */
    boolean connect(String server);

    /**
     * 拉取配置
     * @param appName
     * @param key
     * @return
     */
    String get(String appName, String key);

    /**
     * 批量拉取配置
     * @param appKeys
     * @return
     */
    Map<String, Map<String, String>> get(Map<String, List<String>> appKeys);

    /**
     * 推送配置
     * @param pushConfig
     * @return
     */
    boolean push(SwitchPushConfig pushConfig);

    /**
     * 添加监听器
     * @param listener
     * @return
     */
    boolean addListener(SwitchListener listener);

}
