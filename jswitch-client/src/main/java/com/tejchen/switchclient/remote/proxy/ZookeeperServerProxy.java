package com.tejchen.switchclient.remote.proxy;

import com.tejchen.switchclient.remote.JSwitchListener;
import com.tejchen.switchclient.remote.JSwitchServerProxy;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.helper.SerializeHelper;
import com.tejchen.switchcommon.helper.ZookeeperPathHelper;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPushItem;
import com.tejchen.switchcommon.protocol.zookeeper.ZkPushItem;
import com.tejchen.switchcommon.protocol.zookeeper.ZkPushProtocol;
import com.tejchen.switchcommon.protocol.zookeeper.ZkSinglePullProtocol;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ZookeeperServerProxy implements JSwitchServerProxy {

    private CuratorFramework             client;
    private Map<String, JSwitchListener> listenerMap = new HashMap<>();

    @Override
    public boolean connect(String appCode, String server) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(server, retryPolicy);
        client.start();
        return true;
    }

    @Override
    public Map<String, String> pull(String appCode, List<String> keys) {
        requiredArgument(appCode, keys);

        Map<String, String> keyValue = new HashMap<>();
        keys.forEach(key -> {
            String path = getPath(appCode,key);
            // 重试一次
            try {
                collectForPath(path, key, keyValue);
            } catch (Exception e) {
                try {
                    collectForPath(path, key, keyValue);
                } catch (Exception ex) {
                    throw new JSwitchException(String.format("get config err! err: %s", ex.getMessage()));
                }
            }
        });
        return keyValue;
    }

    private void collectForPath(String path, String key, Map<String, String> keyValue) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            byte[] data = client.getData().forPath(path);
            Object obj = SerializeHelper.deserialize(ZkSinglePullProtocol.class, new String(data, StandardCharsets.UTF_8));
            ZkSinglePullProtocol protocol = (ZkSinglePullProtocol) obj;
            keyValue.put(key, protocol.getValue());
        }
    }

    @Override
    public boolean push(String appCode, List<JSwitchPushItem> items) {
        requiredArgument(appCode, items);
        // 初始化协议参数
        ZkPushProtocol protocol = new ZkPushProtocol();
        protocol.setAppName(appCode);
        for (JSwitchPushItem item : items) {
            protocol.getPushConfig().add(new ZkPushItem(){{
                setType(item.getType());
                setConfigCode(item.getConfigCode());
                setConfigName(item.getConfigName());
                setDefaultValue(item.getDefaultValue());
            }});
        }
        // 推送
        String str = SerializeHelper.serialize(protocol);
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        String path = ZookeeperPathHelper.getPushPath();
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(path, data);
            }else{
                client.setData().forPath(path, data);
            }
        } catch (Exception e) {
            throw new JSwitchException(String.format("zookeeper push err : %s", e.getMessage()));
        }
        return true;
    }

    @Override
    public boolean addListener(JSwitchListener listener) {
        String path = getPath(listener.listenApp(), listener.listenKey());
        if (listenerMap.containsKey(path)){
            return false;
        }
        listenerMap.put(path, listener);
        NodeCache cache = new NodeCache(client, path);
        cache.getListenable().addListener(() -> {
            byte[] data = cache.getCurrentData().getData();
            if (data == null){
                listener.onChange(null);
                return;
            }
            String config = new String(data, StandardCharsets.UTF_8);
            listener.onChange(config);
        });
        try{
            cache.start();
        }catch (Exception e) {
            throw new JSwitchException(String.format("jswitch addListener err! err: %s", e.getMessage()));
        }
        return true;
    }

    private String getPath(String... nodes){
        return ZookeeperPathHelper.getConfigPath(nodes);
    }

    private void requiredArgument(String appCode, Collection collection){
        if (client == null) {
            throw new JSwitchException("zookeeper client not connect!");
        }
        if (appCode == null || collection == null || collection.isEmpty()) {
            throw new JSwitchException("argument required!");
        }
    }
}
