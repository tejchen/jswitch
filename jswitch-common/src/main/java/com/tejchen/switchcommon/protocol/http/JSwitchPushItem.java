package com.tejchen.switchcommon.protocol.http;

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
    private String configKey;

    @NotNull
    private String defaultValue;
}
