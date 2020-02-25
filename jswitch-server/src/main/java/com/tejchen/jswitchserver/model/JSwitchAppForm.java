package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Setter
@Getter
public class JSwitchAppForm {

    private Long id;

    @NotEmpty
    private String appCode;

    @NotEmpty
    private String appName;

    private String appDesc;

    private String appOwner;

    private String gmtCreate;

    private String gmtModified;

}
