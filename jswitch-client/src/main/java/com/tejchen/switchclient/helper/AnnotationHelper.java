package com.tejchen.switchclient.helper;

import com.tejchen.switchclient.model.JSwitchConfig;
import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchcommon.JSwitchException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    public static List<JSwitchConfig> parseAnnotation(String appName, Class[] classes){
        if (classes == null || classes.length == 0){
            throw new JSwitchException("empty classes!");
        }
        List<JSwitchConfig> resultList = new ArrayList<JSwitchConfig>();
        for (Class aClass : classes) {
            resultList.addAll(parseAnnotation(appName, aClass));
        }
        return resultList;
    }

    public static List<JSwitchConfig> parseAnnotation(String appName, Class aClass){
        List<JSwitchConfig> resultList = new ArrayList<JSwitchConfig>();
        if (aClass == null){
            return resultList;
        }
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            JSwitch jswitch = field.getDeclaredAnnotation(JSwitch.class);
            if (jswitch == null) {
                continue;
            }
            JSwitchConfig config = new JSwitchConfig(appName, jswitch, field);
            resultList.add(config);
        }
        return resultList;
    }
}
