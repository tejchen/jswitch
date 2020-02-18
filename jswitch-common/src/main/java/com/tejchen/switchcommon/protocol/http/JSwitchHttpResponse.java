package com.tejchen.switchcommon.protocol.http;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JSwitchHttpResponse<T> {

    private String code;

    private String message;

    private T data;

    public boolean isSuccess() {
        return code.equals("100000");
    }
}
