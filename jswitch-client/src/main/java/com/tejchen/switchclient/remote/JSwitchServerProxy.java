package com.tejchen.switchclient.remote;

import com.tejchen.switchcommon.protocol.http.form.JSwitchPushItem;

import java.util.List;
import java.util.Map;

public interface JSwitchServerProxy {

    /**
     * 连接服务器
     * 做初始化操作
     * @param server
     * @return
     */
    boolean connect(String appCode, String server);

    /**
     * 拉取配置
     * @param appCode
     * @param keys
     * @return
     */
    Map<String, String> pull(String appCode, List<String> keys);

    /**
     * 推送配置
     * @param appCode
     * @param JSwitchPushItems
     * @return
     */
    boolean push(String appCode, List<JSwitchPushItem> JSwitchPushItems);

    /**
     * 添加监听器
     * @param listener
     * @return
     */
    boolean addListener(JSwitchListener listener);

}
