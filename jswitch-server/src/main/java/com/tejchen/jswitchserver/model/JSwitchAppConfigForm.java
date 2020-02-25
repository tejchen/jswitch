package com.tejchen.jswitchserver.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Setter
@Getter
public class JSwitchAppConfigForm {

    @NotEmpty
    private String appCode;

    @NotEmpty
    private String appConfigCode;

    @NotEmpty
    private String appConfigName;

    @NotNull
    private String appConfigContent;

    private String appConfigDesc;

}
