package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ConfigCreateSourceEnum;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchAppConfig;
import com.tejchen.jswitchserver.model.JSwitchAppConfigForm;
import com.tejchen.jswitchserver.model.JSwitchAppConfigListVO;
import com.tejchen.jswitchserver.service.JSwitchAppConfigService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jswitch/app/config")
public class JSwitchAppConfigController {

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchAppConfigService appConfigService;


    @RequestMapping("/detail/{appCode}")
    public JSwitchHttpResponse get(@PathVariable("appCode") String appCode){
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success(app);
    }

    @RequestMapping("/list")
    public JSwitchHttpResponse get(@RequestParam(required = false) String keyword, @RequestParam String appCode, @RequestParam Integer page, @RequestParam Integer pageSize){
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        Wrapper<JSwitchAppConfig> wrapper = Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, appCode)
                .likeRight(JSwitchAppConfig::getAppConfigCode, keyword == null ? "" : keyword)
                .orderByDesc(JSwitchAppConfig::getGmtModified);
        Page result = appConfigService.page(new Page(page, pageSize), wrapper);
        if (result == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        if (result.getRecords() != null) {
            List<JSwitchAppConfigListVO> resultList = new ArrayList<>();
            result.getRecords().forEach(x -> {
                JSwitchAppConfig config = (JSwitchAppConfig) x;
                JSwitchAppConfigListVO configListVO = new JSwitchAppConfigListVO();
                configListVO.setAppCode(config.getAppCode());
                configListVO.setAppName(app.getAppName());
                configListVO.setAppConfigCode(config.getAppConfigCode());
                configListVO.setAppConfigName(config.getAppConfigName());
                configListVO.setAppConfigDesc(config.getAppConfigDesc());
                configListVO.setAppConfigSource(ConfigCreateSourceEnum.getDescByCode(config.getAppConfigSource()));
                configListVO.setGmtModified(config.getGmtModified());
                resultList.add(configListVO);
            });
            result.setRecords(resultList);
        }
        return ResponseHelper.withPage(result);
    }

    @RequestMapping("/save")
    public JSwitchHttpResponse save(@Validated @RequestBody JSwitchAppConfigForm configForm){
        JSwitchAppConfig app = appConfigService.getOne(Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, configForm.getAppCode())
                .eq(JSwitchAppConfig::getAppConfigCode, configForm.getAppConfigCode()));
        if (app != null){
            ServerBizException.throwException(BizResult.DATA_ALREADY_EXIST);
        }
        JSwitchAppConfig config =  new JSwitchAppConfig();
        config.setAppCode(configForm.getAppCode());
        config.setAppConfigCode(configForm.getAppConfigCode());
        config.setAppConfigName(configForm.getAppConfigName());
        config.setAppConfigContent(configForm.getAppConfigContent());
        config.setAppConfigDesc(configForm.getAppConfigDesc());
        config.setCreateUser("admin");
        config.setLastOperateUser("admin");
        config.setAppConfigSource(ConfigCreateSourceEnum.WEB.getCode());
        boolean result = appConfigService.save(config);
        if (!result){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success();
    }
}
