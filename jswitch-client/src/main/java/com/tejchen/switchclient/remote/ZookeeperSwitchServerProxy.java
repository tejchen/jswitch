package com.tejchen.switchclient.remote;

import com.tejchen.switchclient.model.SwitchPushConfig;
import com.tejchen.switchcommon.SerializeHelper;
import com.tejchen.switchcommon.SwitchConfigProtocol;
import com.tejchen.switchcommon.SwitchException;
import com.tejchen.switchcommon.ZookeeperPathHelper;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZookeeperSwitchServerProxy implements SwitchServerProxy {

    private CuratorFramework            client;
    private Map<String, SwitchListener> listenerMap = new HashMap<>();

    @Override
    public boolean connect(String server) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(server, retryPolicy);
        client.start();
        return true;
    }

    @Override
    public String get(String appName, String key) {
        if (client == null) {
            throw new SwitchException("zookeeper client not connect!");
        }
        String path = getPath(appName, key);
        try {
            byte[] data = client.getData().forPath(path);
            SwitchConfigProtocol protocol = SerializeHelper.deserializeProtocol(SwitchConfigProtocol.class, new String(data, StandardCharsets.UTF_8));
            return protocol.getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Map<String, String>> get(Map<String, List<String>> appKeys) {
        if (client == null) {
            throw new SwitchException("zookeeper client not connect!");
        }
        if (appKeys == null) {
            throw new SwitchException("aooKeys must not null!");
        }
        Map<String, Map<String, String>> result = new HashMap<>();
        appKeys.forEach((appName, keys) -> {
            Map<String, String> keyValue = new HashMap<>();
            keys.forEach(key -> {
                String path = getPath(appName,key);
                // 重试两次
                try {
                    Stat stat = client.checkExists().forPath(path);
                    if (stat != null) {
                        byte[] data = client.getData().forPath(path);
                        SwitchConfigProtocol protocol = SerializeHelper.deserializeProtocol(SwitchConfigProtocol.class, new String(data, StandardCharsets.UTF_8));
                        keyValue.put(key, protocol.getValue());
                    }
                } catch (Exception e) {
                    try {
                        Stat stat = client.checkExists().forPath(path);
                        if (stat != null) {
                            byte[] data = client.getData().forPath(path);
                            SwitchConfigProtocol protocol = SerializeHelper.deserializeProtocol(SwitchConfigProtocol.class, new String(data, StandardCharsets.UTF_8));
                            keyValue.put(key, protocol.getValue());
                        }
                    } catch (Exception ex) {
                        throw new SwitchException(String.format("get config err! err: %s", ex.getMessage()));
                    }
                }
            });
            result.put(appName, keyValue);
        });
        return result;
    }

    @Override
    public boolean push(SwitchPushConfig pushConfig) {

        return true;
    }

    @Override
    public boolean addListener(SwitchListener listener) {
        String path = getPath(listener.listenApp(), listener.listenKey());
        if (listenerMap.containsKey(path)){
            return false;
        }
        listenerMap.put(path, listener);
        NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            if (data == null){
                listener.configUpdated(null);
                return;
            }
            String config = new String(data, StandardCharsets.UTF_8);
            listener.configUpdated(config);
        });
        try{
            cache.start();
        }catch (Exception e) {
            throw new SwitchException(String.format("jswitch addListener err! err: %s", e.getMessage()));
        }
        return true;
    }

    private String getPath(String... nodes){
        return ZookeeperPathHelper.getConfigPath(nodes);
    }
}
