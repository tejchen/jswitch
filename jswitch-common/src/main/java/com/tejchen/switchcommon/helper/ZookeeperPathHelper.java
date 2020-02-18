package com.tejchen.switchcommon.helper;

public class ZookeeperPathHelper {

    private static final String zookeeperBasePath = "/com/tejchen/jswitch/";
    private static final String zookeeperRegisterPath = zookeeperBasePath + "/register";

    public static String getConfigPath(String... nodes){
        String path = zookeeperBasePath;
        for (int i = 0; i < nodes.length; i++) {
            path = path + "/" + nodes[i];
        }
        return path;
    }

    public static String getPushPath(){
        return zookeeperRegisterPath + "/push";
    }

}
