package com.tejchen.switchcommon.protocol.http;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JSwitchHttpResponse<T> {

    private String code;

    private String message;

    private T data;

    public JSwitchHttpResponse(){
        this.code = "100000";
        this.message = "成功";
    }

    public JSwitchHttpResponse(String code, String message){
        this.code = code;
        this.message = message;
    }

    public JSwitchHttpResponse(String code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public boolean isSuccess() {
        return code.equals("100000");
    }

    public boolean isFail() {
        return isSuccess();
    }
}
