package com.tejchen.switchclient.remote.proxy;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.tejchen.switchclient.helper.HttpHelper;
import com.tejchen.switchclient.remote.JSwitchEventCollector;
import com.tejchen.switchclient.remote.JSwitchListener;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.event.JSwitchEvent;
import com.tejchen.switchcommon.event.JSwitchEventWrapper;
import com.tejchen.switchcommon.helper.EventHelper;
import com.tejchen.switchcommon.helper.HttpPathHelper;
import com.tejchen.switchcommon.helper.NodeHelper;
import com.tejchen.switchcommon.helper.SerializeHelper;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponseEnhancer;
import com.tejchen.switchcommon.protocol.http.form.*;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPullItem;
import com.tejchen.switchcommon.protocol.http.form.JSwitchHttpPullForm;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 默认通过http连接到server
 * 基于心跳版本号，更新本地缓存的配置
 */
public class DefaultJSwitchServerProxy implements JSwitchServerProxy {

    private final Logger                    logger = LoggerFactory.getLogger(DefaultJSwitchServerProxy.class);
    private HttpClient                      httpClient;
    private String                          server;
    private String                          token;
    private String                          baseVersion = "-1";
    private ScheduledExecutorService        executorService = Executors.newScheduledThreadPool(1);
    private boolean                         registered = false;
    private Map<String, JSwitchListener>    listenerMap = new HashMap<>();
    private JSwitchEventCollector           collector = new DefaultJSwitchEventCollector();


    public boolean connect(String appCode, String server) {
        this.server = server;
        this.httpClient = HttpClients.createDefault();
        // ping
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpPingPath);
        String pingResult = HttpHelper.get(httpClient, url, null);
        JSwitchHttpResponse pingResponse = SerializeHelper.deserializeJson(pingResult, JSwitchHttpResponse.class);
        if (!"pong".equals(pingResponse.getData())){
            logger.error("http connected err! {}", server);
            return false;
        }
        logger.info("http connected success!");
        // 生成机器指纹
        this.token = NodeHelper.generateToken();
        // 获取数据版本
        JSwitchHeartbeatForm heartbeatForm = new JSwitchHeartbeatForm();
        heartbeatForm.setAppCode(appCode);
        heartbeatForm.setToken(token);
        String versionUrl = HttpPathHelper.getUrl(server, HttpPathHelper.httpHeartbeatPath, appCode);
        String versionResult = HttpHelper.postBody(httpClient, versionUrl, heartbeatForm);
        JSwitchHttpResponseEnhancer<JSwitchHeartbeat> response = SerializeHelper.deserializeJson(versionResult, new TypeReference<JSwitchHttpResponseEnhancer<JSwitchHeartbeat>>(){});
        if (response == null || response.getData() == null || !response.isSuccess()){
            logger.error("init version err! {}, {}", server, response);
            return false;
        }
        baseVersion = response.getData().getAppVersion();
        return true;
    }

    @Override
    public Map<String, String> pull(String appCode, List<String> keys) {
        if (appCode == null) {
            throw new JSwitchException("pull appCode must not null");
        }
        if (keys == null || keys.isEmpty()) {
            throw new JSwitchException("pull keys must not empty");
        }
        Map<String, String> result = new HashMap<>();
        // 拉取
        JSwitchHttpPullForm form = new JSwitchHttpPullForm();
        form.setAppCode(appCode);
        form.setKeys(keys);
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpPullPath);
        String batchResult = HttpHelper.postBody(httpClient, url, form);
        JSwitchHttpResponse<JSwitchPull> response = SerializeHelper.deserializeJson(batchResult, new TypeReference<JSwitchHttpResponse<JSwitchPull>>(){});
        JSwitchPull batchPull = response.getData();
        for (JSwitchPullItem item : batchPull.getValues()) {
            result.put(item.getKey(), item.getValue());
        }
        if (!registered) {
            initHeartbeat(appCode, keys);
            registered = true;
        }
        return result;
    }

    @Override
    public boolean push(String appCode, List<JSwitchPushItem> items) {
        if (appCode == null) {
            throw new JSwitchException("push appCode must not null");
        }
        if (items == null || items.isEmpty()) {
            throw new JSwitchException("push items must not empty");
        }
        JSwitchPushForm form = new JSwitchPushForm();
        form.setAppCode(appCode);
        form.setPushConfig(items);
        String url = HttpPathHelper.getUrl(server, HttpPathHelper.httpPushPath);
        String batchResult = HttpHelper.postBody(httpClient, url, form);
        JSwitchHttpResponseEnhancer response = SerializeHelper.deserializeJson(batchResult, JSwitchHttpResponseEnhancer.class);
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

    private void initHeartbeat(String appCode, List<String> keys) {
        executorService.scheduleAtFixedRate(() -> {
            try {
                JSwitchHeartbeatForm heartbeatForm = new JSwitchHeartbeatForm();
                heartbeatForm.setAppCode(appCode);
                heartbeatForm.setToken(token);
                String versionUrl = HttpPathHelper.getUrl(server, HttpPathHelper.httpHeartbeatPath, appCode);
                String versionResult = HttpHelper.postBody(httpClient, versionUrl, heartbeatForm);
                JSwitchHttpResponseEnhancer<JSwitchHeartbeat> response = SerializeHelper.deserializeJson(versionResult, new TypeReference<JSwitchHttpResponseEnhancer<JSwitchHeartbeat>>(){});
                if (response == null || response.getData() == null || !response.isSuccess()){
                    logger.error("heartbeat err! {}", response);
                    return;
                }
                String version = response.getData().getAppVersion();
                if (!version.equals(baseVersion)) {
                    Map<String, String> newKeyValue = pull(appCode, keys);
                    // 开始更新配置
                    try {
                        for (String key : newKeyValue.keySet()) {
                            String value = newKeyValue.get(key);
                            JSwitchListener listener = listenerMap.get(key);
                            if (listener != null) {
                                boolean result = listener.onChange(value);
                                if (result) {
                                    logger.info("listener update config success, key:{}, value:{}", key, value);
                                }
                            }
                        }
                        baseVersion = version;
                    }catch (Exception e){
                        JSwitchEventWrapper event = EventHelper.unacceptedPush(appCode, version);
                        logger.error("update config err! report an event... {}", event, e);
                        collector.collect(JSwitchEvent.ACCEPT_ACK, event);
                    }finally {
                        JSwitchEventWrapper event = EventHelper.acceptedPush(appCode, version);
                        logger.info("update config success! report an event... {}", event);
                        collector.collect(JSwitchEvent.ACCEPT_ACK, event);
                    }
                }
            }catch (Exception e){
                logger.warn("heartbeat err!", e);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    // 默认事件收集器
    // todo 目前只有单事件场景，后续可以拓展成批量事件
    // todo 目前只有单线程，后续可以拓展成线程安全
    class DefaultJSwitchEventCollector implements JSwitchEventCollector {

        private Multimap<JSwitchEvent, JSwitchEventWrapper> multimap = ArrayListMultimap.create();

        @Override
        public boolean collect(JSwitchEvent event, JSwitchEventWrapper data) {
            multimap.put(event, data);
            return flush();
        }

        @Override
        public boolean flush(){
            return send();
        }

        private boolean send(){
            JSwitchEventForm data = this.generateData();
            if (data == null) {
                return true;
            }
            String eventUrl = HttpPathHelper.getUrl(server, HttpPathHelper.httpEventPath);
            String sendResult = HttpHelper.postBody(httpClient, eventUrl, data);
            JSwitchHttpResponseEnhancer response = SerializeHelper.deserializeJson(sendResult, JSwitchHttpResponseEnhancer.class);
            if (response.isSuccess()){
                multimap.clear();
                return true;
            }
            return false;
        }

        private JSwitchEventForm generateData() {
            if (multimap.isEmpty()){
                return null;
            }
            JSwitchEvent event = Lists.newArrayList(multimap.keySet()).get(0);
            JSwitchEventWrapper wrapper = Lists.newArrayList(multimap.get(event)).get(0);
            JSwitchEventForm data = new JSwitchEventForm();
            data.setToken(NodeHelper.generateToken());
            data.setEventCode(event.getCode());
            data.setEventSn(wrapper.getEventSn());
            data.setEventData(SerializeHelper.serializeJson(wrapper.getEventData()));
            return data;
        }
    }
}
