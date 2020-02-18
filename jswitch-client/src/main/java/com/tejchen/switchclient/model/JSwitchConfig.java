package com.tejchen.switchclient.model;

import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.JSwitchNamespace;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.helper.SerializeHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

@Setter
@Getter
@ToString
public class JSwitchConfig {
    private static final Logger logger = LoggerFactory.getLogger(JSwitchConfig.class);

    // 应用名称
    private String      configApp;
    // 配置项 key
    private String      configKey;
    // 配置描述
    private String      configDesc;
    // 配置来源
    private Class       configOrigin;
    // 配置项
    private Field       configItem;
    // 配置值
    private String      configValue;
    // 默认值
    private String      configDefaultValue;

    public JSwitchConfig(String appName, JSwitchNamespace namespace, JSwitch jswitch, Field configItem){
        this.configApp = appName;
        this.configKey = configItem.getDeclaringClass().getSimpleName() + "#" + configItem.getName();
        if (namespace != null){
            this.configKey = String.format("%s_%s", namespace.value(), this.configKey);
        }
        this.configDesc = jswitch.desc();
        this.configOrigin = configItem.getDeclaringClass();
        this.configItem = configItem;
        try {
            Object data = configItem.get(configItem.getDeclaringClass());
            this.configDefaultValue = SerializeHelper.serialize(data);
        } catch (IllegalAccessException e) {
            logger.error("serialize default exception!", e);
            throw new JSwitchException(String.format("serialize default exception! %s#%s", configItem.getDeclaringClass().getName(), configItem.getName()));
        }
    }

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


