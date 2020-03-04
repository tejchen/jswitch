package com.tejchen.switchcommon.helper;

import com.tejchen.switchcommon.JSwitchException;

public class HttpPathHelper {

    private static final String httpBasePath = "/jswitch/client";
    public static final String httpPingPath = httpBasePath + "/ping";
    public static final String httpPushPath = httpBasePath + "/push";
    public static final String httpEventPath = httpBasePath + "/event";
    public static final String httpPullPath = httpBasePath + "/pull";
    public static final String httpHeartbeatPath = httpBasePath + "/heartbeat/{appCode}";

    public static String getUrl(String server, String httpPath, String... nodes) {
        String url = server + httpPath;
        for (String node : nodes) {
            url = url.replaceFirst("\\{.*?\\}", node);
        }
        return url;
    }
}
