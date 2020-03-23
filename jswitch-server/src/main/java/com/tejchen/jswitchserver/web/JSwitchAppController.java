package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.FavoriteType;
import com.tejchen.jswitchserver.base.JSwitchWebEvent;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.mapper.JSwitchUserFavorite;
import com.tejchen.jswitchserver.model.JSwitchAppForm;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.model.JSwitchAppListVO;
import com.tejchen.jswitchserver.model.JSwitchAppVO;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.jswitchserver.service.JSwitchUserFavoriteService;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jswitch/app")
public class JSwitchAppController {

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchAppNodeService appNodeService;

    @Autowired
    private JSwitchUserFavoriteService favoriteService;

    @RequestMapping("/detail/{appCode}")
    public JSwitchHttpResponse get(@PathVariable("appCode") String appCode){
        JSwitchApp app = appService.get(appCode);
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
                .like(JSwitchApp::getAppCode, keyword == null ? "" : keyword)
                .orderByDesc(JSwitchApp::getGmtModified);
        Page<JSwitchApp> result = appService.page(new Page(page, pageSize), wrapper);
        if (result.getRecords().isEmpty()){
            return ResponseHelper.withPage(result);
        }
        // 组合在线机器数
        Multimap<String, JSwitchAppNode> nodeMultimap = ArrayListMultimap.create();
        List<String> appCodeList = result.getRecords()
            .stream()
            .map(JSwitchApp::getAppCode)
            .distinct()
            .collect(Collectors.toList());
        List<JSwitchAppNode> nodeList = appNodeService.getAliveNodeList(appCodeList);
        if (!CollectionUtils.isEmpty(nodeList)){
            nodeList.forEach(x->nodeMultimap.put(x.getAppCode(), x));
        }
        // 组合用用户收藏
        List<JSwitchUserFavorite> favorites = favoriteService.list("admin", FavoriteType.APP, appCodeList);
        List<String> favoriteAppList = favorites.stream().map(JSwitchUserFavorite::getFavoriteObject).collect(Collectors.toList());
        // 更新数据
        List resultList = new ArrayList<>();
        if (result.getRecords() != null) {
            result.getRecords().forEach(app -> {
                JSwitchAppListVO appVO = new JSwitchAppListVO();
                appVO.setAppCode(app.getAppCode());
                appVO.setAppName(app.getAppName());
                appVO.setAppDesc(app.getAppDesc());
                appVO.setAppOwner(app.getAppOwner());
                appVO.setGmtCreate(app.getGmtCreate());
                appVO.setGmtModified(app.getGmtModified());
                appVO.setOnlineMachineCount(nodeMultimap.get(app.getAppCode()) == null ? 0 : (long)nodeMultimap.get(app.getAppCode()).size());
                appVO.setIsFavorite(favoriteAppList.contains(app.getAppCode()) ? "Y" : "N");
                resultList.add(appVO);
            });
        }
        return ResponseHelper.withPage(result, resultList);
    }

    @RequestMapping("/save")
    @JSwitchWebEvent(action = "新增应用", object = "appCode")
    public JSwitchHttpResponse save(HttpServletRequest request, @Validated @RequestBody JSwitchAppForm appForm){
        JSwitchApp checkExist = appService.get(appForm.getAppCode());
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
            ServerBizException.throwException(BizResult.FAIL);
        }
        return ResponseHelper.success(new HashMap<String, Object>(){{
            put("appSignKey", app.getAppSignKey());
            put("appVersion", app.getAppVersion());
        }});
    }

    @RequestMapping("/update")
    @JSwitchWebEvent(action = "更新应用", object = "appCode")
    public JSwitchHttpResponse update(HttpServletRequest request, @Validated @RequestBody JSwitchAppForm appForm){
        JSwitchApp checkExist = appService.get(appForm.getAppCode());
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
