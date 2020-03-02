package com.tejchen.jswitchserver.helper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpPage;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;

public class ResponseHelper {

    public static JSwitchHttpResponse success(){
        return new JSwitchHttpResponse();
    }

    public static JSwitchHttpResponse success(Object data){
        JSwitchHttpResponse response = new JSwitchHttpResponse();
        response.setData(data);
        return response;
    }

    public static JSwitchHttpResponse with(BizResult bizResult){
        JSwitchHttpResponse response = new JSwitchHttpResponse(bizResult.getCode(), bizResult.getMessage());
        return response;
    }

    public static JSwitchHttpResponse with(BizResult bizResult, Object data){
        JSwitchHttpResponse response = new JSwitchHttpResponse(bizResult.getCode(), bizResult.getMessage(), data);
        return response;
    }

    public static JSwitchHttpResponse with(BizResult bizResult,  String message){
        JSwitchHttpResponse response = new JSwitchHttpResponse(bizResult.getCode(), message);
        return response;
    }

    public static JSwitchHttpResponse withPage(Page page){
        JSwitchHttpResponse response = new JSwitchHttpResponse();
        JSwitchHttpPage pageData = new JSwitchHttpPage();
        pageData.setPageNo(page.getCurrent());
        pageData.setPageSize(page.getSize());
        pageData.setTotal(page.getTotal());
        pageData.setTotalPage((long)Math.ceil((1.0*page.getTotal())/page.getSize()));
        pageData.setDataList(page.getRecords());
        response.setData(pageData);
        return response;
    }

    public static JSwitchHttpResponse fail(){
        return new JSwitchHttpResponse(BizResult.FAIL.getCode(), BizResult.FAIL.getMessage());
    }

}
