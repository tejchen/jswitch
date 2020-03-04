package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ConfigCreateSourceEnum;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.mapper.JSwitchAppConfig;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.model.JSwitchAppConfigListVO;
import com.tejchen.jswitchserver.model.JSwitchAppForm;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.model.JSwitchAppListVO;
import com.tejchen.jswitchserver.model.JSwitchAppVO;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jswitch/app")
public class JSwitchAppController {

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchAppNodeService appNodeService;

    @RequestMapping("/detail/{appCode}")
    public JSwitchHttpResponse get(@PathVariable("appCode") String appCode){
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        JSwitchAppVO vo = new JSwitchAppVO();
        BeanUtils.copyProperties(app, vo);
        vo.setAppCheckSign("Y".equals(app.getAppSignFlag()));
        return ResponseHelper.success(vo);
    }

    public JSwitchAppController() {
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
        // 组合在线机器数
        Multimap<String, JSwitchAppNode> nodeMultimap = ArrayListMultimap.create();
        List<String> appCodeList = (List<String>) result.getRecords().stream()
            .map(x->((JSwitchApp) x).getAppCode())
            .distinct()
            .collect(Collectors.toList());
        List<JSwitchAppNode> nodeList = appNodeService.getAliveNodeList(appCodeList);
        if (!CollectionUtils.isEmpty(nodeList)){
            nodeList.forEach(x->nodeMultimap.put(x.getAppCode(), x));
        }
        if (result.getRecords() != null) {
            List<JSwitchAppListVO> resultList = new ArrayList<>();
            result.getRecords().forEach(x -> {
                JSwitchApp app = (JSwitchApp) x;
                JSwitchAppListVO appVO = new JSwitchAppListVO();
                appVO.setAppCode(app.getAppCode());
                appVO.setAppName(app.getAppName());
                appVO.setAppDesc(app.getAppDesc());
                appVO.setAppOwner(app.getAppOwner());
                appVO.setGmtCreate(app.getGmtCreate());
                appVO.setGmtModified(app.getGmtModified());
                appVO.setOnlineMachineCount(nodeMultimap.get(app.getAppCode()) == null ? 0 : (long)nodeMultimap.get(app.getAppCode()).size());
                resultList.add(appVO);
            });
            result.setRecords(resultList);
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
        app.setAppVersion((long) 1);
        app.setAppSignFlag(appForm.isAppCheckSign()?"Y":"N");
        app.setAppSignKey(UUID.randomUUID().toString().replaceAll("-", ""));
        boolean result = appService.save(app);
        if (!result){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success(new HashMap<String, Object>(){{
            put("appSignKey", app.getAppSignKey());
            put("appVersion", app.getAppVersion());
        }});
    }

    @RequestMapping("/update")
    public JSwitchHttpResponse update(@Validated @RequestBody JSwitchAppForm appForm){
        JSwitchApp checkExist = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appForm.getAppCode()));
        if (checkExist == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        boolean result = appService.update(Wrappers.<JSwitchApp>lambdaUpdate()
                .set(JSwitchApp::getAppName, appForm.getAppName())
                .set(JSwitchApp::getAppSignFlag, appForm.isAppCheckSign()?"Y":"N")
                .set(JSwitchApp::getAppDesc, appForm.getAppDesc())
                .set(JSwitchApp::getAppOwner, appForm.getAppOwner())
                .eq(JSwitchApp::getAppCode, appForm.getAppCode())
        );
        if (!result){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        return ResponseHelper.success();
    }
}
