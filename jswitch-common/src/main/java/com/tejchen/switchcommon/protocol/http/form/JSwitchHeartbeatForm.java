package com.tejchen.switchcommon.protocol.http.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配置协议体
 */
@Getter
@Setter
public class JSwitchHeartbeatForm extends JSwitchBaseForm {


    @Override
    public String getSignValue() {
        return null;
    }
}


