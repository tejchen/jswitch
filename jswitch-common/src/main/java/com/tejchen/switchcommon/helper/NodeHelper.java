package com.tejchen.switchcommon.helper;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class NodeHelper {
    private static final Logger logger = LoggerFactory.getLogger(NodeHelper.class);
    private static String nodeToken        = null;

    /**
     *生成机器指纹
     * @return
     */
    public static String generateToken() {
        if (nodeToken != null){
            return nodeToken;
        }
        synchronized (NodeHelper.class) {
            if (nodeToken == null) {
                // 只需生成一次
                List<String> tokenList = new ArrayList<>();
                try {
                    Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (allNetInterfaces.hasMoreElements()) {
                        NetworkInterface netInterface = allNetInterfaces.nextElement();
                        if (netInterface.isLoopback() || netInterface.isVirtual() || netInterface.isPointToPoint() || !netInterface.isUp()) {
                            continue;
                        } else {
                            byte[] mac = netInterface.getHardwareAddress();
                            if (mac != null) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < mac.length; i++) {
                                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                                }
                                if (sb.length() > 0) {
                                    tokenList.add(sb.toString());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("机器指纹获取失败", e);
                }
                // 取不到mac地址时，生成一个UUID
                if (tokenList.isEmpty()){
                    return UUID.randomUUID().toString().replaceAll("-", "");
                }
                tokenList.forEach(x->logger.debug(x));
                String token = tokenList.stream().sorted().reduce(String::concat).get();
                token = sign(token);
                logger.debug("机器指纹:{}", token);
                NodeHelper.nodeToken = token;
            }
            return nodeToken;
        }

    }

    /**
     * 节点参数签名
     * @param target
     * @return
     */
    public static String sign(String target){
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(target.getBytes(Charsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
}
