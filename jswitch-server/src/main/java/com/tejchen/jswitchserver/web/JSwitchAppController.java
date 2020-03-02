package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.model.JSwitchAppForm;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jswitch/app")
public class JSwitchAppController {

    @Autowired
    private JSwitchAppService appService;

    @RequestMapping("/detail/{appCode}")
    public JSwitchHttpResponse get(@PathVariable("appCode") String appCode){
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success(app);
    }

    @RequestMapping("/list")
    public JSwitchHttpResponse get(@RequestParam(required = false) String keyword, @RequestParam Integer page, @RequestParam Integer pageSize){
        Wrapper<JSwitchApp> wrapper = Wrappers.<JSwitchApp>lambdaQuery()
                .likeRight(JSwitchApp::getAppCode, keyword == null ? "" : keyword)
                .orderByDesc(JSwitchApp::getGmtModified);
        Page result = appService.page(new Page(page, pageSize), wrapper);
        if (result == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.withPage(result);
    }

    @RequestMapping("/save")
    public JSwitchHttpResponse save(@Validated @RequestBody JSwitchAppForm appForm){
        JSwitchApp checkExist = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appForm.getAppCode()));
        if (checkExist != null){
            ServerBizException.throwException(BizResult.DATA_ALREADY_EXIST);
        }
        JSwitchApp app =  new JSwitchApp();
        app.setAppCode(appForm.getAppCode());
        app.setAppName(appForm.getAppName());
        app.setAppOwner("tejchen");
        app.setCreateUser("admin");
        app.setLastOperateUser("admin");
        app.setAppDesc(appForm.getAppDesc());
        boolean result = appService.save(app);
        if (!result){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success();
    }
}
