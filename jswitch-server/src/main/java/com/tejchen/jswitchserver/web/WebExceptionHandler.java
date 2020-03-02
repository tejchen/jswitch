package com.tejchen.jswitchserver.web;

import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class WebExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler({ServerBizException.class})
    public @ResponseBody Object biz(ServerBizException bex){
        logger.warn("biz exception!", bex);
        return ResponseHelper.with(bex.getBizResult());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public @ResponseBody Object biz(MethodArgumentNotValidException vex){
        logger.warn("argument exception!", vex);
        return ResponseHelper.with(BizResult.FAIL, BizResult.FAIL.getCode()+":必填参数不能为空");
    }

    @ExceptionHandler({Exception.class})
    public @ResponseBody Object exception(Exception ex){
        logger.error("request error!", ex);
        return ResponseHelper.with(BizResult.FAIL);
    }
}
