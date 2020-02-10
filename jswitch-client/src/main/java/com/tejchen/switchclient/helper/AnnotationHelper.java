package com.tejchen.switchclient.helper;

import com.tejchen.switchclient.annotation.SwitchApp;
import com.tejchen.switchclient.model.SwitchConfig;
import com.tejchen.switchclient.annotation.SwitchItem;
import com.tejchen.switchclient.annotation.SwitchNamespace;
import com.tejchen.switchcommon.SwitchException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    public static List<SwitchConfig> parseAnnotation(Class[] classes){
        if (classes == null || classes.length == 0){
            throw new SwitchException("empty classes!");
        }
        List<SwitchConfig> resultList = new ArrayList<SwitchConfig>();
        for (Class aClass : classes) {
            resultList.addAll(parseAnnotation(aClass));
        }
        return resultList;
    }

    public static List<SwitchConfig> parseAnnotation(Class aClass){
        List<SwitchConfig> resultList = new ArrayList<SwitchConfig>();
        if (aClass == null){
            return resultList;
        }
        SwitchApp app = (SwitchApp) aClass.getDeclaredAnnotation(SwitchApp.class);
        if (app == null || app.value() == null) {
            throw new SwitchException(String.format("%s must add SwitchApp annotation！but not", aClass.getName()));
        }
        String key = "";
        SwitchNamespace namespace = (SwitchNamespace) aClass.getDeclaredAnnotation(SwitchNamespace.class);
        if (namespace != null) {
            key = namespace.value()+"_";
        }
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            SwitchItem item = field.getDeclaredAnnotation(SwitchItem.class);
            if (item == null) {
                continue;
            }
            String configKey = key + aClass.getSimpleName() + "#" + field.getName();
            SwitchConfig config = new SwitchConfig();
            config.setConfigApp(app.value());
            config.setConfigKey(configKey);
            config.setConfigOrigin(aClass);
            config.setConfigItem(field);
            config.setConfigDefaultValue(getDefaultValue(field, aClass));
            resultList.add(config);
        }
        return resultList;
    }

    private static Object getDefaultValue(Field field, Class aClass){
        if (field == null) {
            return null;
        }
        try {
            return field.get(aClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
