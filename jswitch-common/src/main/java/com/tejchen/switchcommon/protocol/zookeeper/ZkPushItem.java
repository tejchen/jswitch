package com.tejchen.switchcommon.protocol.zookeeper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class ZkPushItem {

    @NotNull
    private String type;

    @NotNull
    private String configCode;

    @NotNull
    private String configName;

    @NotNull
    private String defaultValue;
}
