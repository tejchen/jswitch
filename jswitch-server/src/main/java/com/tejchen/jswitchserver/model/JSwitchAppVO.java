package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Setter
@Getter
public class JSwitchAppVO {

    private Long id;

    private String appCode;

    private String appName;

    private String appDesc;

    private String appOwner;

    private Long appVersion;

    private String appSignKey;

    private boolean appCheckSign;
}
