package com.tejchen.jswitchserver.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchAppMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JSwitchAppServiceImpl extends ServiceImpl<JSwitchAppMapper, JSwitchApp> implements JSwitchAppService {

    @Override
    public Long pushAndGet(String appCode) {
        JSwitchApp app = getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app==null) {
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        boolean result = update(Wrappers.<JSwitchApp>lambdaUpdate()
                .set(JSwitchApp::getAppVersion, app.getAppVersion() + 1)
                .eq(JSwitchApp::getAppCode, appCode)
                .eq(JSwitchApp::getAppVersion, app.getAppVersion()));
        if (result) {
            return app.getAppVersion() + 1;
        }
        return null;
    }

    @Override
    public JSwitchApp get(String appCode) {
        JSwitchApp app = getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        return app;
    }

    @Override
    public List<JSwitchApp> list(List<String> appCodeList) {
        if (appCodeList == null || appCodeList.isEmpty()){
            ServerBizException.throwException(BizResult.PARAMETER_CHECK_FAIL);
        }
        List<JSwitchApp> appList = list(Wrappers.<JSwitchApp>lambdaQuery().in(JSwitchApp::getAppCode, appCodeList));
        return appList;
    }
}
