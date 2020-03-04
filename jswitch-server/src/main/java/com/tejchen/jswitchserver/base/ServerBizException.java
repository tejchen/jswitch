package com.tejchen.jswitchserver.base;

import lombok.Getter;

public class ServerBizException extends RuntimeException{

    @Getter
    private BizResult bizResult;

    @Getter
    private String message;

    public static void throwException(BizResult bizResult){
        throw new ServerBizException(bizResult);
    }

    public ServerBizException (BizResult bizResult){
        super(String.format("err=%s, %s", bizResult.getCode(), bizResult.getMessage()));
        this.bizResult = bizResult;
        this.message = bizResult.getMessage();
    }

    public ServerBizException (BizResult bizResult, String message){
        super(String.format("err=%s, %s", bizResult.getCode(), message));
        this.bizResult = bizResult;
        this.message = message;
    }
}
