package com.tejchen.switchcommon.helper;

import com.tejchen.switchcommon.JSwitchException;

public class HttpPathHelper {

    private static final String httpBasePath = "/jswitch";
    public static final String httpPingPath = httpBasePath + "/ping";
    public static final String httpPushPath = httpBasePath + "/push";
    public static final String httpBatchPath = httpBasePath + "/batch";
    public static final String httpVersionPath = httpBasePath + "/version/{appName}";

//    public static String getConfigPath(String appName, String configKey){
//        if (appName == null || appName.isEmpty()){
//            throw new JSwitchException("appName must not empty");
//        }
//        if (configKey == null || configKey.isEmpty()){
//            throw new JSwitchException("configKey must not empty");
//        }
//        String path = httpBasePath;
//        path = path + "/" + appName;
//        path = path + "/" + configKey;
//        return path;
//    }

    public static String getUrl(String server, String httpPath, String... nodes) {
        String url = server + httpPath;
        for (String node : nodes) {
            url = url.replaceFirst("\\{.*?\\}", node);
        }
        return url;
    }
}
