package com.tejchen.jswitchserver.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public enum ConfigCreateSourceEnum {

    WEB("WEB", "后台添加"),
    PUSH("PUSH", "应用推送"),
    ;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String desc;

    ConfigCreateSourceEnum (String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code){
        if (code == null){
            return null;
        }
        ConfigCreateSourceEnum target =  Arrays.stream(values()).filter(x->x.getCode().equals(code)).findAny().get();
        if (target == null){
            return null;
        }
        return target.getDesc();
    }
}
