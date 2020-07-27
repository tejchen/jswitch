package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.base.Charsets;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ConfigCreateSourceEnum;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.mapper.JSwitchAppConfig;
import com.tejchen.jswitchserver.mapper.JSwitchAppNode;
import com.tejchen.jswitchserver.service.JSwitchEventService;
import com.tejchen.switchcommon.event.JSwitchClientAckData;
import com.tejchen.switchcommon.helper.SerializeHelper;
import com.tejchen.switchcommon.protocol.http.form.JSwitchEventForm;
import com.tejchen.jswitchserver.service.JSwitchAppConfigService;
import com.tejchen.jswitchserver.service.JSwitchAppNodeService;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.switchcommon.helper.HttpPathHelper;
import com.tejchen.switchcommon.protocol.http.*;
import com.tejchen.switchcommon.protocol.http.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPull;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPullItem;
import com.tejchen.switchcommon.protocol.http.form.JSwitchHttpPullForm;
import com.tejchen.switchcommon.protocol.http.form.JSwitchPushForm;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class JSwitchClientController {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchClientController.class);

    @Autowired
    private JSwitchAppService appService;

    @Autowired
    private JSwitchAppConfigService appConfigService;

    @Autowired
    private JSwitchAppNodeService appNodeService;

    @Autowired
    private JSwitchEventService eventService;

    @RequestMapping(HttpPathHelper.httpPingPath)
    public JSwitchHttpResponse ping() {
        return ResponseHelper.success("pong");
    }

    @RequestMapping(HttpPathHelper.httpHeartbeatPath)
    public JSwitchHttpResponse<String> heartbeat(HttpServletRequest request, @PathVariable("appCode") String appCode, @RequestBody JSwitchHeartbeatForm form){
        logger.debug("收到 {} 的心跳,应用：{}", getClientIp(request), appCode);
        JSwitchApp app = requiredCheckAndGetApp(appCode, form);
        Wrapper query = Wrappers.<JSwitchAppNode>lambdaQuery()
                .eq(JSwitchAppNode::getAppCode, appCode)
                .eq(JSwitchAppNode::getAppNodeToken, form.getToken());
        JSwitchAppNode node = appNodeService.getOne(query);
        if (node == null) {
            node = new JSwitchAppNode();
            node.setAppCode(appCode);
            node.setAppNodeIp(getClientIp(request));
            node.setAppNodeToken(form.getToken());
            node.setLastHeartbeatTime(new Date());
            appNodeService.save(node);
        }else{
            // 更新心跳
            Wrapper wrapper = Wrappers.<JSwitchAppNode>lambdaUpdate()
                    .set(JSwitchAppNode::getLastHeartbeatTime, new Date())
                    .set(JSwitchAppNode::getAppNodeIp, getClientIp(request))
                    .eq(JSwitchAppNode::getAppCode, appCode)
                    .eq(JSwitchAppNode::getAppNodeToken, form.getToken());
            appNodeService.update(wrapper);
        }
        // 返回应用版本号
        JSwitchHeartbeat heartbeat = new JSwitchHeartbeat();
        heartbeat.setAppVersion(String.valueOf(app.getAppVersion()));
        return ResponseHelper.success(heartbeat);
    }

    @RequestMapping(HttpPathHelper.httpPullPath)
    public JSwitchHttpResponse<JSwitchPull> pull(@Validated @RequestBody JSwitchHttpPullForm form){
        JSwitchApp app = requiredCheckAndGetApp(form.getAppCode(), form);
        Wrapper<JSwitchAppConfig> wrapper = Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode());
        List<JSwitchAppConfig> configs = appConfigService.list(wrapper);
        if (configs == null || configs.isEmpty()){
            JSwitchPull pull = new JSwitchPull();
            pull.setAppCode(form.getAppCode());
            pull.setAppVersion(app.getAppVersion());
            pull.setValues(new ArrayList<>());
            return ResponseHelper.success(pull);
        }
        List<JSwitchPullItem> items = configs.stream().map(x->{
            JSwitchPullItem item = new JSwitchPullItem();
            item.setKey(x.getAppConfigCode());
            item.setValue(x.getAppConfigContent());
            return item;
        }).collect(Collectors.toList());
        JSwitchPull pull = new JSwitchPull();
        pull.setAppCode(form.getAppCode());
        pull.setAppVersion(app.getAppVersion());
        pull.setValues(items);
        return ResponseHelper.success(pull);
    }

    @RequestMapping(value = HttpPathHelper.httpPushPath, method = RequestMethod.POST)
    public JSwitchHttpResponse push(@Validated @RequestBody JSwitchPushForm form) {
        requiredCheckAndGetApp(form.getAppCode(), form);
        // 获取所有配置
        Wrapper<JSwitchAppConfig> wrapper = Wrappers.<JSwitchAppConfig>lambdaQuery()
                .eq(JSwitchAppConfig::getAppCode, form.getAppCode());
        List<JSwitchAppConfig> configs = appConfigService.list(wrapper);
        // 比对未注册配置
        List<String> registeredKeys = new ArrayList<>();
        if (configs != null){
            configs.forEach(config -> registeredKeys.add(config.getAppConfigCode()));
        }
        List<JSwitchPushItem> registerConfigs = new ArrayList<>();
        form.getPushConfig().forEach(config->{
            if (!registeredKeys.contains(config.getConfigCode())){
                registerConfigs.add(config);
            }
        });
        if (registerConfigs.isEmpty()){
            return ResponseHelper.success();
        }
        // 注册
        List<JSwitchAppConfig> targetConfigs = registerConfigs.stream().map(config -> {
            JSwitchAppConfig appConfig = new JSwitchAppConfig();
            appConfig.setAppCode(form.getAppCode());
            appConfig.setAppConfigCode(config.getConfigCode());
            appConfig.setAppConfigName(config.getConfigName());
            appConfig.setAppConfigContent(config.getDefaultValue());
            appConfig.setAppConfigDesc(config.getConfigDesc());
            appConfig.setAppConfigType(config.getType());
            appConfig.setAppConfigSource(ConfigCreateSourceEnum.PUSH.getCode());
            appConfig.setCreateUser("system");
            appConfig.setLastOperateUser("system");
            logger.debug("注册 {} 配置, 应用：{}", config.getConfigCode(), form.getAppCode());
            return appConfig;
        }).collect(Collectors.toList());
        appConfigService.saveBatch(targetConfigs);

        return ResponseHelper.success();
    }

    @RequestMapping(value = HttpPathHelper.httpEventPath, method = RequestMethod.POST)
    public JSwitchHttpResponse event(@Validated @RequestBody JSwitchEventForm form) {
        JSwitchClientAckData ack = SerializeHelper.deserializeJson(form.getEventData(), JSwitchClientAckData.class);
        if (ack == null){
            ServerBizException.throwException(BizResult.PARAMETER_CHECK_FAIL);
        }
        JSwitchAppNode node = appNodeService.getOne(Wrappers.<JSwitchAppNode>lambdaQuery()
            .eq(JSwitchAppNode::getAppCode, ack.getObject())
            .eq(JSwitchAppNode::getAppNodeToken, form.getToken()));
        if (node == null){
            ServerBizException.throwException(BizResult.DATA_NOT_EXIST);
        }
        eventService.collectEvent(form.getToken(), node.getAppNodeIp(), form.getEventCode(), form.getEventSn(), form.getEventData(), form.getEvenLevel());
        return ResponseHelper.success();
    }

    private JSwitchApp requiredCheckAndGetApp(String appCode, JSwitchBaseForm form){
        if (appCode == null){
            throw new ServerBizException(BizResult.PARAMETER_CHECK_FAIL);
        }
        // 判断应用是否存在
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (app == null){
            throw new ServerBizException(BizResult.DATA_NOT_EXIST, appCode+" 应用不存在");
        }
        // 判断是否检查签名
        if ("Y".equals(app.getAppSignFlag())) {
            String value = form.getSignValue();
            if (value == null) {
                value = "";
            }
            value += app.getAppSignKey();
            String sign = DigestUtils.md5DigestAsHex(value.getBytes(Charsets.UTF_8));
//            if (!sign.toLowerCase().equals(form.getAppSign().toLowerCase())){
//                throw new ServerBizException(BizResult.SIGN_CHECK_FAIL);
//            }
        }
        return app;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }
}
