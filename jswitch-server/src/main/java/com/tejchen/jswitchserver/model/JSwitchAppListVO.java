package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Setter
@Getter
public class JSwitchAppListVO {

    private Long id;

    private String appCode;

    private String appName;

    private String appDesc;

    private String appOwner;

    private String gmtCreate;

    private String gmtModified;

    private Long onlineMachineCount;


}
