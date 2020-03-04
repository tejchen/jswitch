package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class JSwitchAppConfigDetailVO {

    private String appCode;

    private String appName;

    private String appConfigCode;

    private String appConfigName;

    private String appConfigDesc;

    private String appConfigSource;

    private String appConfigType;

    private String appConfigContent;

    private List<String> appNodes;

}
