package com.tejchen.switchclient.remote;

import com.tejchen.switchclient.model.SwitchPushConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认连接到server
 *
 */
public class DefaultSwitchServerProxy implements SwitchServerProxy{

    public boolean connect(String server) {
        return true;
    }

    public String get(String appName, String key) {
        return "new value";
    }

    @Override
    public Map<String, Map<String, String>> get(Map<String, List<String>> appKeys) {
        return new HashMap<String, Map<String, String>>(){{
            put("switch", new HashMap<String, String>(){{
                put("abc_BaseServiceTestConfig#testString","abc");
                put("abc_BaseServiceTestConfig#testInt","999");
                put("abc_BaseServiceTestConfig#testLong","999");
                put("abc_BaseServiceTestConfig#testBigDecimal","999");
                put("abc_BaseServiceTestConfig#testBool","true");
                put("abc_BaseServiceTestConfig#testList", "[\"1\",\"2\"]");
                put("abc_BaseServiceTestConfig#testListMap", "[{\"k\":[\"v\"]},{\"k1\":[\"v1\"]}]");
                put("abc_BaseServiceTestConfig#testListObject", "[{\"name\":\"tejchen\"}]");
                put("abc_BaseServiceTestConfig#testMap", "{\"name\":\"tejchen\"}");
                put("abc_BaseServiceTestConfig#testMapObject", "{\"name\":{\"name\":\"tejchen\"}}");
            }});
        }};
    }

    @Override
    public boolean push(SwitchPushConfig pushConfig) {
        return false;
    }

    public boolean addListener(SwitchListener listener) {
        return false;
    }
}
