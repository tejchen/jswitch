package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配置协议体
 */
@Getter
@Setter
public class JSwitchPull {

    private String appCode;

    private Long appVersion;

    private List<JSwitchPullItem> values;
}


