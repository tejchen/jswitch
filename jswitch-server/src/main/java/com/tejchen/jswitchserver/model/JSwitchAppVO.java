package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;


@Setter
@Getter
public class JSwitchAppVO {

    private Long id;

    private String appCode;

    private String appName;

    private String appDesc;

    private String appOwner;

    private List<String> appMember;

    private Long appVersion;

    private String appSignKey;

    private boolean appCheckSign;

    private String isFavorite;

    private String gmtModified;

    private String lastOperator;
}
