package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 配置协议体
 */
@Getter
@Setter
public class JSwitchHeartbeatForm extends JSwitchBaseForm {

    @NotNull
    private String token;

    @Override
    public String getSignValue() {
        return token;
    }
}


