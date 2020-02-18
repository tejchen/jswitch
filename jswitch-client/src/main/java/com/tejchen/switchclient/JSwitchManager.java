package com.tejchen.switchclient;

import com.tejchen.switchclient.helper.AnnotationHelper;
import com.tejchen.switchclient.helper.JSwitchServerHelper;
import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchclient.remote.JSwitchRemoteHandle;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchclient.remote.handler.JSwitchConnectHandler;
import com.tejchen.switchclient.remote.handler.JSwitchListenHandler;
import com.tejchen.switchclient.remote.handler.JSwitchPullHandler;
import com.tejchen.switchclient.remote.handler.JSwitchPushHandler;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.JSwitchServer;

import java.util.List;

public class JSwitchManager {

    private String                      serverUrl;
    private JSwitchServer               server;
    private static JSwitchManager       jswitchManager;
    private static JSwitchServerProxy   proxy;

    private JSwitchManager(String serverUrl, JSwitchServer server){
        this.serverUrl = serverUrl;
        this.server = server;
        if (serverUrl == null || server == null) {
            throw new JSwitchException("serverUrl or server empty");
        }
    }

    public static JSwitchManager newSwitchManager(JSwitchContext entity){
        if (jswitchManager == null) {
            synchronized (JSwitchManager.class) {
                if (jswitchManager == null) {
                    jswitchManager = new JSwitchManager(entity.getServerUrl(), entity.getServer());
                }
            }
        }
        return jswitchManager;
    }

    public void init(String appName, Class... classes){
        if (appName == null || appName.isEmpty()) {
            throw new JSwitchException("empty appName!");
        }
        if (classes == null || classes.length == 0) {
            throw new JSwitchException("empty config!");
        }

        // 解析注解
        List<JSwitchConfig> configList = AnnotationHelper.parseAnnotation(appName, classes);
        if (configList.isEmpty()) {
            throw new JSwitchException("empty config!");
        }

        // 远程代理
        proxy = JSwitchServerHelper.getProxy(server);

        // 连接
        JSwitchRemoteHandle connectHandle = new JSwitchConnectHandler(appName, proxy);
        connectHandle.addItem(serverUrl);
        connectHandle.handle();

        // 拉取配置
        JSwitchRemoteHandle pullHandler = new JSwitchPullHandler(appName, proxy);
        for (JSwitchConfig jswitchConfig : configList) {
            pullHandler.addItem(jswitchConfig);
        }
        pullHandler.handle();

        // 添加监听
        JSwitchRemoteHandle listenHandler = new JSwitchListenHandler(appName, proxy);
        for (JSwitchConfig jswitchConfig : configList) {
            listenHandler.addItem(jswitchConfig);
        }
        listenHandler.handle();

        // 推送配置
        JSwitchRemoteHandle pushHandler = new JSwitchPushHandler(appName, proxy);
        for (JSwitchConfig jswitchConfig : configList) {
            pushHandler.addItem(jswitchConfig);
        }
        pushHandler.handle();
    }

}
