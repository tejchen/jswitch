package com.tejchen.switchclient.model;

import com.tejchen.switchcommon.SerializeHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;

@Setter
@Getter
@ToString
public class SwitchConfig {

    // 应用名称
    private String      configApp;
    // 配置项 key
    private String      configKey;
    // 配置来源
    private Class       configOrigin;
    // 配置项
    private Field       configItem;
    // 配置值
    private String      configValue;
    // 默认值
    private Object      configDefaultValue;

    public void updateConfig(String configValue){
        this.configValue = configValue;
        try {
            configItem.setAccessible(true);
            if (configValue == null) {
                configItem.set(configOrigin, null);
                return;
            }
            configItem.set(configOrigin, SerializeHelper.deserialize(configItem.getType(), configValue));
        } catch (Exception e) {
            e.printStackTrace();
            // todo 打印日志
            // todo 响应异常
        }
    }
}


