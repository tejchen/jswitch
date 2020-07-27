package com.tejchen.switchclient.helper;

import com.tejchen.switchclient.model.CacheData;
import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchcommon.JSwitchException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    public static List<CacheData> parseAnnotation(String appName, Class[] classes){
        if (classes == null || classes.length == 0){
            throw new JSwitchException("empty classes!");
        }
        List<CacheData> cacheDataList = new ArrayList<CacheData>();
        for (Class aClass : classes) {
            cacheDataList.addAll(parseAnnotation(appName, aClass));
        }
        return cacheDataList;
    }

    public static List<CacheData> parseAnnotation(String appName, Class aClass){
        List<CacheData> cacheDataList = new ArrayList<CacheData>();
        if (aClass == null){
            return cacheDataList;
        }
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            JSwitch jswitch = field.getDeclaredAnnotation(JSwitch.class);
            if (jswitch == null) {
                continue;
            }
            CacheData cacheData = new CacheData(appName, jswitch, field);
            cacheDataList.add(cacheData);
        }
        return cacheDataList;
    }
}
