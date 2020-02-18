package com.tejchen.jswitchserver.helper;

import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;

public class ResponseHelper {

    public static JSwitchHttpResponse success(){
        JSwitchHttpResponse response = new JSwitchHttpResponse();
        response.setCode("100000");
        response.setMessage("成功");
        return response;
    }

    public static JSwitchHttpResponse success(Object data){
        JSwitchHttpResponse response = new JSwitchHttpResponse();
        response.setCode("100000");
        response.setMessage("成功");
        response.setData(data);
        return response;
    }
}
