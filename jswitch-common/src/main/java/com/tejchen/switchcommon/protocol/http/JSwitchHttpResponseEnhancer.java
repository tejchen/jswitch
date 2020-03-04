package com.tejchen.switchcommon.protocol.http;


import lombok.ToString;

public class JSwitchHttpResponseEnhancer<T> extends JSwitchHttpResponse<T> {

    public boolean isSuccess(){
        return "100000".equals(super.getCode());
    }

    public String toString(){
        return super.toString();
    }
}
