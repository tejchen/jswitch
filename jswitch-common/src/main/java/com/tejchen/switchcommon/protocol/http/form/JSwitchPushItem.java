package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class JSwitchPushItem {

    @NotNull
    private String type;

    @NotNull
    private String configCode;

    @NotNull
    private String configName;

    @NotNull
    private String defaultValue;

    private String configDesc;
}
