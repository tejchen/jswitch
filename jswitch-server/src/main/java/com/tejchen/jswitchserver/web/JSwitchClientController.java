package com.tejchen.jswitchserver.web;

import com.tejchen.jswitchserver.helper.ResponseHelper;
import com.tejchen.switchcommon.helper.HttpPathHelper;
import com.tejchen.switchcommon.protocol.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class JSwitchClientController {

    private AtomicInteger mockVersion = new AtomicInteger(0);
    private Map<String, Map<String, String>> mockConfig = new HashMap<>();

    @RequestMapping(HttpPathHelper.httpPingPath)
    public String ping() {
        return "pong";
    }

    @RequestMapping(HttpPathHelper.httpBatchPath)
    public JSwitchHttpResponse<JSwitchBatchPull> batch(@Validated @RequestBody JSwitchHttpBatchForm form){
        JSwitchBatchPull batchPull = new JSwitchBatchPull();
        batchPull.setAppName(form.getAppName());
        // get from db
        // mock
        List<JSwitchBatchPullItem> values = new ArrayList<>();
        if (mockConfig.get(form.getAppName()) != null) {
            mockConfig.get(form.getAppName()).forEach((key, value) -> {
                values.add(new JSwitchBatchPullItem(){{
                    setKey(key);
                    setValue(value);
                }});
            });
        }
        batchPull.setValues(values);
        return ResponseHelper.success(batchPull);
    }

    @RequestMapping(HttpPathHelper.httpVersionPath)
    public JSwitchHttpResponse<String> version(@PathVariable("appName") String appName){
        return ResponseHelper.success(String.valueOf(mockVersion.intValue()));
    }

    @RequestMapping(value = HttpPathHelper.httpPushPath, method = RequestMethod.POST)
    public JSwitchHttpResponse<String> push(@Validated @RequestBody JSwitchPushForm protocol) {
        // todo update to db

        // mock
        Map<String, String> configMap = new HashMap<>();
        protocol.getPushConfig().forEach(config -> {
            configMap.put(config.getConfigKey(), config.getDefaultValue());
        });
        mockConfig.put(protocol.getAppName(), configMap);
        return ResponseHelper.success();
    }

    @RequestMapping("/set")
    public Object set(@RequestParam String appName, @RequestParam String key, @RequestParam String value) {
        if (mockConfig.get(appName) == null) {
            mockConfig.put(appName, new HashMap<>());
        }
        mockConfig.get(appName).put(key, value);
        mockVersion.incrementAndGet();
        return "success";
    }
}
