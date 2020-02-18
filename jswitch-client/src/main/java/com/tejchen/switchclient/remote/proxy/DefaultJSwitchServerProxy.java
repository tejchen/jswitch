package com.tejchen.switchclient.remote.proxy;

import com.alibaba.fastjson.TypeReference;
import com.tejchen.switchclient.helper.HttpHelper;
import com.tejchen.switchclient.remote.JSwitchListener;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.helper.HttpPathHelper;
import com.tejchen.switchcommon.helper.SerializeHelper;
import com.tejchen.switchcommon.protocol.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 默认连接到server
 *
 */
public class DefaultJSwitchServerProxy implements JSwitchServerProxy {

    private final Logger                    logger = LoggerFactory.getLogger(DefaultJSwitchServerProxy.class);
    private HttpClient                      httpClient;
    private String                          server;
    private String                          baseVersion = "-1";
    private ScheduledExecutorService        executorService = Executors.newScheduledThreadPool(1);
    private boolean                         registered = false;
    private Map<String, JSwitchListener>    listenerMap = new HashMap<>();


    public boolean connect(String appName, String server) {
        this.server = server;
        this.httpClient = HttpClients.createDefault();
        // ping
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpPingPath);
        String pingResult = HttpHelper.get(httpClient, url, null);
        if (!pingResult.equals("pong")){
            logger.error("http connected err! {}", server);
            return false;
        }
        logger.info("http connected success!");
        // check version
        String versionUrl = HttpPathHelper.getUrl(server, HttpPathHelper.httpVersionPath, appName);
        String versionResult = HttpHelper.get(httpClient, versionUrl, null);
        JSwitchHttpResponse<String> response = SerializeHelper.deserializeJson(versionResult, new TypeReference<JSwitchHttpResponse<String>>(){});
        String version = response.getData();
        if (version != null) {
            baseVersion = version;
        }
        return true;
    }

    @Override
    public Map<String, String> pull(String appName, List<String> keys) {
        if (appName == null) {
            throw new JSwitchException("pull appName must not null");
        }
        if (keys == null || keys.isEmpty()) {
            throw new JSwitchException("pull keys must not empty");
        }
        Map<String, String> result = new HashMap<>();
        // 拉取
        JSwitchHttpBatchForm form = new JSwitchHttpBatchForm();
        form.setAppName(appName);
        form.setKeys(keys);
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpBatchPath);
        String batchResult = HttpHelper.PostBody(httpClient, url, form);
        JSwitchHttpResponse<JSwitchBatchPull> response = SerializeHelper.deserializeJson(batchResult, new TypeReference<JSwitchHttpResponse<JSwitchBatchPull>>(){});
        JSwitchBatchPull batchPull = response.getData();
        for (JSwitchBatchPullItem jSwitchBatchPullItem : batchPull.getValues()) {
            result.put(jSwitchBatchPullItem.getKey(), jSwitchBatchPullItem.getValue());
        }
        if (!registered) {
            initHeartbeat(appName, keys);
            registered = true;
        }
        return result;
    }

    @Override
    public boolean push(String appName, List<JSwitchPushItem> items) {
        if (appName == null) {
            throw new JSwitchException("push appName must not null");
        }
        if (items == null || items.isEmpty()) {
            throw new JSwitchException("push items must not empty");
        }
        JSwitchPushForm form = new JSwitchPushForm();
        form.setAppName(appName);
        form.setPushConfig(items);
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpPushPath);
        String batchResult = HttpHelper.PostBody(httpClient, url, form);
        JSwitchHttpResponse response = SerializeHelper.deserializeJson(batchResult, JSwitchHttpResponse.class);
        if (response == null){
            logger.error("http connect err!");
            return false;
        }
        return response.isSuccess();
    }

    public boolean addListener(JSwitchListener listener) {
        this.listenerMap.put(listener.listenKey(), listener);
        return true;
    }

    private void initHeartbeat(String appName, List<String> keys) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpVersionPath, appName);
                String versionResult = HttpHelper.get(httpClient, url, null);
                JSwitchHttpResponse<String> response = SerializeHelper.deserializeJson(versionResult, new TypeReference<JSwitchHttpResponse<String>>(){});
                String version = response.getData();
                if (!version.equals(baseVersion)) {
                    Map<String, String> newKeyValue = pull(appName, keys);
                    for (String key : newKeyValue.keySet()) {
                        String value = newKeyValue.get(key);
                        JSwitchListener listener = listenerMap.get(key);
                        if (listener != null) {
                            boolean result = listener.onChange(value);
                            if (result) {
                                logger.info("listener update config, key:{}, value:{}", key, value);
                            }
                        }
                        baseVersion = version;
                    }
                }
                logger.debug("heartbeat success!");
            }catch (Exception e){
                logger.warn("heartbeat err!", e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

}
