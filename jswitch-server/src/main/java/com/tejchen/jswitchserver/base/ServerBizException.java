package com.tejchen.jswitchserver.base;

public class ServerBizException extends RuntimeException{

    public static void throwException(BizResult bizResult){
        throw new ServerBizException(bizResult);
    }

    public ServerBizException (BizResult bizResult){
        super(String.format("err=%s, %s", bizResult.getCode(), bizResult.getMessage()));
    }
}
