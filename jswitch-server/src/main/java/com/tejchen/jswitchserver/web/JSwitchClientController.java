package com.tejchen.jswitchserver.web;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tejchen.jswitchserver.base.BizResult;
import com.tejchen.jswitchserver.base.ServerBizException;
import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.jswitchserver.mapper.JSwitchApp;
import com.tejchen.jswitchserver.service.JSwitchAppService;
import com.tejchen.switchcommon.helper.HttpPathHelper;
import com.tejchen.switchcommon.protocol.http.*;
import com.tejchen.switchcommon.protocol.http.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@RestController
public class JSwitchClientController {

    private AtomicInteger mockVersion = new AtomicInteger(0);
    private Map<String, Map<String, String>> mockConfig = new HashMap<>();

    @Autowired
    private JSwitchAppService appService;

    @RequestMapping(HttpPathHelper.httpPingPath)
    public JSwitchHttpResponse ping() {
        return ResponseHelper.success();
    }

    @RequestMapping(HttpPathHelper.httpBatchPath)
    public JSwitchHttpResponse<JSwitchBatchPull> batch(@Validated @RequestBody JSwitchHttpBatchForm form){
        JSwitchBatchPull batchPull = new JSwitchBatchPull();
        batchPull.setAppCode(form.getAppCode());
        // get from db
        // mock
        List<JSwitchBatchPullItem> values = new ArrayList<>();
        if (mockConfig.get(form.getAppCode()) != null) {
            mockConfig.get(form.getAppCode()).forEach((key, value) -> {
                values.add(new JSwitchBatchPullItem(){{
                    setKey(key);
                    setValue(value);
                }});
            });
        }
        batchPull.setValues(values);
        return ResponseHelper.success(batchPull);
    }

    @RequestMapping(HttpPathHelper.httpHeartbeatPath)
    public JSwitchHttpResponse<String> heartbeat(@PathVariable("appCode") String appCode, @RequestBody JSwitchHeartbeatForm form){
        JSwitchApp app = requiredCheckAndGetApp(appCode, form);
        //todo 返回版本号
        return ResponseHelper.success();
    }

    @RequestMapping(value = HttpPathHelper.httpPushPath, method = RequestMethod.POST)
    public JSwitchHttpResponse<String> push(@Validated @RequestBody JSwitchPushForm protocol) {
        // todo update to db

        // mock
        Map<String, String> configMap = new HashMap<>();
        protocol.getPushConfig().forEach(config -> {
            configMap.put(config.getConfigKey(), config.getDefaultValue());
        });
        mockConfig.put(protocol.getAppCode(), configMap);
        return ResponseHelper.success();
    }

    private JSwitchApp requiredCheckAndGetApp(String appCode, JSwitchBaseForm form){
        if (appCode == null){
            throw new ServerBizException(BizResult.PARAMETER_CHECK_FAIL);
        }
        // 判断应用是否存在
        JSwitchApp app = appService.getOne(Wrappers.<JSwitchApp>lambdaQuery().eq(JSwitchApp::getAppCode, appCode));
        if (appCode == null){
            throw new ServerBizException(BizResult.DATA_NOT_EXIST);
        }

        String value = form.getSignValue();
        // TODO 检查应用签名
        String sign = DigestUtils.md5DigestAsHex("1234".getBytes());
        if (!sign.equals(form.getAppSign())){
            throw new ServerBizException(BizResult.DATA_NOT_EXIST);
        }
        return app;
    }

}
