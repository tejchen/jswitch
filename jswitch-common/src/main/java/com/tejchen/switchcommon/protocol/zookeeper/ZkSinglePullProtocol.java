package com.tejchen.switchcommon.protocol.zookeeper;

import lombok.Getter;
import lombok.Setter;

/**
 * 配置协议体
 */
@Getter
@Setter
public class ZkSinglePullProtocol {

    private String appName;

    private String key;

    private String value;
}
