package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ConfigCreateSourceEnum;
import com.tejchen.jswitchserver.base.JSwitchWebEvent;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchAppConfig;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.mapper.JSwitchEvent;
import com.tejchen.jswitchserver.model.JSwitchAppConfigDetailVO;
import com.tejchen.jswitchserver.model.JSwitchAppConfigForm;
import com.tejchen.jswitchserver.model.JSwitchAppConfigListVO;
import com.tejchen.jswitchserver.model.JSwitchAppConfigPushResultForm;
import com.tejchen.jswitchserver.service.JSwitchAppConfigService;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.jswitchserver.service.JSwitchEventService;
import com.tejchen.switchcommon.event.AckFLag;
import com.tejchen.switchcommon.event.JSwitchClientAckData;
import com.tejchen.switchcommon.event.JSwitchClientEvent;
import com.tejchen.switchcommon.helper.SerializeHelper;
import com.tejchen.switchcommon.protocol.http.JSwitchHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jswitch/app/config")
public class JSwitchAppConfigController {

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchAppConfigService appConfigService;

    @Autowired
    private JSwitchAppNodeService appNodeService;

    @Autowired
    private JSwitchEventService eventService;


    @RequestMapping("/detail/{appConfigCode}")
    public JSwitchHttpResponse get(@PathVariable("appConfigCode") String appConfigCode, @RequestParam String appCode){
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        JSwitchAppConfig config = appConfigService.getOne(Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, appCode)
                .eq(JSwitchAppConfig::getAppConfigCode, appConfigCode));
        if (config == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        List<JSwitchAppNode> nodes = appNodeService.getAliveNodeList(appCode);
        // 组装结果
        JSwitchAppConfigDetailVO vo = new JSwitchAppConfigDetailVO();
        vo.setAppCode(app.getAppCode());
        vo.setAppName(app.getAppName());
        vo.setAppConfigCode(config.getAppConfigCode());
        vo.setAppConfigName(config.getAppConfigName());
        vo.setAppConfigSource(ConfigCreateSourceEnum.getDescByCode(config.getAppConfigSource()));
        vo.setAppConfigContent(config.getAppConfigContent());
        vo.setAppConfigDesc(config.getAppConfigDesc());
        vo.setAppConfigType(config.getAppConfigType());
        vo.setAppNodes(nodes.stream().map(x->x.getAppNodeIp()).collect(Collectors.toList()));
        return ResponseHelper.success(vo);
    }

    @RequestMapping("/list")
    public JSwitchHttpResponse list(@RequestParam(required = false) String keyword, @RequestParam String appCode, @RequestParam Integer page, @RequestParam Integer pageSize){
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
    @JSwitchWebEvent(action = "添加配置", object = "appCode")
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

    @RequestMapping("/update")
    @JSwitchWebEvent(action = "仅更新配置信息", object = "appCode")
    public JSwitchHttpResponse update(@Validated @RequestBody JSwitchAppConfigForm form){
        JSwitchAppConfig config = appConfigService.getOne(Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode())
                .eq(JSwitchAppConfig::getAppConfigCode, form.getAppConfigCode()));
        if (config == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }

        boolean result = appConfigService.update(Wrappers.<JSwitchAppConfig>lambdaUpdate()
                .set(JSwitchAppConfig::getAppConfigName, form.getAppConfigName())
                .set(JSwitchAppConfig::getAppConfigDesc, form.getAppConfigDesc())
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode())
                .eq(JSwitchAppConfig::getAppConfigCode, form.getAppConfigCode())
        );
        if (!result){
            ServerBizException.throwException(BizResult.FAIL);
        }
        return ResponseHelper.success();
    }

    @RequestMapping("/push")
    @JSwitchWebEvent(action = "修改并推送配置", object = "appCode")
    public JSwitchHttpResponse push(@Validated @RequestBody JSwitchAppConfigForm form){
        // 校验数据
        JSwitchAppConfig config = appConfigService.getOne(Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode())
                .eq(JSwitchAppConfig::getAppConfigCode, form.getAppConfigCode()));
        if (config == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }

        // 更新数据
        boolean result = appConfigService.update(Wrappers.<JSwitchAppConfig>lambdaUpdate()
                .set(JSwitchAppConfig::getAppConfigContent, form.getAppConfigContent())
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode())
                .eq(JSwitchAppConfig::getAppConfigCode, form.getAppConfigCode())
        );
        if (!result){
            ServerBizException.throwException(BizResult.FAIL);
        }

        // 更新版本
        Long newVersion = appService.pushAndGet(form.getAppCode());
        if (newVersion == null){
            ServerBizException.throwException(BizResult.FAIL);
        }

        // 查看存活机器
        List<JSwitchAppNode> nodeList = appNodeService.getAliveNodeList(form.getAppCode());


        return ResponseHelper.success(new HashMap<String, Object>(){{
            put("appCode", form.getAppCode());
            put("appNodes", nodeList);
            put("appVersion", newVersion);
        }});
    }

    @RequestMapping("/pushResult")
    public JSwitchHttpResponse pushResult(@Validated @RequestBody JSwitchAppConfigPushResultForm form){
        // 校验数据
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery()
                .eq(JSwitchApp::getAppCode, form.getAppCode()));
        if (app == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        // 如果已经有新的数据版本，那么认为本次是成功的
        if (app.getAppVersion() > form.getAppVersion()) {
            Map<String, String> result = form.getAppNodeTokens().stream().collect(Collectors.toMap(x->x, x->"success"));
            return ResponseHelper.success(result);
        }
        // 搜集事件
        List<JSwitchEvent> events = eventService.pickEvents(form.getAppNodeTokens(), JSwitchClientEvent.ACCEPT_ACK.getCode(), form.getAppCode()+"_"+form.getAppVersion());
        // 生成结果
        Map<String, String> result = form.getAppNodeTokens().stream().collect(Collectors.toMap(x->x, x->"pending"));
        if (events != null && !events.isEmpty()) {
            for (JSwitchEvent event : events) {
                // 解析
                JSwitchClientAckData ack = SerializeHelper.deserializeJson(event.getEventData(), JSwitchClientAckData.class);
                if (AckFLag.SUCCESS.equals(ack.getFlag())) {
                    result.put(event.getEventOperator(), "success");
                }else {
                    result.put(event.getEventOperator(), "fail");
                }
            }
        }
        return ResponseHelper.success(result);
    }
}
