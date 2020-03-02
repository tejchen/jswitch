package com.tejchen.jswitchserver.base;

import lombok.Getter;

public class ServerBizException extends RuntimeException{

    @Getter
    private BizResult bizResult;

    public static void throwException(BizResult bizResult){
        throw new ServerBizException(bizResult);
    }

    public ServerBizException (BizResult bizResult){
        super(String.format("err=%s, %s", bizResult.getCode(), bizResult.getMessage()));
        this.bizResult = bizResult;
    }
}
