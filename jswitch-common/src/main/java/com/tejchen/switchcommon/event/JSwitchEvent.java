package com.tejchen.switchcommon.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public enum JSwitchEvent {

    ACCEPT_ACK("ACCEPT_ACK", "响应推送配置更新")
    ;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String desc;



    JSwitchEvent(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static JSwitchEvent formatEvent(String code){
        if (code == null){
            return null;
        }
        return Arrays.stream(values()).filter(x->code.equals(x.getCode())).findAny().get();
    }
}
