package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户端服务端交互协议
 */
@Setter
@Getter
public abstract class JSwitchBaseForm {

    @NotNull
    private String appCode;

    @NotNull
    private String appSign;

    public abstract String getSignValue();
}


